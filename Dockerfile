FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./

RUN mvn -q -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    DB_HOST=db \
    DB_PORT=5432 \
    DB_NAME=immobilienverwaltung \
    DB_USER=myuser \
    DB_PASSWORD=mypassword \
    ADMIN=admin \
    ADMIN_PW=1234 \
    DEMO_MODE=TRUE \
    PORT=8080

EXPOSE 8080

COPY DEMO /app/DEMO
RUN mkdir -p /app/data

COPY --from=build /workspace/target/immobilienverwaltung-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
