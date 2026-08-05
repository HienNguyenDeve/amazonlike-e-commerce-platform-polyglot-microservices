# ---------- Build Stage ----------
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

RUN groupadd spring && useradd -g spring spring

WORKDIR /app

COPY --from=builder /workspace/target/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]

LABEL maintainer="Nguyen Hien"

LABEL project="AmazonLike"

LABEL service="api-gateway"