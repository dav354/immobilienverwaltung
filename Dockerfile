# Verwenden eines Basis-Images mit Java Runtime
FROM openjdk:21-jre-slim

# Optionale Umgebungsvariablen für Port-Konfigurationen
ENV PORT 8080
EXPOSE $PORT

# Arbeitsverzeichnis im Container festlegen
WORKDIR /app

# Kopieren der gebauten ausführbaren JAR-Datei in den Container
COPY target/myapplication-0.0.1-SNAPSHOT.jar app.jar

# Starten der Spring Boot-Anwendung beim Start des Containers
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
