# Komponenten

## Service-Klassen

- **DashboardService**: Diese Klasse berechnet verschiedene Statistiken, einschließlich der gesamten Mieteinnahmen und des Immobilienstatus (Anzahl der vermieteten und verfügbaren Immobilien).
- **DokumentService**: Diese Klasse ist für die Verwaltung von Dokumenten verantwortlich, einschließlich des Hochladens, Abrufens und Löschens von Dokumenten, die mit Mietern oder Wohnungen verknüpft sind.
- **GeocodingService**: Diese Klasse verwendet die Nominatim-API von OpenStreetMap, um Geokoordinaten (Breiten- und Längengrade) für gegebene Adressen abzurufen.
- **MieterService**: Diese Klasse verwaltet die Daten der Mieter und ihrer Mietverträge. Sie bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Mietern sowie zur Überprüfung der Existenz von E-Mails.
- **MietvertragService**: Diese Klasse verwaltet die Mietverträge, einschließlich der Erstellung, Aktualisierung und Löschung von Verträgen sowie der Zuordnung von Mietern zu Wohnungen.
- **SecurityService**: Diese Klasse verwaltet die Authentifizierung und Autorisierung der Benutzer. Sie bietet Methoden zur Anmeldung und Abmeldung von Benutzern sowie zur Abfrage der Rollen des angemeldeten Benutzers.
- **UserService**: Diese Klasse verwaltet die Benutzerkonten und Rollen. Sie bietet Methoden zum Hinzufügen, Bearbeiten und Löschen von Benutzern sowie zur Validierung von Benutzernamen und Passwörtern.
- **WohnungService**: Diese Klasse verwaltet die Daten der Wohnungen, einschließlich der Erstellung, Bearbeitung und Löschung von Wohnungen sowie der Verwaltung der Verfügbarkeit und der zugehörigen Dokumente und Zählerstände.
- **ZaehlerstandService**: Diese Klasse verwaltet die Zählerstände für die Wohnungen, einschließlich der Erstellung, Aktualisierung und Löschung von Zählerständen.

## UI-Komponenten

- **MieterForm**: Ein Formular zur Bearbeitung von Mieterdaten. Es ermöglicht die Eingabe und Validierung von Mieterinformationen und deren Speicherung in der Datenbank.
- **MieterListView**: Eine Übersicht und Verwaltung der Mieter. Diese Ansicht zeigt eine Liste aller Mieter und ermöglicht das Hinzufügen, Bearbeiten und Löschen von Mietern.
- **WohnungDetailsView**: Eine Detailansicht einer Wohnung, die Zählerstände und Dokumente einschließt. Diese Ansicht zeigt die vollständigen Informationen zu einer Wohnung und ermöglicht die Verwaltung der zugehörigen Daten.
- **WohnungListView**: Eine Übersicht und Verwaltung der Wohnungen. Diese Ansicht zeigt eine Liste aller Wohnungen und ermöglicht das Hinzufügen, Bearbeiten und Löschen von Wohnungen.
- **MainView**: Die Startseite der Anwendung, die Statistiken zu den Mieteinnahmen, Immobilien und Mietern anzeigt. Sie enthält außerdem eine Übersichtskarte, die alle Wohnungen darstellt.