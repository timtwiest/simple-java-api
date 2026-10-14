ARG BUILD_IMAGE=maven:3.10.0-eclipse-temurin-25-alpine
ARG RUNTIME_IMAGE=eclipse-temurin:25-jre-alpine

FROM ${BUILD_IMAGE} AS build
WORKDIR /usr/src/app

COPY pom.xml ./pom.xml
COPY simple-java-api-api/pom.xml ./simple-java-api-api/
COPY simple-java-api-app/pom.xml ./simple-java-api-app/
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline -DskipTests

COPY simple-java-api-api ./simple-java-api-api
COPY simple-java-api-app ./simple-java-api-app
RUN mvn --batch-mode --no-transfer-progress package -DskipTests -DskipITs

FROM ${RUNTIME_IMAGE}
ARG PORT=9080

WORKDIR /app

RUN addgroup --system app && adduser --system --ingroup app app

COPY --from=build --chown=app:app /usr/src/app/simple-java-api-app/target/*.jar app.jar

USER app

EXPOSE ${PORT}

HEALTHCHECK --interval=5m --timeout=3s --start-period=1m CMD wget --quiet --tries=1 --spider http://localhost:${PORT}/liveness || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
