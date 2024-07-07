Hier sind die Use-Cases in Tabellenform dargestellt:

# Use Cases

## Use Case 1: Hinzufügen eines neuen Mieters

| Kategorie       | Beschreibung                                                  |
|-----------------|---------------------------------------------------------------|
| **Ziel**        | Ein neuer Mieter soll in das System aufgenommen und gespeichert werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter)                 |
| **Vorbedingungen** | - Der Benutzer muss im System angemeldet sein.<br>- Der Benutzer befindet sich auf der `MieterListView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `MieterListView` und klickt auf die Schaltfläche "Mieter hinzufügen".<br>2. **Systemreaktion**: Das `MieterForm` wird geöffnet.<br>3. **Benutzeraktion**: Der Benutzer gibt die Daten des neuen Mieters ein, wie Name, Vorname, Telefonnummer, E-Mail und Einkommen.<br>4. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Speichern".<br>5. **Systemreaktion**: Die eingegebenen Daten werden validiert.<br>- Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>6. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.<br>7. **Systemreaktion**: Die `MieterListView` wird aktualisiert und zeigt den neuen Mieter an. |
| **Nachbedingungen** | - Der neue Mieter ist in der Datenbank gespeichert und in der `MieterListView` sichtbar. |

## Use Case 2: Bearbeiten einer Wohnung

| Kategorie       | Beschreibung                                                  |
|-----------------|---------------------------------------------------------------|
| **Ziel**        | Die Details einer vorhandenen Wohnung sollen aktualisiert werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter)                 |
| **Vorbedingungen** | - Der Benutzer muss im System angemeldet sein.<br>- Der Benutzer befindet sich auf der `WohnungListView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `WohnungListView` und wählt eine Wohnung aus.<br>2. **Systemreaktion**: Die `WohnungDetailsView` wird geöffnet und zeigt die Details der ausgewählten Wohnung.<br>3. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Bearbeiten".<br>4. **Systemreaktion**: Das `WohnungEditDialog` wird geöffnet.<br>5. **Benutzeraktion**: Der Benutzer ändert die gewünschten Daten und klickt auf die Schaltfläche "Speichern".<br>6. **Systemreaktion**: Die eingegebenen Daten werden validiert.<br>- Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>7. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.<br>8. **Systemreaktion**: Die `WohnungDetailsView` wird aktualisiert und zeigt die aktualisierten Daten an. |
| **Nachbedingungen** | - Die Änderungen an der Wohnung sind in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar. |

## Use Case 3: Anzeige von Mieteinnahmen

| Kategorie       | Beschreibung                                                  |
|-----------------|---------------------------------------------------------------|
| **Ziel**        | Die Gesamtsumme der Mieteinnahmen soll berechnet und angezeigt werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter)                 |
| **Vorbedingungen** | - Der Benutzer muss im System angemeldet sein. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `MainView`.<br>2. **Systemreaktion**: Die `MainView` ruft den `DashboardService` auf, um die Mieteinnahmen zu berechnen.<br>3. **Systemreaktion**: Der `DashboardService` summiert alle Mieteinnahmen aus den Mietverträgen.<br>4. **Systemreaktion**: Die berechneten Mieteinnahmen werden in einem Div-Element in der `MainView` angezeigt. |
| **Nachbedingungen** | - Die Mieteinnahmen werden in der `MainView` korrekt angezeigt. |

## Use Case 4: Löschen eines Mietvertrags

| Kategorie       | Beschreibung                                                  |
|-----------------|---------------------------------------------------------------|
| **Ziel**        | Ein vorhandener Mietvertrag soll zusammen mit dem zugehörigen Mieter gelöscht werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter)                 |
| **Vorbedingungen** | - Der Benutzer muss im System angemeldet sein.<br>- Der Benutzer befindet sich auf der `MieterListView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `MieterListView` und wählt einen Mieter aus.<br>2. **Systemreaktion**: Das `MieterForm` wird geöffnet.<br>3. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Löschen".<br>4. **Systemreaktion**: Ein Bestätigungsdialog wird angezeigt.<br>5. **Benutzeraktion**: Der Benutzer bestätigt die Löschung.<br>6. **Systemreaktion**: Der Mietvertrag und der zugehörige Mieter werden aus der Datenbank gelöscht.<br>7. **Systemreaktion**: Die `MieterListView` wird aktualisiert und zeigt die aktualisierte Liste der Mieter an. |
| **Nachbedingungen** | - Der Mietvertrag und der zugehörige Mieter sind aus der Datenbank gelöscht und in der `MieterListView` nicht mehr sichtbar. |

## Use Case 5: Erfassung eines neuen Zählerstands

| Kategorie       | Beschreibung                                                  |
|-----------------|---------------------------------------------------------------|
| **Ziel**        | Ein neuer Zählerstand für eine Wohnung soll erfasst und gespeichert werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter)                 |
| **Vorbedingungen** | - Der Benutzer muss im System angemeldet sein.<br>- Der Benutzer befindet sich auf der `WohnungDetailsView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `WohnungDetailsView` und wählt die gewünschte Wohnung aus.<br>2. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Add Zählerstand".<br>3. **Systemreaktion**: Der `ZaehlerstandDialog` wird geöffnet.<br>4. **Benutzeraktion**: Der Benutzer gibt die Zählerstanddaten ein (z.B. Ablesedatum und Ablesewert) und klickt auf "Speichern".<br>5. **Systemreaktion**: Die eingegebenen Daten werden validiert.<br>- Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>6. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.<br>7. **Systemreaktion**: Die `WohnungDetailsView` wird aktualisiert und zeigt den neuen Zählerstand an. |
| **Nachbedingungen** | - Der neue Zählerstand ist in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar. |


