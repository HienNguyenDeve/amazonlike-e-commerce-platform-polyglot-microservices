#!/usr/bin/env python3
"""Check the three built images using an isolated, disposable Compose project."""
import base64
import hashlib
import hmac
import json
import os
from pathlib import Path
import subprocess
import tempfile
import time
import urllib.error
import urllib.request
import uuid
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[2]
SERVICES = {"auth-service": 8085, "user-service": 8082, "api-gateway": 8080}


def main():
    log_dir = Path(tempfile.mkdtemp(prefix="amazonlike-docker-smoke-"))
    project = "phase0-smoke-" + uuid.uuid4().hex[:10]
    # Public, disposable test material. Never read development .env or credentials.
    key = b"phase0-docker-smoke-only-signing-key-32-bytes"
    def encode(value):
        return base64.urlsafe_b64encode(value).rstrip(b"=").decode()
    header = encode(json.dumps({"alg": "HS256", "typ": "JWT"}).encode())
    payload = encode(json.dumps({"sub": "smoke-user", "roles": "ROLE_USER", "exp": int(time.time()) + 1800}).encode())
    message = header + "." + payload
    token = message + "." + encode(hmac.new(key, message.encode(), hashlib.sha256).digest())
    env = {k: v for k, v in os.environ.items() if k in {"PATH", "HOME", "TMPDIR", "LANG"}}
    env.update(SMOKE_SIGNING_KEY=base64.b64encode(key).decode(), SMOKE_AUTHORIZATION="Bearer " + token)
    compose = ["docker", "compose", "--env-file", "/dev/null", "-p", project,
               "-f", str(ROOT / "infrastructure/docker/docker-compose.smoke.yml")]

    def run(args, name, timeout=60):
        try:
            output = (log_dir / name).open("w")
        except OSError as error:
            # Cleanup must still run if the filesystem cannot accept another log.
            print("Cannot write", name, ":", error.strerror, flush=True)
            subprocess.run(args, cwd=ROOT, env=env, stdout=subprocess.DEVNULL,
                           stderr=subprocess.STDOUT, check=True, timeout=timeout)
        else:
            with output:
                subprocess.run(args, cwd=ROOT, env=env, stdout=output,
                               stderr=subprocess.STDOUT, check=True, timeout=timeout)

    opener = urllib.request.build_opener(urllib.request.ProxyHandler({}))
    started = False
    try:
        run(compose + ["config", "--quiet"], "config.log")
        for service in SERVICES:
            image = "amazonlike/" + service + ":phase0"
            version = subprocess.check_output(["docker", "run", "--rm", "--entrypoint", "java", image, "-version"],
                                              env=env, stderr=subprocess.STDOUT, text=True, timeout=30)
            assert 'version "21.' in version, "Runtime must use Java 21"
            user = subprocess.check_output(["docker", "image", "inspect", "--format", "{{.Config.User}}", image],
                                           env=env, text=True, timeout=30).strip()
            assert user == "spring", "Runtime must use the non-root spring user"
            jar = log_dir / (service + ".jar")
            with jar.open("wb") as output:
                subprocess.run(["docker", "run", "--rm", "--entrypoint", "cat",
                                "amazonlike/" + service + ":phase0", "/app/app.jar"],
                               env=env, stdout=output, check=True, timeout=60)
            with ZipFile(jar) as archive:
                names = archive.namelist()
                manifest = archive.read("META-INF/MANIFEST.MF").decode()
                assert "Main-Class: org.springframework.boot.loader.launch.JarLauncher" in manifest
                assert "Start-Class: com.nguyenhien." + service.replace("-", "_") + "." in manifest
                assert "org/springframework/boot/loader/launch/JarLauncher.class" in names
                assert any(name.startswith("BOOT-INF/classes/") and name.endswith(".class") for name in names)
                assert any(name.startswith("BOOT-INF/lib/") for name in names)
                if service != "api-gateway":
                    assert "BOOT-INF/lib/shared-common-1.0.0.jar" in names
                assert not any(name.endswith("application-test.properties") for name in names)
            jar.unlink()
            print("PASS image:", service, "Java 21; non-root; executable JAR", flush=True)
        # Compose may partially create resources before `up` fails; still run
        # cleanup for this uniquely named project in that case.
        started = True
        run(compose + ["up", "-d", "--no-build", "--wait", "--wait-timeout", "240"], "up.log", timeout=300)
        for service, port in SERVICES.items():
            result = subprocess.check_output(compose + ["port", service, str(port)], env=env, text=True, timeout=30).strip()
            url = "http://" + result
            authorization = {"auth-service": "Bearer " + token,
                             "user-service": "Basic " + base64.b64encode(b"smoke-user:smoke-only-password").decode()}.get(service)
            headers = {"Authorization": authorization} if authorization else {}
            request = urllib.request.Request(url + "/actuator/health", headers=headers)
            with opener.open(request, timeout=10) as response:
                assert response.status == 200 and json.load(response)["status"] == "UP"
            container = subprocess.check_output(compose + ["ps", "-q", service], env=env, text=True, timeout=30).strip()
            health = subprocess.check_output(["docker", "inspect", "--format", "{{.State.Health.Status}}", container], env=env, text=True, timeout=30).strip()
            assert health == "healthy", (service, health)
            print("PASS runtime:", service, "Docker healthy; HTTP 200 / UP", flush=True)
            if service == "api-gateway":
                try:
                    opener.open(url + "/api/v1/users/test", timeout=10)
                    raise AssertionError("Gateway accepted anonymous protected request")
                except urllib.error.HTTPError as error:
                    assert error.code == 401, error.code
                print("PASS Gateway security: anonymous protected request HTTP 401", flush=True)
    finally:
        # Remove only this runner's extracted JARs, including partially written ones.
        for jar in log_dir.glob("*.jar"):
            jar.unlink()
        # Only this randomly named project's resources are stopped/removed. No -v or prune.
        if started:
            try:
                run(compose + ["logs", "--no-color"], "containers.log")
            finally:
                run(compose + ["down", "--timeout", "20"], "cleanup.log")
        print("Smoke project:", project, "logs:", log_dir, flush=True)


if __name__ == "__main__":
    main()
