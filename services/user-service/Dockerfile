# Build with the repository root as context:
# docker build -f services/user-service/Dockerfile -t amazonlike/user-service:phase0 .
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/maven-wrapper.properties
# Maven must read every reactor POM, even when selecting one service with -pl.
COPY services/auth-service/pom.xml services/auth-service/pom.xml
COPY services/user-service/pom.xml services/user-service/pom.xml
COPY services/api-gateway/pom.xml services/api-gateway/pom.xml
COPY shared/shared-common/pom.xml shared/shared-common/pom.xml
COPY services/user-service/src/main services/user-service/src/main
COPY shared/shared-common/src/main shared/shared-common/src/main

# Tests run separately with Docker/Testcontainers; image packaging needs no Docker socket.
RUN --mount=type=cache,id=amazonlike-maven,target=/root/.m2,sharing=locked \
    chmod +x mvnw && ./mvnw -B -ntp -pl services/user-service -am -DskipTests package

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd spring && useradd -g spring spring
WORKDIR /app
COPY --from=builder --chown=spring:spring /workspace/services/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar
USER spring
EXPOSE 8082
# Auth/User protect health; provide an authorization header at runtime when required.
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl --fail --silent --show-error --header "Authorization: ${HEALTHCHECK_AUTHORIZATION:-}" \
        "http://localhost:${SERVER_PORT:-8082}/actuator/health" || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL project="AmazonLike" service="user-service"