### Use Case 6: Hinzufügen eines neuen Mietvertrags

| Abschnitt       | Beschreibung |
|-----------------|--------------|
| **Ziel**        | Ein neuer Mietvertrag soll im System erstellt und gespeichert werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter) |
| **Vorbedingungen** | Der Benutzer muss im System angemeldet sein. Der Benutzer befindet sich auf der `MieterListView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die `MieterListView` und wählt einen Mieter aus.<br>2. **Systemreaktion**: Das `MieterForm` wird geöffnet und zeigt die Details des ausgewählten Mieters.<br>3. **Benutzeraktion**: Der Benutzer gibt die Mietvertragsdaten ein, wie Mietbeginn, Mietende, Miete, Kaution und Anzahl der Bewohner.<br>4. **Benutzeraktion**: Der Benutzer wählt eine Wohnung aus der Liste verfügbarer Wohnungen.<br>5. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Speichern".<br>6. **Systemreaktion**: Die eingegebenen Daten werden validiert.<br> - Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>7. **Systemreaktion**: Die validierten Daten werden in der Datenbank gespeichert.<br>8. **Systemreaktion**: Die `MieterListView` wird aktualisiert und zeigt den neuen Mietvertrag an. |
| **Nachbedingungen** | Der neue Mietvertrag ist in der Datenbank gespeichert und in der `MieterListView` sichtbar. |


### Use Case 7: Hochladen eines Dokuments

| Abschnitt       | Beschreibung |
|-----------------|--------------|
| **Ziel**        | Ein neues Dokument soll im System hochgeladen und gespeichert werden. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter) |
| **Vorbedingungen** | Der Benutzer muss im System angemeldet sein. Der Benutzer befindet sich auf der `DokumenteListView`. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Dokument hinzufügen".<br>2. **Systemreaktion**: Der `DokumentUploadDialog` wird geöffnet.<br>3. **Benutzeraktion**: Der Benutzer wählt eine Datei von seinem Computer aus und gibt die Dokumentdetails ein, wie Dokumententyp, zugehöriger Mieter und Wohnung.<br>4. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Hochladen".<br>5. **Systemreaktion**: Die eingegebenen Daten werden validiert.<br> - Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>6. **Systemreaktion**: Die validierten Daten und die Datei werden in der Datenbank gespeichert.<br>7. **Systemreaktion**: Die `DokumenteListView` wird aktualisiert und zeigt das neue Dokument an. |
| **Nachbedingungen** | Das neue Dokument ist in der Datenbank gespeichert und in der `DokumenteListView` sichtbar. |


### Use Case 8: Anmeldung und Abmeldung

| Abschnitt       | Beschreibung |
|-----------------|--------------|
| **Ziel**        | Ein Benutzer soll sich im System anmelden und abmelden können. |
| **Akteure**     | Benutzer (Immobilienverwalter oder Vermieter) |
| **Vorbedingungen** | Der Benutzer verfügt über gültige Anmeldeinformationen. |
| **Ablauf**      | 1. **Benutzeraktion**: Der Benutzer öffnet die Anmeldeseite.<br>2. **Benutzeraktion**: Der Benutzer gibt seinen Benutzernamen und sein Passwort ein und klickt auf die Schaltfläche "Anmelden".<br>3. **Systemreaktion**: Das System überprüft die Anmeldeinformationen.<br> - Wenn die Anmeldeinformationen ungültig sind, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.<br>4. **Systemreaktion**: Der Benutzer wird zur `MainView` weitergeleitet.<br>5. **Benutzeraktion**: Der Benutzer klickt auf die Schaltfläche "Abmelden".<br>6. **Systemreaktion**: Der Benutzer wird abgemeldet und zur Anmeldeseite weitergeleitet. |
| **Nachbedingungen** | Der Benutzer ist im System angemeldet oder abgemeldet, abhängig von der durchgeführten Aktion. |