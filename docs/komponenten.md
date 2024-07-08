### Komponenten

Die Anwendung folgt einer mehrschichtigen Architektur. Mit folgendem Aufbau:

#### Service-Klassen

- **DashboardService**: Berechnet verschiedene Statistiken, einschließlich der gesamten Mieteinnahmen und des Immobilienstatus (Anzahl der vermieteten und verfügbaren Immobilien).
- **DokumentService**: Verwaltet Dokumente, einschließlich Hochladen, Abrufen und Löschen von Dokumenten, die mit Mietern oder Wohnungen verknüpft sind.
- **GeocodingService**: Verwendet die Nominatim-API von OpenStreetMap, um Geokoordinaten (Breiten- und Längengrade) für gegebene Adressen abzurufen.
- **MieterService**: Verwaltet Mieterdaten und Mietverträge. Bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Mietern sowie zur Überprüfung der Existenz von E-Mails.
- **MietvertragService**: Verwaltet Mietverträge, einschließlich Erstellung, Aktualisierung und Löschung von Verträgen sowie Zuordnung von Mietern zu Wohnungen.
- **SecurityService**: Verwaltet Authentifizierung und Autorisierung der Benutzer. Bietet Methoden zur Anmeldung und Abmeldung von Benutzern sowie zur Abfrage der Rollen des angemeldeten Benutzers.
- **UserService**: Verwaltet Benutzerkonten und Rollen. Bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Benutzern sowie zur Validierung von Benutzernamen und Passwörtern.
- **WohnungService**: Verwaltet Wohnungsdaten, einschließlich Erstellung, Bearbeitung und Löschung von Wohnungen sowie Verwaltung der Verfügbarkeit und zugehöriger Dokumente und Zählerstände.
- **ZaehlerstandService**: Verwaltet Zählerstände für Wohnungen, einschließlich Erstellung, Aktualisierung und Löschung von Zählerständen.
- **ConfigurationService**: Verwaltet Konfigurationseinstellungen für die Anwendung.

#### UI-Komponenten

- **MieterForm**: Formular zur Bearbeitung von Mieterdaten. Ermöglicht Eingabe und Validierung von Mieterinformationen und deren Speicherung in der Datenbank.
- **MieterListView**: Übersicht und Verwaltung der Mieter. Zeigt eine Liste aller Mieter und ermöglicht Hinzufügen, Bearbeiten und Löschen von Mietern.
- **WohnungDetailsView**: Detailansicht einer Wohnung, einschließlich Zählerstände und Dokumente. Zeigt vollständige Informationen zu einer Wohnung und ermöglicht Verwaltung der zugehörigen Daten.
- **WohnungListView**: Übersicht und Verwaltung der Wohnungen. Zeigt eine Liste aller Wohnungen und ermöglicht Hinzufügen, Bearbeiten und Löschen von Wohnungen.
- **MainView**: Startseite der Anwendung, die Statistiken zu Mieteinnahmen, Immobilien und Mietern anzeigt. Enthält eine Übersichtskarte, die alle Wohnungen darstellt.
- **DokumenteDetailsView**: Detaillierte Auflistung aller vorhandenen Dokumente. Diese können auch gefiltert werden.

#### Dialoge

- **MieterForm**: Formular zur Eingabe und Bearbeitung von Mieterdaten.
- **WohnungEditDialog**: Dialog zur Bearbeitung von Wohnungsdaten.
- **ZaehlerstandDialog**: Dialog zur Eingabe und Bearbeitung von Zählerständen.
- **ChangePasswordDialog**: Dialog zum Ändern des Passworts des aktuellen Benutzers.
- **ConfirmationDialog**: Dialog zur Bestätigung von Löschvorgängen.

#### Datenzugriffsschicht (Repository)

- **Repository-Klassen**: Interagieren direkt mit der Datenbank und führen CRUD-Operationen (Create, Read, Update, Delete) aus. Verantwortlich für Speichern, Abrufen, Aktualisieren und Löschen von Daten.
  - **MieterRepository**: Verwaltung der Mieter-Datenbankoperationen.
  - **MietvertragRepository**: Verwaltung der Mietvertrags-Datenbankoperationen.
  - **WohnungRepository**: Verwaltung der Wohnungs-Datenbankoperationen.
  - **DokumentRepository**: Verwaltung der Dokument-Datenbankoperationen.
  - **ZaehlerstandRepository**: Verwaltung der Zählerstand-Datenbankoperationen.
  - **UserRepository**: Verwaltung der Benutzerdatenbankoperationen.
  - **RoleRepository**: Verwaltung der Rollen-Datenbankoperationen.
  - **ConfigurationRepository**: Verwaltung der Konfigurations-Datenbankoperationen.

#### Sicherheitskomponenten

- **DataLoader**: Lädt Standard-Administrationsbenutzer in die Datenbank, falls dieser noch nicht existiert.
- **SecurityConfig**: Konfiguriert die Sicherheitsanforderungen und Authentifizierungsmethoden für die Anwendung.

#### Demo-Komponenten

- **DemoModeConfig**: Verwaltet den Demo-Modus der Anwendung und überprüft, ob der Demo-Modus aktiviert ist.
- **DokumentDemo**: Initialisiert Dokument-Daten für Demo-Zwecke.
- **MieterDemo**: Initialisiert Mieter-Daten für Demo-Zwecke.
- **MietvertragDemo**: Weist Mietverträge Mietern und Wohnungen im Demo-Modus zu.
- **WohnungDemo**: Initialisiert Wohnung-Daten für Demo-Zwecke.
- **ZaehlerstandDemo**: Initialisiert Zählerstand-Daten für Demo-Zwecke.