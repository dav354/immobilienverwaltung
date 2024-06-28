# Aufbau und Architektur

Die Anwendung folgt einer mehrschichtigen Architektur:

## Präsentationsschicht (UI)
Diese Schicht ist verantwortlich für die Benutzerinteraktion. Sie enthält verschiedene Views und Formulare, die dem Benutzer ermöglichen, Daten einzugeben und zu bearbeiten sowie Informationen anzuzeigen.

- **Views**: Hauptansichten der Anwendung, die Daten anzeigen und Interaktionen ermöglichen.
    - `MieterListView`: Ansicht zur Verwaltung und Anzeige der Mieter.
    - `WohnungListView`: Ansicht zur Verwaltung und Anzeige der Wohnungen.
    - `WohnungDetailsView`: Detaillierte Ansicht einer einzelnen Wohnung.
    - `MainView`: Startseite der Anwendung mit statistischen Übersichten.

- **Formulare und Dialoge**: Ermöglichen die Eingabe und Bearbeitung von Daten.
    - `MieterForm`: Formular zur Eingabe und Bearbeitung von Mieterdaten.
    - `WohnungEditDialog`: Dialog zur Bearbeitung von Wohnungsdaten.
    - `ZaehlerstandDialog`: Dialog zur Eingabe und Bearbeitung von Zählerständen.

## Service-Schicht
Diese Schicht enthält die Geschäftslogik und Service-Klassen, die zwischen der UI und der Datenbankschicht vermitteln. Sie führen die eigentlichen Operationen und Verarbeitungen der Daten durch.

- **Service-Klassen**:
    - `DashboardService`: Berechnet Mieteinnahmen und liefert Statistiken.
    - `MieterService`: Verarbeitet Mieterdaten und führt Geschäftslogik aus.
    - `MietvertragService`: Verarbeitet Mietvertragsdaten.
    - `WohnungService`: Verarbeitet Wohnungsdaten.
    - `DokumentService`: Verarbeitet Dokumentdaten.
    - `ZaehlerstandService`: Verarbeitet Zählerstandsdaten.
    - `SecurityService`: Verwaltung der Authentifizierung und Autorisierung.
    - `UserService`: Verwaltung der Benutzerdaten und Rollen.
    - `GeocodingService`: Abrufen von Geokoordinaten für Adressen.

## Datenzugriffsschicht (Repository)
Diese Schicht interagiert direkt mit der Datenbank und führt CRUD-Operationen (Create, Read, Update, Delete) aus. Sie ist für das Speichern, Abrufen, Aktualisieren und Löschen von Daten verantwortlich.

- **Repository-Klassen**:
    - `MieterRepository`: Verwaltung der Mieter-Datenbankoperationen.
    - `MietvertragRepository`: Verwaltung der Mietvertrags-Datenbankoperationen.
    - `WohnungRepository`: Verwaltung der Wohnungs-Datenbankoperationen.
    - `DokumentRepository`: Verwaltung der Dokument-Datenbankoperationen.
    - `ZaehlerstandRepository`: Verwaltung der Zählerstand-Datenbankoperationen.
    - `UserRepository`: Verwaltung der Benutzerdatenbankoperationen.
    - `RoleRepository`: Verwaltung der Rollen-Datenbankoperationen.