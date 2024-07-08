# Use Cases

## Use Case 1: Hinzufügen eines neuen Mieters

| Kategorie           | Beschreibung                                                                                                                                |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **Ziel**            | Ein neuer Mieter soll in das System aufgenommen und gespeichert werden.                                                                     |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                                               |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                                                |
|                     | Der Benutzer befindet sich auf der `MieterListView`.                                                                                        |
| **Ablauf**          | **1.** Benutzer öffnet die `MieterListView` und klickt auf die Schaltfläche "Mieter hinzufügen".                                            |
|                     | **2.** System öffnet das `MieterForm`.                                                                                                      |
|                     | **3.** Benutzer gibt die Daten des neuen Mieters ein, wie Name, Vorname, Telefonnummer, E-Mail und Einkommen.                               |
|                     | **4.** Benutzer klickt auf die Schaltfläche "Speichern".                                                                                    |
|                     | **5.** System validiert die eingegebenen Daten.                                                                                             |
|                     | **6.** Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.                  |
|                     | **7.** System speichert die validierten Daten in der Datenbank.                                                                             |
|                     | **8.** System aktualisiert die `MieterListView` und zeigt den neuen Mieter an.                                                              |
| **Nachbedingungen** | Der neue Mieter ist in der Datenbank gespeichert und in der `MieterListView` sichtbar.                                                      |

## Use Case 2: Bearbeiten einer Wohnung

| Kategorie           | Beschreibung                                                                                                    |
| ------------------- | ----------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Die Details einer vorhandenen Wohnung sollen aktualisiert werden.                                               |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                   |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                    |
|                     | Der Benutzer befindet sich auf der `WohnungListView`.                                                           |
| **Ablauf**          | **1.** Benutzer öffnet die `WohnungListView` und wählt eine Wohnung aus.                                        |
|                     | **2.** System öffnet die `WohnungDetailsView` und zeigt die Details der ausgewählten Wohnung.                   |
|                     | **3.** Benutzer klickt auf die Schaltfläche "Bearbeiten".                                                       |
|                     | **4.** System öffnet das `WohnungEditDialog`.                                                                   |
|                     | **5.** Benutzer ändert die gewünschten Daten und klickt auf die Schaltfläche "Speichern".                       |
|                     | **6.** System validiert die eingegebenen Daten.                                                                 |
|                     | **7.** Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren. |
|                     | **8.** System speichert die validierten Daten in der Datenbank.                                                 |
|                     | **9.** System aktualisiert die `WohnungDetailsView` und zeigt die aktualisierten Daten an.                      |
| **Nachbedingungen** | Die Änderungen an der Wohnung sind in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar.       |

## Use Case 3: Anzeige von Mieteinnahmen

| Kategorie           | Beschreibung                                                                                 |
| ------------------- |----------------------------------------------------------------------------------------------|
| **Ziel**            | Die Gesamtsumme der Mieteinnahmen soll berechnet und angezeigt werden.                       |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                 |
| **Ablauf**          | **1.** Benutzer öffnet das `Dashboard`.                                                      |
|                     | **2.** `DashboardService` summiert alle Mieteinnahmen aus den Mietverträgen.                 |
|                     | **3.** System zeigt die berechneten Mieteinnahmen in einem Div-Element in der `MainView` an. |
| **Nachbedingungen** | Die Mieteinnahmen werden in der `MainView` korrekt angezeigt.                                |

## Use Case 4: Löschen eines Mietvertrags

| Kategorie           | Beschreibung                                                                                                    |
| ------------------- | ----------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Ein vorhandener Mietvertrag soll zusammen mit dem zugehörigen Mieter gelöscht werden.                           |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                   |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                    |
|                     | Der Benutzer befindet sich auf der `MieterListView`.                                                            |
| **Ablauf**          | **1.** Benutzer öffnet die `MieterListView` und wählt einen Mieter aus.                                         |
|                     | **2.** System öffnet das `MieterForm`.                                                                          |
|                     | **3.** Benutzer klickt auf die Schaltfläche "Löschen".                                                          |
|                     | **4.** System zeigt einen Bestätigungsdialog an.                                                                |
|                     | **5.** Benutzer bestätigt die Löschung.                                                                         |
|                     | **6.** System löscht den Mietvertrag und den zugehörigen Mieter aus der Datenbank.                              |
|                     | **7.** System aktualisiert die `MieterListView` und zeigt die aktualisierte Liste der Mieter an.                |
| **Nachbedingungen** | Der Mietvertrag und der zugehörige Mieter sind aus der Datenbank gelöscht und in der `MieterListView` nicht mehr sichtbar. |

## Use Case 5: Erfassung eines neuen Zählerstands

