# Use Cases

## Use Case 1: Hinzufügen eines neuen Mieters

### Ziel
Ein neuer Mieter soll in das System aufgenommen und gespeichert werden.

### Akteure
- Benutzer (Immobilienverwalter oder Vermieter)

### Vorbedingungen
- Der Benutzer muss im System angemeldet sein.
- Der Benutzer befindet sich auf der `MieterListView`.

### Ablauf
1. **Benutzeraktion**: Der Benutzer öffnet die `MieterListView` und klickt auf die Schaltfläche "Mieter hinzufügen".
2. **Systemreaktion**: Das `MieterForm` wird geöffnet.
3. **Benutzeraktion**: Der Benutzer gibt die Daten des neuen Mieters ein, wie Name, Vorname, Telefonnummer, E-Mail und Einkommen.
4. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Speichern".
5. **Systemreaktion**: Die eingegebenen Daten werden validiert.
    - Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.
6. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.
7. **Systemreaktion**: Die `MieterListView` wird aktualisiert und zeigt den neuen Mieter an.

### Nachbedingungen
- Der neue Mieter ist in der Datenbank gespeichert und in der `MieterListView` sichtbar.

## Use Case 2: Bearbeiten einer Wohnung

### Ziel
Die Details einer vorhandenen Wohnung sollen aktualisiert werden.

### Akteure
- Benutzer (Immobilienverwalter oder Vermieter)

### Vorbedingungen
- Der Benutzer muss im System angemeldet sein.
- Der Benutzer befindet sich auf der `WohnungListView`.

### Ablauf
1. **Benutzeraktion**: Der Benutzer öffnet die `WohnungListView` und wählt eine Wohnung aus.
2. **Systemreaktion**: Die `WohnungDetailsView` wird geöffnet und zeigt die Details der ausgewählten Wohnung.
3. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Bearbeiten".
4. **Systemreaktion**: Das `WohnungEditDialog` wird geöffnet.
5. **Benutzeraktion**: Der Benutzer ändert die gewünschten Daten und klickt auf die Schaltfläche "Speichern".
6. **Systemreaktion**: Die eingegebenen Daten werden validiert.
    - Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.
7. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.
8. **Systemreaktion**: Die `WohnungDetailsView` wird aktualisiert und zeigt die aktualisierten Daten an.

### Nachbedingungen
- Die Änderungen an der Wohnung sind in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar.

## Use Case 3: Anzeige von Mieteinnahmen

### Ziel
Die Gesamtsumme der Mieteinnahmen soll berechnet und angezeigt werden.

### Akteure
- Benutzer (Immobilienverwalter oder Vermieter)

### Vorbedingungen
- Der Benutzer muss im System angemeldet sein.

### Ablauf
1. **Benutzeraktion**: Der Benutzer öffnet die `MainView`.
2. **Systemreaktion**: Die `MainView` ruft den `DashboardService` auf, um die Mieteinnahmen zu berechnen.
3. **Systemreaktion**: Der `DashboardService` summiert alle Mieteinnahmen aus den Mietverträgen.
4. **Systemreaktion**: Die berechneten Mieteinnahmen werden in einem Div-Element in der `MainView` angezeigt.

### Nachbedingungen
- Die Mieteinnahmen werden in der `MainView` korrekt angezeigt.

## Use Case 4: Löschen eines Mietvertrags

### Ziel
Ein vorhandener Mietvertrag soll zusammen mit dem zugehörigen Mieter gelöscht werden.

### Akteure
- Benutzer (Immobilienverwalter oder Vermieter)

### Vorbedingungen
- Der Benutzer muss im System angemeldet sein.
- Der Benutzer befindet sich auf der `MieterListView`.

### Ablauf
1. **Benutzeraktion**: Der Benutzer öffnet die `MieterListView` und wählt einen Mieter aus.
2. **Systemreaktion**: Das `MieterForm` wird geöffnet.
3. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Löschen".
4. **Systemreaktion**: Ein Bestätigungsdialog wird angezeigt.
5. **Benutzeraktion**: Der Benutzer bestätigt die Löschung.
6. **Systemreaktion**: Der Mietvertrag und der zugehörige Mieter werden aus der Datenbank gelöscht.
7. **Systemreaktion**: Die `MieterListView` wird aktualisiert und zeigt die aktualisierte Liste der Mieter an.

### Nachbedingungen
- Der Mietvertrag und der zugehörige Mieter sind aus der Datenbank gelöscht und in der `MieterListView` nicht mehr sichtbar.

## Use Case 5: Erfassung eines neuen Zählerstands

### Ziel
Ein neuer Zählerstand für eine Wohnung soll erfasst und gespeichert werden.

### Akteure
- Benutzer (Immobilienverwalter oder Vermieter)

### Vorbedingungen
- Der Benutzer muss im System angemeldet sein.
- Der Benutzer befindet sich auf der `WohnungDetailsView`.

### Ablauf
1. **Benutzeraktion**: Der Benutzer öffnet die `WohnungDetailsView` und wählt die gewünschte Wohnung aus.
2. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Add Zählerstand".
3. **Systemreaktion**: Der `ZaehlerstandDialog` wird geöffnet.
4. **Benutzeraktion**: Der Benutzer gibt die Zählerstanddaten ein (z.B. Ablesedatum und Ablesewert) und klickt auf "Speichern".
5. **Systemreaktion**: Die eingegebenen Daten werden validiert.
    - Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.
6. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.
7. **Systemreaktion**: Die `WohnungDetailsView` wird aktualisiert und zeigt den neuen Zählerstand an.

### Nachbedingungen
- Der neue Zählerstand ist in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar.