# Komponenten

Die Anwendung folgt einer mehrschichtigen Architektur mit folgendem Aufbau:

#### Service-Klassen

- **DashboardService**: Berechnet verschiedene Statistiken, einschließlich der gesamten Mieteinnahmen und des Immobilienstatus (Anzahl der vermieteten und verfügbaren Immobilien). Die Mieteinnahmen werden durch die Summe der Mietzahlungen aus allen Mietverträgen berechnet. Statistiken umfassen z.B. die Gesamtmieteinnahmen.
- **DokumentService**: Verwaltet Dokumente, einschließlich Hochladen, Abrufen und Löschen von Dokumenten, die mit Mietern oder Wohnungen verknüpft sind. Dies umfasst beispielsweise Mietverträge, Rechnungen und Nebenkostenabrechnungen.
- **GeocodingService**: Verwendet die Nominatim-API von OpenStreetMap, um Geokoordinaten (Breiten- und Längengrade) für gegebene Adressen abzurufen.
- **MieterService**: Verwaltet Mieterdaten und Mietverträge. Bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Mietern sowie zur Überprüfung der Existenz von E-Mails. Verarbeitet Daten wie Name, Vorname, Telefonnummer, E-Mail und Einkommen der Mieter.
- **MietvertragService**: Verwaltet Mietverträge, einschließlich Erstellung, Aktualisierung und Löschung von Verträgen sowie Zuordnung von Mietern zu Wohnungen. Führt Geschäftslogik zur Berechnung der Mietdauer und zur Überprüfung der Vertragsbedingungen durch.
- **SecurityService**: Verwaltet Authentifizierung und Autorisierung der Benutzer. Bietet Methoden zur Anmeldung und Abmeldung von Benutzern sowie zur Abfrage der Rollen des angemeldeten Benutzers.
- **UserService**: Verwaltet Benutzerkonten und Rollen. Bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Benutzern sowie zur Validierung von Benutzernamen und Passwörtern.
- **WohnungService**: Verwaltet Wohnungsdaten, einschließlich Erstellung, Bearbeitung und Löschung von Wohnungen sowie Verwaltung der Verfügbarkeit und zugehöriger Dokumente und Zählerstände.
- **ZaehlerstandService**: Verwaltet Zählerstände für Wohnungen, einschließlich Erstellung, Aktualisierung und Löschung von Zählerständen. Diese umfassen beispielsweise Wasser- und Stromzählerstände.
- **ConfigurationService**: Verwaltet Konfigurationseinstellungen für die Anwendung.

#### UI-Komponenten

- **MainView**: Startseite der Anwendung, die Statistiken zu Mieteinnahmen, Immobilien und Mietern anzeigt. Enthält eine Standortübersicht, die alle Wohnungen darstellt.
- **AdminView**: Übersicht und Verwaltung von Benutzern. Zeigt eine Liste aller vom Admin erstellten Benutzer. Ermöglicht dass anlegen neuer Benutzer, sowie das ändern der Passwörter und das löschen von Benutzern.
- **WohnungListView**: Übersicht und Verwaltung der Wohnungen. Zeigt eine Liste aller Wohnungen und ermöglicht Hinzufügen Wohnungen.
- **WohnungDetailsView**: Detailansicht einer Wohnung, einschließlich Zählerstände und Dokumente. Zeigt vollständige Informationen zu einer Wohnung und ermöglicht Verwaltung der zugehörigen Daten.
- **MieterListView**: Übersicht und Verwaltung der Mieter. Zeigt eine Liste aller Mieter und ermöglicht Hinzufügen von Mietern.
- **MieterDetailsView**: Detailansicht eines Mieters, einschließlich Mietverträge und Dokumente. Zeigt vollständige Informationen zu einem Mieter und ermöglicht Verwaltung der zugehörigen Daten.
- **DokumenteDetailsView**: Detaillierte Auflistung aller vorhandenen Dokumente. Diese können auch gefiltert werden.

#### Dialoge

- **MieterEditDialog**: Dialog zur Eingabe und Bearbeitung von Mieterdaten.
- **VertragHinzufuegenDialog**: Dialog zur Eingabe und Erstellung von Mietverträgen.
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