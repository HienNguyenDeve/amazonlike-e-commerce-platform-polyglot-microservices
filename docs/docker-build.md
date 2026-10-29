# Docker builds (Phase 0)

Run all commands from the repository root. Docker/BuildKit and registry access
are required on the first build. Each Dockerfile uses Java 21, the project's
Maven Wrapper (Maven 3.9.10), and a Maven dependency cache. No host-built JAR,
local Maven repository, or development `.env` is copied into an image.

```sh
docker build --progress=plain -f services/auth-service/Dockerfile -t amazonlike/auth-service:phase0 .
docker build --progress=plain -f services/user-service/Dockerfile -t amazonlike/user-service:phase0 .
docker build --progress=plain -f services/api-gateway/Dockerfile -t amazonlike/api-gateway:phase0 .
python3 infrastructure/docker/smoke-test.py
```

The root context is required: Maven reads the parent and every active reactor
module's POM. `-pl services/<service> -am` builds the selected application and
its dependencies; Auth/User include Shared Common. Product is not in the
reactor. Only main sources for the selected application (and Shared Common
where needed) are copied into the builder. Test sources are deliberately not
part of the image build, and `-DskipTests` keeps packaging independent of a
Docker socket. Run regression tests separately:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw -o -fae -pl services/auth-service,services/user-service,services/api-gateway -am verify
```

Omit `-o` when Maven dependencies have not been cached yet.

## Isolated runtime smoke test

`docker-compose.smoke.yml` is independent of the existing development Compose
stack. It defines two disposable PostgreSQL databases backed by tmpfs, Redis,
a single-node Kafka broker advertised as `kafka:9092`, and the three applications.
Redis and all three Kafka image-declared volume paths also use tmpfs so that
`compose down` leaves no anonymous test volumes behind.
It mounts existing test configuration read-only and publishes application
ports dynamically on localhost. It does not load `.env`, use named development
containers, or attach existing data volumes.

The Python standard-library runner:

- Checks the executable manifest, Boot loader, `BOOT-INF`, and the bundled
  Shared Common JAR inside the images.
- Generates a short-lived JWT using public test-only signing material and
  uses test-only Basic authentication for User Service health.
- Waits for dependency and application Docker healthchecks, then checks each
  application's HTTP health response and Gateway anonymous-access rejection.
- Deletes temporary extracted JARs immediately after checking them.
- Saves diagnostic logs under a temporary directory and runs `compose down`
  only for its uniquely named project, including on failure. It never uses
  `down -v` or prune. Only its own disposable resources are removed.

Images run as the non-root `spring` user. The JRE stage explicitly installs
curl for healthchecks. Auth/User keep their actual security configuration;
when an endpoint requires authentication, supply `HEALTHCHECK_AUTHORIZATION`
at runtime rather than opening the endpoint. The variable is a complete
Authorization header value; do not bake it into the image.

## Development/runtime configuration

The existing `docker-compose.yml` remains unchanged and still starts the
existing development infrastructure. Its commented Auth/User build examples now use the
root context without enabling these services against development data.
The smoke Compose file provides active root-context build definitions for all three applications.

For other runtime environments, supply the variables referenced by each
service's `application.properties`, or mount an external Spring configuration.
Auth needs PostgreSQL, Redis, and a Kafka broker for event delivery; User needs
PostgreSQL, Kafka, and Redis for its auto-configured health indicator; Gateway needs reachable service URLs. The existing
production Gateway properties still use localhost URLs (and an incorrect
Product URL); the smoke stack overrides only Auth/User routes with Docker DNS
names. Product and its route are outside this task. The development Kafka
advertised listener is also localhost, so it must not be reused as-is by
application containers. These production configuration concerns are separate
from source-to-image packaging and the isolated smoke test.