| Kategorie           | Beschreibung                                                                                                    |
| ------------------- | ----------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Ein neuer Zählerstand für eine Wohnung soll erfasst und gespeichert werden.                                     |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                   |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                    |
|                     | Der Benutzer befindet sich auf der `WohnungDetailsView`.                                                        |
| **Ablauf**          | **1.** Benutzer öffnet die `WohnungDetailsView` und wählt die gewünschte Wohnung aus.                           |
|                     | **2.** Benutzer klickt auf die Schaltfläche "Add Zählerstand".                                                  |
|                     | **3.** System öffnet den `ZaehlerstandDialog`.                                                                  |
|                     | **4.** Benutzer gibt die Zählerstanddaten ein (z.B. Ablesedatum und Ablesewert) und klickt auf "Speichern".     |
|                     | **5.** System validiert die eingegebenen Daten.                                                                 |
|                     | **6.** Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren. |
|                     | **7.** System speichert die validierten Daten in der Datenbank.                                                 |
|                     | **8.** System aktualisiert die `WohnungDetailsView` und zeigt den neuen Zählerstand an.                         |
| **Nachbedingungen** | Der neue Zählerstand ist in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar.                 |

## Use Case 6: Hinzufügen eines neuen Mietvertrags

| Kategorie           | Beschreibung                                                                                                    |
| ------------------- | ----------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Ein neuer Mietvertrag soll im System erstellt und gespeichert werden.                                           |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                   |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                    |
|                     | Der Benutzer befindet sich auf der `MieterListView`.                                                            |
| **Ablauf**          | **1.** Benutzer öffnet die `MieterListView` und wählt einen Mieter aus.                                         |
|                     | **2.** System öffnet das `MieterForm` und zeigt die Details des ausgewählten Mieters.                           |
|                     | **3.** Benutzer gibt die Mietvertragsdaten ein, wie Mietbeginn, Mietende, Miete, Kaution und Anzahl der Bewohner.|
|                     | **4.** Benutzer wählt eine Wohnung aus der Liste verfügbarer Wohnungen.                                         |
|                     | **5.** Benutzer klickt auf die Schaltfläche "Speichern".                                                        |
|                     | **6.** System validiert die eingegebenen Daten.                                                                 |
|                     | **7.** Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren. |
|                     | **8.** System speichert die validierten Daten in der Datenbank.                                                 |
|                     | **9.** System aktualisiert die `MieterListView` und zeigt den neuen Mietvertrag an.                             |
| **Nachbedingungen** | Der neue Mietvertrag ist in der Datenbank gespeichert und in der `MieterListView` sichtbar.                     |

## Use Case 7: Hochladen eines Dokuments

| Kategorie           | Beschreibung                                                                                                                                  |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Ein neues Dokument soll im System hochgeladen und gespeichert werden.                                                                         |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                                                 |
| **Vorbedingungen**  | Der Benutzer muss im System angemeldet sein.                                                                                                  |
|                     | Der Benutzer befindet sich auf der `MieterDetailsView oder WohnungsDetailview`.                                                               |
| **Ablauf**          | **1.** Benutzer klickt auf die Schaltfläche "Dokument hinzufügen".                                                                            |
|                     | **2.** System öffnet den `DokumentUploadDialog`.                                                                                              |
|                     | **3.** Benutzer wählt eine Datei von seinem Computer aus und gibt die Dokumentdetails ein, wie Dokumententyp, zugehöriger Mieter und Wohnung. |
|                     | **4.** Benutzer klickt auf die Schaltfläche "Hochladen".                                                                                      |
|                     | **5.** System validiert die eingegebenen Daten.                                                                                               |
|                     | **6.** Wenn die Validierung fehlschlägt, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren.                    |
|                     | **7.** System speichert die validierten Daten und die Datei in der Datenbank.                                                                 |
|                     | **8.** System aktualisiert die `MieterDetailsView oder WohnungsDetailview` und zeigt das neue Dokument an.                                    |
| **Nachbedingungen** | Das neue Dokument ist in der Datenbank gespeichert und in der `MieterDetailsView oder WohnungsDetailview` sichtbar.                           |

## Use Case 8: Anmeldung und Abmeldung

| Kategorie           | Beschreibung                                                                                                                          |
| ------------------- |---------------------------------------------------------------------------------------------------------------------------------------|
| **Ziel**            | Ein Benutzer soll sich im System anmelden und abmelden können.                                                                        |
| **Akteure**         | Benutzer (Immobilienverwalter oder Vermieter)                                                                                         |
| **Vorbedingungen**  | Der Benutzer verfügt über gültige Anmeldeinformationen.                                                                               |
| **Ablauf**          | **1.** Benutzer öffnet die Anmeldeseite.                                                                                              |
|                     | **2.** Benutzer gibt seinen Benutzernamen und sein Passwort ein und klickt auf die Schaltfläche "Anmelden".                           |
|                     | **3.** System überprüft die Anmeldeinformationen.                                                                                     |
|                     | **4.** Wenn die Anmeldeinformationen ungültig sind, wird eine Fehlermeldung angezeigt und der Benutzer kann die Eingaben korrigieren. |
|                     | **5.** Benutzer wird zur `MainView` weitergeleitet.                                                                                   |
|                     | **6.** Benutzer klickt auf die Schaltfläche "Abmelden".                                                                               |
|                     | **7.** System meldet den Benutzer ab und leitet ihn zur Anmeldeseite weiter.                                                          |
| **Nachbedingungen** | Der Benutzer ist im System abgemeldet.                                                                                |