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
  - [Grundfunktionalität](#grundfunktionalität)
  - [Priorität 2](#priorität-2)
  - [Priorität 3](#priorität-3)
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

<a id="grundfunktionalität"></a>

### [x] Grundfunktionalität

- **Immobilienverwaltung**: Erstellen, Ansehen und Verwalten von Immobiliendetails und -zusammenfassungen.
  - Adresse
  - Immobilientyp
  - Fotos / Dokumente
  - Anzahl der Häuser/Wohnungen
  - Namen zuweisen
  - Quadratmeter
  - Stockwerk
- **Mieterverwaltung**: Einrichten von Mietverhältnissen.
  - Telefonnummer
  - Einnahmen/Ausgaben
  - Verträge erstellen
  - Zählerstände
  - Dokumente
  - Vertragserstellung (Übergabeprotokoll usw.)
- **Dashboard**:
  - Anzahl der Immobilien
  - Einnahmen
- Such- und Filterfunktionen

<a id="priorität-2"></a>

### [ ] Priorität 2

- Dauer des Mietverhältnisses, Benachrichtigung bei Beendigung
- Benutzerrollen: Admin/Benutzer/Verwalter
- Mietdetails:
  - Karte mit Standort der Immobilie anzeigen

<a id="priorität-3"></a>

### [ ] Priorität 3

- Erstellung eines Exposés aus den Informationen als PDF
- Automatisches Erstellen von Mietverträgen mit Mieterdaten
- E-Mail-Support

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
    ADRESSE ||--|| WOHNUNG : "gehoert zu"
    ADRESSE {
        string WohnungID PK
        int Postleitzahl FK
        string Stadt
        string Strasse
        string Hausnummer
    }
    POSTLEITZAHL ||--|{ ADRESSE : "gehoert zu"
    POSTLEITZAHL {
        int Postleitzahl PK
        string Stadt
        string Land
    }
    WOHNUNG ||--o| MIETER : "hat"
    WOHNUNG {
        string WohnungId PK
        int GesamtQuadratmeter
        int Baujahr
        int AnzahlBaeder
        int AnzahlSchlafzimmer
        boolean Balkon
        boolean Terrasse
        boolean Garten
        boolean Klimaanlage
    }
    MIETER ||--o| DOKUMENT : "unterzeichnet"
    MIETER {
        string MieterID PK
        string WohnungId FK
        string Name
        string Vorname
        long Telefonnummer
        int Einkommen
        int Ausgaben
        date Mietbeginn
        date Mietende
        int Kaution
        int AnzahlBewohner
    }
    WOHNUNG ||--o{ DOKUMENT : "enthaelt"
    DOKUMENT {
        string DokumentID PK
        string WohnungId FK
        string MieterID FK
        string Dokumenttyp
        string Dateipfad
    }
    WOHNUNG ||--|{ ZAEHLERSTAND : "aufgezeichnet"
    ZAEHLERSTAND {
        string ZaehlerstandID PK
        string WohnungId FK
        date Ablesedatum
        string Messwert
    }
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