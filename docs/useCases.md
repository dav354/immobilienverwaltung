# Use Cases

## Use Case 1: Anmeldung und Abmeldung {#tbl:usecase12 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein Benutzer soll sich im System anmelden und abmelden können. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer verfügt über gültige Anmeldeinformationen. \\
\hline
Ablauf & 1. Benutzer öffnet die Anmeldeseite. \newline 2. Benutzer gibt seinen Benutzernamen und sein Passwort ein und
klickt auf die Schaltfläche "Anmelden". \newline 3. System überprüft die Anmeldeinformationen. \newline 4. Bei
ungültigen Anmeldeinformationen: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren. \newline 5. Benutzer wird
zur `MainView` weitergeleitet. \newline 6. Benutzer klickt im Burger Menü auf die Schaltfläche "Abmelden". \newline 7. System meldet
den Benutzer ab und leitet ihn zur Anmeldeseite weiter. \\
\hline
Nachbedingungen & Der Benutzer ist im System abgemeldet. \\
\end{longtable}

## Use Case 2: User erstellen {#tbl:usecase13 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein Benutzer soll erstellt werden können. \\
\hline
Akteure & Admin \\
\hline
Vorbedingungen & - Der Benutzer muss die Rolle eines Admins haben und angemeldet sein. \\
\hline
Ablauf & 1. Benutzer öffnet im Burger-Menü die AdminView. \newline 2. Benutzer gibt einen validem Benutzernamen und ein valides Passwort ein, wählt die gewünschte Rolle aus und
klickt auf die Schaltfläche "add User". \newline 3. System überprüft die eingegebenen informationen. \newline 4. Bei
ungültigen Informationen: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren. \newline 
\hline
Nachbedingungen & Der Benutzer ist erstellt. \\
\end{longtable}

## Use Case 3: User löschen {#tbl:usecase14 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein Benutzer soll gelöscht werden. \\
\hline
Akteure & Admin \\
\hline
Vorbedingungen & - Der Benutzer muss die Rolle eines Admins haben und angemeldet sein. \\
\hline
Ablauf & 1. Benutzer öffnet im Burger-Menü die AdminView. \newline 2. Benutzer klickt auf den Button Delete des zu löschenden Users.
\newline 3. Es öffnet sich ein Dialogfenster bei dem das Löschen bestätigt werden muss
\newline 4. Soll der letzte Admin gelöscht werden, kommt eine Fehlermeldung, dass dies nicht möglich ist
Admin . \newline
\hline
Nachbedingungen & Der Benutzer ist aus dem System gelöscht. \\
\end{longtable}

## Use Case 4: Hinzufügen eines neuen Mieters {#tbl:usecase1 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein neuer Mieter soll in das System aufgenommen und gespeichert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `MieterListView` und klickt auf die Schaltfläche "Mieter hinzufügen". \newline 2. System
öffnet aus der `MieterDetailsView` den `MieterEditDialog`. \newline 3. Benutzer gibt die Daten des neuen Mieters ein, wie Name, Vorname, Telefonnummer,
E-Mail und Einkommen. \newline 4. Benutzer klickt auf die Schaltfläche "Speichern". \newline 5. System validiert die
eingegebenen Daten. \newline 6. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren.
\newline 7. System speichert die validierten Daten in der Datenbank. \newline 8. System aktualisiert
die `MieterListView` und zeigt den neuen Mieter an. \\
\hline
Nachbedingungen & Der neue Mieter ist in der Datenbank gespeichert und in der `MieterListView` sichtbar. \\
\end{longtable}

