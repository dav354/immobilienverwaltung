# Technologien

Wir verwenden einen etablierten und weit verbreiteten Technologie-Stack, der uns ermöglicht, effizient und zuverlässig zu arbeiten.

## Programmiersprache

- **Java 21**: Die Wahl fiel auf Java, da wir Java bereits aus den Programmiervorlesungen kennen und können. Wir nutzen die neueste LTS-Version, um von den aktuellsten Sprachfeatures und Performance-Verbesserungen zu profitieren.

## Backend

- **Spring Boot**:
    - **Spring Data JPA**: Vereinfacht den Datenzugriff und die Interaktion mit Datenbanken erheblich, indem es eine Abstraktionsschicht bietet.
    - **Spring Web**: Ermöglicht die Erstellung von Webanwendungen und RESTful APIs mit minimalem Aufwand.
    - **Spring Dev Tools**: Bietet Entwickler-Tools, die die Produktivität steigern, wie z.B. automatisches Neuladen von Anwendungen.
    - **Spring Security**: Stellt ein umfangreiches Sicherheitsframework bereit, das die Implementierung von Authentifizierungs- und Autorisierungsmechanismen erleichtert.

## Datenbanken

- **H2**: Wird während der Entwicklungsphase verwendet, da es eine in-memory Datenbank ist, die schnelle Tests und einfache Konfiguration ermöglicht.
- **PostgreSQL**: Für das Deployment wird PostgreSQL eingesetzt, eine leistungsstarke und skalierbare relationale Datenbank, die für den Produktiveinsatz bestens geeignet ist.

## Frontend

- **Vaadin**: Bietet ein umfassendes Framework für die Erstellung von modernen und reaktiven Benutzeroberflächen in Java, was die Integration mit dem Backend erleichtert.

## Build-Tool

- **Maven**: Automatisiert den Build-Prozess und das Management von Abhängigkeiten, was die Projektverwaltung effizient und konsistent macht.

## Tests

- **JUnit**: Ermöglicht das Schreiben und Ausführen von Unit-Tests, um die Qualität und Zuverlässigkeit des Codes sicherzustellen.

## Deployment

- **Docker & Docker Compose**:
    - Erlaubt die Containerisierung der Anwendung, was zu einer konsistenten Umgebung führt und das Deployment auf verschiedenen Plattformen vereinfacht. Docker sorgt dafür, dass die Anwendung überall gleich läuft, unabhängig von der zugrunde liegenden Infrastruktur.
    - Mit Docker Compose können wir Multi-Container-Anwendungen einfach definieren und ausführen, was die Verwaltung komplexer Anwendungsumgebungen erleichtert.

## Continuous Integration

- **CI Pipelines**:
    - Automatisieren den gesamten Prozess des Buildens, Testens und Deployens der Anwendung. Dies gewährleistet eine konstante Codequalität und ermöglicht eine schnelle Erkennung und Behebung von Fehlern.
    - Ermöglichen die automatisierte Erstellung dieser Dokumentation, wodurch die Konsistenz und Aktualität der Dokumentation sichergestellt wird.