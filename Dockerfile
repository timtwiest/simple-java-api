ARG BUILD_IMAGE=maven:3.9.11-eclipse-temurin-25-alpine
ARG RUNTIME_IMAGE=eclipse-temurin:25-jre-jammy

FROM ${BUILD_IMAGE} AS build
WORKDIR /usr/src/app

COPY pom.xml ./pom.xml
COPY simple-java-api-api/pom.xml ./simple-java-api-api/
COPY simple-java-api-app/pom.xml ./simple-java-api-app/
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline -DskipTests

COPY simple-java-api-api ./simple-java-api-api
COPY simple-java-api-app ./simple-java-api-app
RUN mvn --batch-mode --no-transfer-progress package -DskipTests

FROM ${RUNTIME_IMAGE}
ARG PORT=9080

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

RUN useradd -m dockercontainerconnect
USER dockercontainerconnect

COPY --from=build --chown=dockercontainerconnect /usr/src/app/simple-java-api-app/target/*.jar app.jar

EXPOSE ${PORT}

HEALTHCHECK --interval=5m --timeout=3s --start-period=1m CMD curl -f http://localhost:${PORT}/liveness || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