## Use Case 5: Bearbeiten eines Mieters {#tbl:usecase2 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein neuer Mieter soll aus dem System gelöscht werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `MieterListView` und wählt einen Mieter aus. \newline 2. System öffnet
die `MieterDetailsView` und zeigt die Details des ausgewählten Mieters. \newline 3. Benutzer klickt auf die
Schaltfläche "Edit". \newline 4. System öffnet den `MieterEditDialog`. \newline 5. Benutzer ändert die
gewünschten Daten und klickt auf die Schaltfläche "Speichern". \newline 6. System validiert die eingegebenen Daten.
\newline 7. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren. \newline 8. System
speichert die validierten Daten in der Datenbank. \newline 9. System aktualisiert die `MieterDetailsView` und zeigt die
aktualisierten Daten an. \\
\hline
Nachbedingungen & Der neue Mieter ist in der Datenbank aktualisiert und in der `MieterListView` sichtbar. \\
\end{longtable}

## Use Case 6: Löschen eines Mieter {#tbl:usecase3 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein vorhandener Mieter soll gelöscht werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `MieterListView` und wählt einen Mieter aus. \newline 2. System öffnet die `MieterDetailView`.
\newline 3. Benutzer klickt auf die Schaltfläche "Löschen". \newline 4. System zeigt einen Bestätigungsdialog an.
\newline 5. Benutzer bestätigt die Löschung. \newline 6. System löscht den Mieter und die zugehörigen Mieterverträge und Dokumente aus
der Datenbank. \newline 7. System aktualisiert die `MieterListView` und zeigt die aktualisierte Liste der Mieter an. \\
\hline
Nachbedingungen & Der Mieter und die zugehörige Mietverträge sind aus der Datenbank gelöscht und in der `MieterListView`
nicht mehr sichtbar. \\
\end{longtable}

## Use Case 7: Hinzufügen einer neuen Wohnung {#tbl:usecase4 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Eine neue Wohnung soll in das System aufgenommen und gespeichert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `WohnungsListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `WohnungsListView` und klickt auf die Schaltfläche "Mieter hinzufügen". \newline 2. System
öffnet aus der `WohnungDetailsView` den `WohnungEditDialog`. \newline 3. Benutzer gibt die Daten der neuen Wohnung ein, wie PLT, Stadt,
Adresse und Baujahr. \newline 4. Benutzer klickt auf die Schaltfläche "Speichern". \newline 5. System validiert die
eingegebenen Daten. \newline 6. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren.
\newline 7. System speichert die validierten Daten in der Datenbank. \newline 8. System aktualisiert
die `WohnungsListView` und zeigt die neue Wohnung an. \\
\hline
Nachbedingungen & Die neue Wohnung ist in der Datenbank gespeichert und in der `WohnungListView` sichtbar. \\
\end{longtable}

## Use Case 8: Bearbeiten einer Wohnung {#tbl:usecase5 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Die Details einer vorhandenen Wohnung sollen aktualisiert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `WohnungListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `WohnungListView` und wählt eine Wohnung aus. \newline 2. System öffnet
die `WohnungDetailsView` und zeigt die Details der ausgewählten Wohnung. \newline 3. Benutzer klickt auf die
Schaltfläche "Edit". \newline 4. System öffnet das `WohnungEditDialog`. \newline 5. Benutzer ändert die
gewünschten Daten und klickt auf die Schaltfläche "Speichern". \newline 6. System validiert die eingegebenen Daten.
\newline 7. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren. \newline 8. System
speichert die validierten Daten in der Datenbank. \newline 9. System aktualisiert die `WohnungDetailsView` und zeigt die
aktualisierten Daten an. \\
\hline
Nachbedingungen & Die Änderungen an der Wohnung sind in der Datenbank gespeichert und in der `WohnungDetailsView`
sichtbar. \\
\end{longtable}

