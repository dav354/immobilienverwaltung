# Datenbankmodell

## Mieter
- **Attribute**:
    - `Long mieterId PK`: Eindeutige Kennung des Mieters in der Datenbank.
    - `String name`: Nachname des Mieters.
    - `String vorname`: Vorname des Mieters.
    - `String telefonnummer`: Telefonnummer des Mieters für Kontaktzwecke.
    - `String email`: E-Mail-Adresse des Mieters.
    - `double einkommen`: Monatliches Einkommen des Mieters.
- **Beziehungen**:
    - One-to-Many mit `Dokument`: Ein Mieter kann mehrere Dokumente haben.
    - One-to-Many mit `Mietvertrag`: Ein Mieter kann mehrere Mietverträge haben.

## Mietvertrag
- **Attribute**:
    - `Long mietvertrag_id PK`: Eindeutige Kennung des Mietvertrags.
    - `Long mieterId FK`: Verweis auf den Mieter, der den Vertrag unterschrieben hat.
    - `Long wohnung_id FK`: Verweis auf die Wohnung, die vermietet wird.
    - `LocalDate mietbeginn`: Startdatum des Mietvertrags.
    - `LocalDate mietende`: Enddatum des Mietvertrags (kann null sein, wenn unbefristet).
    - `double kaution`: Höhe der Kaution.
    - `double miete`: Monatliche Mietzahlung.
    - `int anzahlBewohner`: Anzahl der Personen, die in der Wohnung leben.
- **Beziehungen**:
    - One-to-One mit `Mieter`: Jeder Mietvertrag gehört zu einem Mieter.
    - One-to-One mit `Wohnung`: Jeder Mietvertrag bezieht sich auf eine Wohnung.

## Wohnung
- **Attribute**:
    - `Long wohnung_id PK`: Eindeutige Kennung der Wohnung.
    - `String strasse`: Straße, in der sich die Wohnung befindet.
    - `String hausnummer`: Hausnummer der Wohnung.
    - `String postleitzahl`: Postleitzahl der Wohnung.
    - `String stadt`: Stadt, in der sich die Wohnung befindet.
    - `String land`: Land, in dem die Wohnung liegt.
    - `int gesamtQuadratmeter`: Gesamtfläche der Wohnung in Quadratmetern.
    - `int baujahr`: Baujahr des Gebäudes.
    - `int anzahlBaeder`: Anzahl der Badezimmer in der Wohnung.
    - `int anzahlSchlafzimmer`: Anzahl der Schlafzimmer in der Wohnung.
    - `boolean hatBalkon`: Gibt an, ob die Wohnung einen Balkon hat.
    - `boolean hatTerrasse`: Gibt an, ob die Wohnung eine Terrasse hat.
    - `boolean hatGarten`: Gibt an, ob die Wohnung einen Garten hat.
    - `boolean hatKlimaanlage`: Gibt an, ob die Wohnung eine Klimaanlage hat.
    - `String stockwerk`: Stockwerk, in dem sich die Wohnung befindet.
    - `String wohnungsnummer`: Nummer der Wohnung.
    - `Double latitude`: Geografische Breite der Wohnung.
    - `Double longitude`: Geografische Länge der Wohnung.
- **Beziehungen**:
    - One-to-Many mit `Mietvertrag`: Eine Wohnung kann einen Mietverträge haben.
    - One-to-Many mit `Dokument`: Eine Wohnung kann mehrere Dokumente haben.
    - One-to-Many mit `Zaehlerstand`: Eine Wohnung kann mehrere Zählerstände haben.

## Dokument
- **Attribute**:
    - `Long dokument_id PK`: Eindeutige Kennung des Dokuments.
    - `Long wohnung_id FK`: Verweis auf die Wohnung, zu der das Dokument gehört.
    - `Long mieter_id FK`: Verweis auf den Mieter, zu dem das Dokument gehört.
    - `String dokumententyp`: Typ des Dokuments (z.B. Mietvertrag, Abrechnung).
    - `String dateipfad`: Pfad zur Datei des Dokuments.
- **Beziehungen**:
    - Many-to-One mit `Wohnung`: Jedes Dokument gehört zu einer Wohnung.
    - Many-to-One mit `Mieter`: Jedes Dokument gehört zu einem Mieter.

## Zählerstand
- **Attribute**:
    - `Long zaehlerstandId PK`: Eindeutige Kennung des Zählerstands.
    - `Long wohnung_id FK`: Verweis auf die Wohnung, zu der der Zählerstand gehört.
    - `LocalDate ablesedatum`: Datum der Ablesung des Zählerstands.
    - `double ablesewert`: Abgelesener Wert des Zählers.
    - `String name`: Name des Zählers (z.B. Wasserzähler, Stromzähler).
- **Beziehungen**:
    - Many-to-One mit `Wohnung`: Jeder Zählerstand gehört zu einer Wohnung.

Die grafische Übersicht gibt es im Anhang Figure 1.

## User und Role
- **User**:
    - `Long id PK`
    - `String username`
    - `String password`
    - `Long created_by_admin_id FK`
- **Role**:
    - `Long id PK`
    - `String name`
- **Beziehungen**:
    - Ein User kann mehrere Rollen haben (Many-to-Many Beziehung).
    - Eine Rolle kann mehreren Usern zugewiesen werden (Many-to-Many Beziehung).

Die grafische Übersicht gibt es im Anhang Figure 2.

## Konfiguration

Die Configuration-Entität dient zur Speicherung von Konfigurationsparametern in der Datenbank. Sie ermöglicht es, Schlüssel-Wert-Paare für verschiedene Einstellungen innerhalb der Anwendung zu verwalten.
Attribute

- `configKey`: Eindeutiger Schlüssel für die Konfiguration (Primärschlüssel).
- `configValue`: Wert der Konfiguration.

### Werte
- **demo.mode.disable**: Speichert den wert, wenn die Demo Daten initial geladen wurden, dass die Daten nicht bei jedem Start neu geladen werden.
- **darkMode**: Speichert die Einstellung des Dark Modes, dass die Layout Klassen darauf Zugriff haben.

Die grafische Übersicht gibt es im Anhang Figure 3.