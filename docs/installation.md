# Installation

### Voraussetzungen

Um die Anwendung zu installieren, müssen Docker und Docker Compose auf Ihrem System installiert sein. Eine Anleitung zur Installation finden Sie [hier](https://docs.docker.com/engine/install/).

### Installationsschritte

1. Laden Sie die [Docker Compose-Datei](https://github.com/dav354/immobilienverwaltung/blob/main/docker-compose.yml) herunter und passen Sie diese an Ihre Bedürfnisse an.
2. Starten Sie die Anwendung mit dem Befehl:
   ```bash
   docker compose up -d
   ```

### Konfigurationsvariablen

Folgende Einstellungen sollten noch angepasst werden: 

- **PORT**: Als Standartport ist `8080` gesetzt. Dieser kann nach Belieben geändert werden.
- **DATA**: Hier kann das Verzeichnis oder Docker Volume geändert werden, in das die Dateien beim Upload gespeichert werden.

In der Docker Compose-Datei können folgende Umgebungsvariablen konfiguriert werden:

- **DB_HOST**: Definiert den Hostnamen oder die IP-Adresse der Datenbank. Standardwert ist `db`.
- **DB_PORT**: Gibt den Port von PostgreSQL an. Standardwert ist `5432`.
- **DB_NAME**: Der Name der zu verwendenden Datenbank. Standardwert ist `immobilienverwaltung`.
- **DB_USER**: Der Benutzername für den Datenbankzugriff. Standardwert ist `myuser`.
- **DB_PASSWORD**: Das Passwort für den Datenbankbenutzer. Standardwert ist `mypassword`.
- **ADMIN**: Benutzername des Administrators. Standardwert ist `admin`.
- **ADMIN_PW**: Passwort für den Administrator. Standardwert ist `password`.
- **DEMO_MODE**: Gibt an, ob Demodaten geladen werden sollen. Kann auf `true` oder `false` gesetzt werden. Standardwert ist `true`.

Passen Sie die Variablen nach Bedarf an, um die Anwendung zu konfigurieren. Nachdem Sie die Datei angepasst haben, können Sie die Anwendung starten und auf die bereitgestellten Dienste zugreifen.

Ebenfalls wird beim Start der Anwendung gewartet, bis der Datenbank Container korrekt gestartet ist und gesund ist.