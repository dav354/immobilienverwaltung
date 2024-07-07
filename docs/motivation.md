# Motivation

Die Verwaltung von Immobilien umfasst komplexe und zeitaufwändige Aufgaben wie Mieterverwaltung, Mietvertragsmanagement, Zählerstandserfassung und Dokumentation. Diese Prozesse sind traditionell manuell, fehleranfällig und ineffizient.

Die Hauptmotivation für die Entwicklung dieser Anwendung ist die Vereinfachung und Automatisierung dieser Verwaltungsprozesse. Durch die Digitalisierung und Zentralisierung aller relevanten Daten werden Verwaltungsaufgaben effizienter und transparenter gestaltet, was zu einer erheblichen Zeitersparnis und reduzierter Fehleranfälligkeit führt.

## Funktionen

#### 1. Mieter-Verwaltung

Die Mieter-Verwaltung ermöglicht es, Mieter zu erstellen, zu aktualisieren und zu löschen. Jeder Mieter hat mehrere Attribute, darunter Name, Vorname, Telefonnummer, E-Mail und Einkommen. Diese Attribute unterliegen strikten Validierungsregeln:

- Name und Vorname: Dürfen nur Buchstaben und Leerzeichen enthalten und müssen zwischen 1 und 100 Zeichen lang sein.
- Telefonnummer: Muss zwischen 6 und 12 Ziffern enthalten.
- E-Mail: Muss ein gültiges E-Mail-Format haben und eindeutig sein.
- Einkommen: Muss ein positiver Wert sein.

Zusätzlich können Mietern Dokumente und Mietverträge zugeordnet werden. Diese werden beim löschen eines Mieters ebenfalls mitgelöscht.
#### 2. Mietvertrag-Verwaltung

Mietverträge verknüpfen Mieter mit Wohnungen und enthalten Informationen wie Mietbeginn, Mietende, Miete, Kaution und die Anzahl der Bewohner. Die Validierungen stellen sicher, dass:

- Mietbeginn: Nicht in der Zukunft liegt.
- Mietende: Nach dem Mietbeginn liegt, falls gesetzt. Sollte dieser nicht gesetzt sein, wird ein unbefristeter Vertrag angenommen.
- Miete und Kaution: Positive Werte sind.
- Anzahl der Bewohner: Mindestens 1 beträgt.

Mietverträge können erstellt, aktualisiert und gelöscht werden, wobei die Mieter und Wohnungen erhalten bleiben werden. 
#### 3. Wohnung-Verwaltung

Wohnungen werden mit verschiedenen Attributen verwaltet, darunter Straße, Hausnummer, Postleitzahl, Stadt, Land, Gesamtquadratmeter, Baujahr, Anzahl der Bäder, Schlafzimmer sowie Ausstattungsmerkmale wie Balkon, Terrasse, Garten und Klimaanlage. Die Validierungen umfassen:

- Straße, Hausnummer, Stadt: Dürfen nicht leer sein.
- Postleitzahl: Muss zwischen 4 und 10 Ziffern enthalten.
- Gesamtquadratmeter: Muss mindestens 1 betragen.
- Baujahr: Muss eine vierstellige Jahreszahl sein und darf nicht in der Zukunft liegen.
- Anzahl der Bäder: Muss positiv sein.
- Anzahl der Schlafzimmer: Muss null oder positiv sein.

Wohnungen können ebenfalls Dokumente und Zählerstände zugeordnet werden.  Diese werden beim löschen einer Wohnung ebenfalls mitgelöscht.
#### 4. Dokumenten-Verwaltung

Dokumente können Mietern oder Wohnungen zugeordnet werden und enthalten Attribute wie Dokumenttyp und Dateipfad. Beide Felder dürfen nicht leer sein und müssen bestimmten Validierungen entsprechen. Sie können auch einzeln gelöscht, heruntergeladen oder angesehen werden werden.
#### 5. Zählerstand-Verwaltung

Zählerstände sind Wohnungen zugeordnet und enthalten das Ablesedatum und den Ablesewert. Beide Felder müssen gesetzt sein, wobei der Ablesewert positiv sein muss. Diese können auch einzeln gelöscht werden, unabhängig von der Wohnung. 
#### 6. Beziehungen und Integrität

Die Anwendung stellt sicher, dass die Beziehungen zwischen Mietern, Mietverträgen, Wohnungen, Dokumenten und Zählerständen korrekt gehandhabt werden. Beispielsweise:

- Ein Mietvertrag muss einem existierenden Mieter und einer existierenden Wohnung zugeordnet sein.
- Dokumente können entweder Mietern oder Wohnungen zugeordnet sein, nicht jedoch beiden gleichzeitig.
- Ein Zählerstand ist immer einer Wohnung zugeordnet.
- Eine Wohnung kann nur **einen** Mieter haben, mehrere Personen können nicht in einem Mietvertrag abgebildet werden.
- Ein Mieter kann auch ohne zugeordnete Wohnung existieren. Beispielsweise Bewerber für neue Wohnungen oder nachdem der Mietvertrag beendet wurde aber die Kosten noch nicht final abgerechnet wurden.
- Ein Mieter kann auch mehreren Wohnungen zugeordnet sein, also mehrere Wohnungen mieten.