# Test environment (Phase 0)

Run from the repository root using Java 21 and the Maven Wrapper:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw -v
./mvnw -pl services/auth-service,services/user-service,services/api-gateway -am test
./mvnw -fae -pl services/auth-service,services/user-service,services/api-gateway -am verify
```

Surefire runs `Test*`, `*Test`, `*Tests`, and `*TestCase`; this includes the
three `*ApplicationTests` context tests. Failsafe runs `*IT` during
`integration-test` / `verify`. Abstract base classes and empty placeholder
classes contain no executable tests. `verify` also runs the existing quality
plugins; a quality gate failure is distinct from a test failure.

For focused context tests:

```sh
./mvnw -pl services/auth-service,services/user-service,services/api-gateway -am \
  -Dtest=AuthServiceApplicationTests,UserServiceApplicationTests,ApiGatewayApplicationTests \
  -Dsurefire.failIfNoSpecifiedTests=false test
```

The no-specified-tests option only permits prerequisite modules without the
selected classes; it does not disable any selected test.

## External dependencies

| Test group | Dependencies |
| --- | --- |
| Auth unit tests | None; collaborators are mocked |
| Auth context | PostgreSQL `postgres:16`, Redis `redis:7`; KafkaTemplate is mocked because event delivery is outside this smoke test |
| User unit tests | None; domain objects or mocked collaborators |
| User context and integration | PostgreSQL `postgres:16`, Kafka `apache/kafka:4.1.1` |
| Gateway context | None; routes are not invoked by the context test |

Docker must be running for context and integration tests. Testcontainers may
need registry access on first use. It allocates private test containers with
random host ports; it does not use the local development database or Docker
Compose services. User containers are not reusable; the Spring context is
closed after each class so cached connections cannot point at stopped
containers. Testcontainers owns cleanup of its test resources.

Spring context tests explicitly load `classpath:/application-test.properties`
from test resources, rather than development configuration. JWT signing
material in these files is a public test fixture only. Flyway migrations and
Hibernate schema validation remain enabled. Security filters remain enabled;
UserControllerIT supplies an authenticated mock user for its success case and
checks that anonymous access is rejected. This does not test a JWT login flow.

Tests are not skipped when Docker is unavailable. A Docker/registry failure
must be reported as an infrastructure error. If a sandbox blocks Docker or
Mockito agent attachment, run with the necessary execution permission rather
than disabling tests, security, or Mockito checks.

Reports: each module's `target/surefire-reports` and
`target/failsafe-reports`. A successful `test` invocation alone does not prove
that the integration tests passed; use `verify` and inspect Failsafe reports.