## Use Case 9: Löschen einer Wohnung {#tbl:usecase6 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Eine vorhandener Wohnung soll gelöscht werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `WohnungListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `WohnungListView` und wählt eine Wohnung aus. \newline 2. System öffnet die `WohnungDetailView`.
\newline 3. Benutzer klickt auf die Schaltfläche "Löschen". \newline 4. System zeigt einen Bestätigungsdialog an.
\newline 5. Benutzer bestätigt die Löschung. \newline 6. System löscht die Wohnung und die zugehörigen Zählerstände und Dokumente aus
der Datenbank. \newline 7. System aktualisiert die `WohnungListView` und zeigt die aktualisierte Liste der Wohnungen an. \\
\hline
Nachbedingungen & Die Wohnung, die zugehörigen Zählerstände und Mietverträge sind aus der Datenbank gelöscht und in der `WohnungListView`
nicht mehr sichtbar. \\
\end{longtable}

## Use Case 10: Hinzufügen eines neuen Mietvertrags {#tbl:usecase7 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein neuer Mietvertrag soll im System erstellt und gespeichert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `MieterListView` und wählt einen Mieter aus. \newline 2. System öffnet das `MieterForm`
und zeigt die Details des ausgewählten Mieters. \newline 3. Benutzer gibt die Mietvertragsdaten ein, wie Mietbeginn,
Mietende, Miete, Kaution und Anzahl der Bewohner. \newline 4. Benutzer wählt eine Wohnung aus der Liste verfügbarer
Wohnungen. \newline 5. Benutzer klickt auf die Schaltfläche "Speichern". \newline 6. System validiert die eingegebenen
Daten. \newline 7. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren. \newline 8.
System speichert die validierten Daten in der Datenbank. \newline 9. System aktualisiert die `MieterListView` und zeigt
den neuen Mietvertrag an. \\
\hline
Nachbedingungen & Der neue Mietvertrag ist in der Datenbank gespeichert und in der `MieterListView` sichtbar. \\
\end{longtable}

## Use Case 11: Löschen eines Mietvertrags {#tbl:usecase8 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein vorhandener Mietvertrag soll zusammen mit dem zugehörigen Mieter gelöscht werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterListView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `MieterListView` und wählt einen Mieter aus. \newline 2. System öffnet das `MieterForm`.
\newline 3. Benutzer klickt auf die Schaltfläche "Löschen". \newline 4. System zeigt einen Bestätigungsdialog an.
\newline 5. Benutzer bestätigt die Löschung. \newline 6. System löscht den Mietvertrag und den zugehörigen Mieter aus
der Datenbank. \newline 7. System aktualisiert die `MieterListView` und zeigt die aktualisierte Liste der Mieter an. \\
\hline
Nachbedingungen & Der Mietvertrag und der zugehörige Mieter sind aus der Datenbank gelöscht und in der `MieterListView`
nicht mehr sichtbar. \\
\end{longtable}

## Use Case 12: Erfassung eines neuen Zählerstands {#tbl:usecase9 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein neuer Zählerstand für eine Wohnung soll erfasst und gespeichert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `WohnungDetailsView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `WohnungDetailsView` und wählt die gewünschte Wohnung aus. \newline 2. Benutzer klickt
auf die Schaltfläche "Add Zählerstand". \newline 3. System öffnet den `ZaehlerstandDialog`. \newline 4. Benutzer gibt
die Zählerstanddaten ein (z.B. Ablesedatum und Ablesewert) und klickt auf "Speichern". \newline 5. System validiert die
eingegebenen Daten. \newline 6. Bei Validierungsfehler: Fehlermeldung anzeigen, Benutzer kann Eingaben korrigieren.
\newline 7. System speichert die validierten Daten in der Datenbank. \newline 8. System aktualisiert
die `WohnungDetailsView` und zeigt den neuen Zählerstand an. \\
\hline
Nachbedingungen & Der neue Zählerstand ist in der Datenbank gespeichert und in der `WohnungDetailsView` sichtbar. \\
\end{longtable}

