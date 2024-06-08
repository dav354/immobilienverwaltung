<div align="center">

![Java Version](https://img.shields.io/badge/java-v21-blue.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

<img src="assets/artwork.webp" width="60%" alt="artwork"/>
<p><em>Erstellt mit DALL-E</em></p>
</div>

# Immobilienverwaltung

Diese Anwendung ermöglicht die Verwaltung von Immobilien und deren Mietern.

---

> ⚠️ **Warnung**
>
> Under heavy development

<div align="center">
    <img src="assets/progproj.gif" width="30%" alt="project"/>
</div>

## Inhaltsverzeichnis

- [Frameworks](#frameworks)
- [Funktionen](#funktionen)
- [Install](#install)
- [Mockups](#mockups)
- [Database](#database)
- [Dev Setup](#dev_setup)

<a id="frameworks"></a>

## Frameworks

- **Java 21**
- **Backend**: Spring Boot
  - **Komponenten**: Spring Data JPA, Spring Web, Spring Dev Tools, und Spring Security
- **Database**: H2, PostgreSQL
  - H2 zur Entwicklung, fürs deployment Postgres
- **Frontend**: Vaadin
- **Build-Tool**: Maven
- **Tests**: JUnit
- **Deployment**: Docker

<a id="funktionen"></a>

## Funktionen

#### 1. Mieter-Verwaltung

Die Mieter-Verwaltung ermöglicht es, Mieter zu erstellen, zu aktualisieren und zu löschen. Jeder Mieter hat mehrere Attribute, darunter Name, Vorname, Telefonnummer, E-Mail und Einkommen. Diese Attribute unterliegen strikten Validierungsregeln:

- Name und Vorname: Dürfen nur Buchstaben und Leerzeichen enthalten und müssen zwischen 1 und 100 Zeichen lang sein.
- Telefonnummer: Muss zwischen 6 und 12 Ziffern enthalten.
- E-Mail: Muss ein gültiges E-Mail-Format haben und eindeutig sein.
- Einkommen: Muss ein positiver Wert sein.

Zusätzlich können Mietern Dokumente und Mietverträge zugeordnet werden.
#### 2. Mietvertrag-Verwaltung

Mietverträge verknüpfen Mieter mit Wohnungen und enthalten Informationen wie Mietbeginn, Mietende, Miete, Kaution und die Anzahl der Bewohner. Die Validierungen stellen sicher, dass:

- Mietbeginn: Nicht in der Zukunft liegt.
- Mietende: Nach dem Mietbeginn liegt, falls gesetzt.
- Miete und Kaution: Positive Werte sind.
- Anzahl der Bewohner: Mindestens 1 beträgt.

Mietverträge können erstellt, aktualisiert und gelöscht werden, wobei die Beziehungen zu Mietern und Wohnungen berücksichtigt werden.
#### 3. Wohnung-Verwaltung

Wohnungen werden mit verschiedenen Attributen verwaltet, darunter Straße, Hausnummer, Postleitzahl, Stadt, Land, Gesamtquadratmeter, Baujahr, Anzahl der Bäder, Schlafzimmer sowie Ausstattungsmerkmale wie Balkon, Terrasse, Garten und Klimaanlage. Die Validierungen umfassen:

- Straße, Hausnummer, Stadt: Dürfen nicht leer sein und müssen bestimmten Mustern entsprechen.
- Postleitzahl: Muss zwischen 4 und 10 Ziffern enthalten.
- Gesamtquadratmeter: Muss mindestens 1 betragen.
- Baujahr: Muss eine vierstellige Jahreszahl sein und darf nicht in der Zukunft liegen.
- Anzahl der Bäder: Muss positiv sein.
- Anzahl der Schlafzimmer: Muss null oder positiv sein.

Wohnungen können ebenfalls Dokumente und Zählerstände zugeordnet werden.
#### 4. Dokumenten-Verwaltung

Dokumente können Mietern oder Wohnungen zugeordnet werden und enthalten Attribute wie Dokumenttyp und Dateipfad. Beide Felder dürfen nicht leer sein und müssen bestimmten Validierungen entsprechen.
#### 5. Zählerstand-Verwaltung

Zählerstände sind Wohnungen zugeordnet und enthalten das Ablesedatum und den Ablesewert. Beide Felder müssen gesetzt sein, wobei der Ablesewert positiv sein muss.
Beziehungen und Integrität

Die Anwendung stellt sicher, dass die Beziehungen zwischen Mietern, Mietverträgen, Wohnungen, Dokumenten und Zählerständen korrekt gehandhabt werden. Durch die Nutzung von Foreign Keys und spezifischen Validierungen wird die Datenintegrität gewährleistet. Beispielsweise:

- Ein Mietvertrag muss einem existierenden Mieter und einer existierenden Wohnung zugeordnet sein.
- Dokumente können entweder Mietern oder Wohnungen zugeordnet sein, nicht jedoch beiden gleichzeitig.

<a id="install"></a>

## Install

**Voraussetzungen:**
- Docker & Docker Compose muss [installiert](https://docs.docker.com/engine/install/) sein
- dann die [Docker compose](docker-compose.yml) herunterladen und anpassen
- dann `docker compose up -d`

<a id="mockups"></a>

### Mockups

<details>
<summary>Click to expand for Screenshots</summary>

<div align="center">
    <img src="assets/dashboard-mockup.png" alt="dashboard-mockup" width="70%"/>
    <br/>Dashboard
</div>

<div align="center">
    <img src="assets/immobiliem-mockup.png" alt="immoblien-mockup" width="70%"/>
    <br/>Immo Management
</div>

<div align="center">
    <img src="assets/add-immobilien-mockup.png" alt="add-immobilien-mockup" width="70%"/>
    <br/>Neu Hinzufügen
</div>

<div align="center">
    <img src="assets/mieter-mockup.png" alt="mieter-mockup" width="70%"/>
    <br/>Miete Hinzufügen
</div>

<div align="center">
    <img src="assets/add-mieter-mockup.png" alt="add-mieter-mockup" width="70%"/>
    <br/>Neu Hinzufügen
</div>

</details>

<a id="database"></a>

## Database

<details>
<summary>Click to expand for Database Design</summary>

```mermaid
erDiagram
  Mieter {
    Long mieterId PK
    String name
    String vorname
    String telefonnummer
    String email
    double einkommen
  }
  Mietvertrag {
    Long mietvertrag_id PK
    Long mieterId FK
    Long wohnung_id FK
    LocalDate mietbeginn
    LocalDate mietende
    double kaution
    double miete
    int anzahlBewohner
  }
  Wohnung {
    Long wohnung_id PK
    String strasse
    String hausnummer
    String postleitzahl
    String stadt
    String land
    int gesamtQuadratmeter
    int baujahr
    int anzahlBaeder
    int anzahlSchlafzimmer
    boolean hatBalkon
    boolean hatTerrasse
    boolean hatGarten
    boolean hatKlimaanlage
    String stockwerk
    String wohnungsnummer
  }
  Dokument {
    Long dokument_id PK
    Long wohnung_id FK
    Long mieter_id FK
    String dokumententyp
    String dateipfad
  }
  Zaehlerstand {
    Long zaehlerstandId PK
    Long wohnung_id FK
    LocalDate ablesedatum
    double ablesewert
  }

  Mieter ||--o{ Dokument : "hat"
  Mieter ||--o{ Mietvertrag : "hat"
  Wohnung ||--o{ Mietvertrag : "hat"
  Wohnung ||--o{ Dokument : "hat"
  Wohnung ||--o{ Zaehlerstand : "hat"

```

</details>

<a id="dev_setup"></a>

## dev Setup

**Voraussetzungen:**

- Java 21
- Git
- Maven bzw. Intellij installiert alles andere

**Setup:** Run/Debug Configuration:

1. Spring Boot auswählen
2. Modify Options: Add VM Options
3. Dort `-Dspring.profiles.active=dev` eingeben, um das Entwicklungsprofil auszuwählen
4. Modify Options: Add Environment Variable
5. Dort `demo_mode=true` eingeben, dass die Demodaten geladen werden

**Setup:** Database Connection

- Automatisch: Intellij schlägt die Verbindung vor: Nur die H2 verbinden, config wird automatisch übernommen. Die Postgres mit `-` heraus löschen
- Manuell: Auf der rechten Seite unter dem Database reiter die Verbindung zur H2 manuell hinzufügen.