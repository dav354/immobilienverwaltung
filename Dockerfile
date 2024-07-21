# Verwenden eines Basis-Images mit Java Runtime
FROM openjdk:21-slim

# Umgebungsvariablen für den Datenbankzugriff und Demomodus
ENV SPRING_PROFILES_ACTIVE=prod \
    DB_HOST=db \
    DB_PORT=5432 \
    DB_NAME=immobilienverwaltung \
    DB_USER=myuser \
    DB_PASSWORD=mypassword \
    ADMIN=admin \
    ADMIN_PW=1234 \
    DEMO_MODE=TRUE

# Optionale Umgebungsvariablen für Port-Konfigurationen
ENV PORT 8080
EXPOSE $PORT

# Arbeitsverzeichnis im Container festlegen
WORKDIR /app

# Kopieren aller notwendigen Dateien und Erstellen des Verzeichnisses für Dokumente in einem Schritt
COPY DEMO /app/DEMO
COPY target/immobilienverwaltung-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /data/documents

# Starten der Spring Boot-Anwendung beim Start des Containers
ENTRYPOINT ["java", "-jar", "/app/app.jar"]