## Use Case 13: Löschen eines Zählerstands {#tbl:usecase9 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein bestehender Zählerstand für eine Wohnung soll gelöscht werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `WohnungDetailsView`. \\
\hline
Ablauf & 1. Benutzer öffnet die `WohnungDetailsView` und wählt die gewünschte Wohnung aus. \newline 2. Benutzer klickt
auf die Schaltfläche mit dem roten Kreuz. \newline 3. System öffnet ein Dialogfenster. \newline 4. Benutzer bestätigt das löschen 
mit dem Button "Löschen"".\\
\hline
Nachbedingungen & Der bestehende Zählerstand ist in der Datenbank gelöscht und nicht mehr in der `WohnungDetailsView` sichtbar. \\
\end{longtable}

## Use Case 14: Hochladen eines Dokuments {#tbl:usecase10 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein neues Dokument soll im System hochgeladen und gespeichert werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterDetailsView` oder `WohnungsDetailview`. \\
\hline
Ablauf & 1. Benutzer klickt auf die Schaltfläche "Dokument hinzufügen". \newline 2. System öffnet
den `DokumentUploadDialog`. \newline 3. Benutzer wählt eine Datei von seinem Computer aus und gibt die Dokumentdetails
ein, wie Dokumententyp, zugehöriger Mieter und Wohnung. \newline 4. Benutzer klickt auf die Schaltfläche "Hochladen".
\newline 5. System validiert die eingegebenen Daten. \newline 6. Bei Validierungsfehler: Fehlermeldung anzeigen,
Benutzer kann Eingaben korrigieren. \newline 7. System speichert die validierten Daten und die Datei in der Datenbank.
\newline 8. System aktualisiert die `MieterDetailsView` oder `WohnungsDetailview` und zeigt das neue Dokument an. \\
\hline
Nachbedingungen & Das neue Dokument ist in der Datenbank gespeichert und in der `MieterDetailsView`
oder `WohnungsDetailview` sichtbar. \\
\end{longtable}

## Use Case 15: Anzeigen eines Dokuments {#tbl:usecase10 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein erstelltes Dokument soll angezeigt werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterDetailsView` oder `WohnungsDetailview`. \\
\hline
Ablauf & 1. Benutzer klickt auf die Schaltfläche mit dem Auge. 
\newline 2. System zeigt Bilder auf der selben Seite an. PDF Dokumente werden in seperatem Tab angezeigt
\\
\hline
Nachbedingungen & Das Dokument wird angezeigt
\\
\end{longtable}

## Use Case 16: Download eines Dokuments {#tbl:usecase10 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein erstelltes Dokument soll angezeigt werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterDetailsView` oder `WohnungsDetailview`. \\
\hline
Ablauf & 1. Benutzer klickt auf die Schaltfläche zum downloaden.
\newline 2. Das System speichert die Datei und das Bild lokal. PDF Dokumente werden in seperatem Tab angezeigt
\\
\hline
Nachbedingungen & Das Dokument wird angezeig und ist lokal gespeichert
\\
\end{longtable}

## Use Case 17: Löschen eines Dokuments {#tbl:usecase10 .longtable}

\renewcommand{\arraystretch}{1.5}
\begin{longtable}{|p{3cm}|p{12cm}|}
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endfirsthead
\hline
\rowcolor{gray!25}
\textbf{Kategorie} & \textbf{Beschreibung} \\
\hline
\endhead
\hline
\multicolumn{2}{r}{\textit{Fortsetzung auf der nächsten Seite}} \\
\endfoot
\hline
\endlastfoot
Ziel & Ein erstelltes Dokument soll angezeigt werden. \\
\hline
Akteure & Benutzer (Immobilienverwalter oder Vermieter) \\
\hline
Vorbedingungen & - Der Benutzer muss im System angemeldet sein. \newline - Der Benutzer befindet sich auf
der `MieterDetailsView` oder `WohnungsDetailview`. \\
\hline
Ablauf & 1. Benutzer klickt auf die Schaltfläche mit dem roten kreuz.
\newline 2. Es öffnet sich ein Dialogfeld, in dem das Löschen bestätigt werden muss
\\
\hline
Nachbedingungen & Das Dokument wurde gelöscht
\\
\end{longtable}