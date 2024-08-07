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

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{4cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Variable} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Variable} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
\textbf{DB\_HOST} & Definiert den Hostnamen oder die IP-Adresse der Datenbank. Standardwert ist \texttt{db}. \\
\hline
\textbf{DB\_PORT} & Gibt den Port von PostgreSQL an. Standardwert ist \texttt{5432}. \\
\hline
\textbf{DB\_NAME} & Der Name der zu verwendenden Datenbank. Standardwert ist \texttt{immobilienverwaltung}. \\
\hline
\textbf{DB\_USER} & Der Benutzername für den Datenbankzugriff. Standardwert ist \texttt{myuser}. \\
\hline
\textbf{DB\_PASSWORD} & Das Passwort für den Datenbankbenutzer. Standardwert ist \texttt{mypassword}. \\
\hline
\textbf{ADMIN} & Benutzername des Administrators. Standardwert ist \texttt{admin}. \\
\hline
\textbf{ADMIN\_PW} & Passwort für den Administrator. Standardwert ist \texttt{password}. \\
\hline
\textbf{DEMO\_MODE} & Gibt an, ob Demodaten geladen werden sollen. Kann auf \texttt{true} oder \texttt{false} gesetzt werden. Standardwert ist \texttt{true}. \\
\hline
\end{longtable}

Passen Sie die Variablen nach Bedarf an, um die Anwendung zu konfigurieren. Nachdem Sie die Datei angepasst haben, können Sie die Anwendung starten und auf die bereitgestellten Dienste zugreifen.

Ebenfalls wird beim Start der Anwendung gewartet, bis der Datenbank Container korrekt gestartet ist und gesund ist.