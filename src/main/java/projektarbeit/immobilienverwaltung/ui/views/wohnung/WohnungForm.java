package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Land;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.ui.components.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@PermitAll
public class WohnungForm extends FormLayout {

    Binder<Wohnung> binder = new BeanValidationBinder<>(Wohnung.class);
    ComboBox<Land> land = new ComboBox<>("Land");
    TextField postleitzahl = new TextField("Postleitzahl");
    TextField stadt = new TextField("Stadt");
    TextField strasse = new TextField("Strasse");
    TextField hausnummer = new TextField("Hausnummer");
    IntegerField gesamtQuadratmeter = new IntegerField("Gesamt Quadratmeter");
    IntegerField baujahr = new IntegerField("Baujahr");
    IntegerField anzahlBaeder = new IntegerField("Anzahl Baeder");
    IntegerField anzahlSchlafzimmer = new IntegerField("Anzahl Schlafzimmer");
    TextField stockwerk = new TextField("Stockwerk");
    TextField wohnungsnummer = new TextField("Wohnungsnummer");
    Checkbox hatBalkon = new Checkbox("Hat Balkon");
    Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
    Checkbox hatGarten = new Checkbox("Hat Garten");
    Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");
    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");
    Span mieterSpan = new Span();

    private Wohnung wohnung;
    private final MietvertragService mietvertragService;

    /**
     * Konstruktor für die WohnungForm.
     *
     * @param mieters eine Liste von Mietern
     * @param mietvertragService der MietvertragService für Mietverträge
     */
    public WohnungForm(List<Mieter> mieters, MietvertragService mietvertragService) {
        this.mietvertragService = mietvertragService;
        addClassName("wohnung-form");
        binder.bindInstanceFields(this);

        land.setItems(Land.values()); // Befüllt ComboBox mit Enum-Werten
        land.setItemLabelGenerator(Land::getName);

        add(land, postleitzahl, stadt, strasse, hausnummer, stockwerk, wohnungsnummer, gesamtQuadratmeter, baujahr, anzahlBaeder, anzahlSchlafzimmer, mieterSpan, hatBalkon, hatTerrasse, hatGarten, hatKlimaanlage, createButtonsLayout());
        configureValidation();
    }

    /**
     * Konfiguriert die Validierung der Felder im Formular.
     * Setzt Regex-Validatoren für Textfelder, Bereichsvalidatoren für Integer-Felder
     * und einen benutzerdefinierten Validator für das Land-Feld.
     */
    private void configureValidation() {
        // Erforderliche Textfelder mit Regex-Validierung
        configureTextField(postleitzahl, "^\\d{4,10}$", "Postleitzahl muss 4 bis 10 Ziffern lang sein", "Postleitzahl ist erforderlich", Wohnung::getPostleitzahl, Wohnung::setPostleitzahl);
        configureTextField(stadt, "^[\\p{L}\\s]+$", "Stadt darf nur Buchstaben enthalten", "Stadt ist erforderlich", Wohnung::getStadt, Wohnung::setStadt);
        configureTextField(strasse, "^[\\p{L}\\s]+$", "Strasse darf nur Buchstaben enthalten", "Strasse ist erforderlich", Wohnung::getStrasse, Wohnung::setStrasse);
        configureTextField(hausnummer, "^\\d+[a-zA-Z]?$", "Hausnummer muss numerisch sein mit optionalem Buchstaben", "Hausnummer ist erforderlich", Wohnung::getHausnummer, Wohnung::setHausnummer);

        // Optionale Textfelder mit Regex-Validierung
        configureTextField(stockwerk, "^[0-9]*$", "Stockwerk darf nur Zahlen enthalten", Wohnung::getStockwerk, Wohnung::setStockwerk);
        configureTextField(wohnungsnummer, "^[a-zA-Z0-9]*$", "Wohnungsnummer darf nur Buchstaben und Zahlen enthalten", Wohnung::getWohnungsnummer, Wohnung::setWohnungsnummer);

        // Erforderliche Integer-Felder mit Bereichsvalidierung
        configureIntegerField(gesamtQuadratmeter, "Gesamt Quadratmeter muss positiv sein", 1, Integer.MAX_VALUE, Wohnung::getGesamtQuadratmeter, Wohnung::setGesamtQuadratmeter);
        configureIntegerField(baujahr, "Baujahr muss ein gültiges Jahr zwischen 1000 und " + LocalDate.now().getYear() + "sein", 1000, LocalDate.now().getYear(), Wohnung::getBaujahr, Wohnung::setBaujahr);
        configureIntegerField(anzahlBaeder, "Anzahl Baeder muss positiv sein", 1, Integer.MAX_VALUE, Wohnung::getAnzahlBaeder, Wohnung::setAnzahlBaeder);
        configureIntegerField(anzahlSchlafzimmer, "Anzahl Schlafzimmer muss null oder positiv sein", 0, Integer.MAX_VALUE, Wohnung::getAnzahlSchlafzimmer, Wohnung::setAnzahlSchlafzimmer);

        // Erforderliches Land-Feld
        binder.forField(land).asRequired("Land ist erforderlich").bind(Wohnung::getLand, Wohnung::setLand);

        Mietvertrag mietvertrag = mietvertragService.findByWohnung(wohnung);
        if (mietvertrag != null && mietvertrag.getMieter() != null) {
            mieterSpan.setText(mietvertrag.getMieter().getFullName());
        } else {
            mieterSpan.setText("Kein Mieter");
        }

        // Aktiviert die Speichern-Schaltfläche nur, wenn der Binder gültig ist
        binder.addStatusChangeListener(event -> speichern.setEnabled(binder.isValid()));
    }

    /**
     * Konfiguriert ein erforderliches Textfeld mit einem Regex-Validator.
     *
     * @param field           Das zu konfigurierende Textfeld.
     * @param regex           Das Regex-Muster zur Validierung.
     * @param errorMessage    Die Fehlermeldung, die bei fehlerhafter Validierung angezeigt wird.
     * @param requiredMessage Die Fehlermeldung, die angezeigt wird, wenn das Feld leer ist.
     * @param getter          Die Getter-Methode für den Feldwert.
     * @param setter          Die Setter-Methode für den Feldwert.
     */
    private void configureTextField(TextField field, String regex, String errorMessage, String requiredMessage, ValueProvider<Wohnung, String> getter, Setter<Wohnung, String> setter) {
        binder.forField(field)
                .asRequired(requiredMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Konfiguriert ein optionales Textfeld mit einem Regex-Validator.
     *
     * @param field        Das zu konfigurierende Textfeld.
     * @param regex        Das Regex-Muster zur Validierung.
     * @param errorMessage Die Fehlermeldung, die bei fehlerhafter Validierung angezeigt wird.
     * @param getter       Die Getter-Methode für den Feldwert.
     * @param setter       Die Setter-Methode für den Feldwert.
     */
    private void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<Wohnung, String> getter, Setter<Wohnung, String> setter) {
        binder.forField(field)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Konfiguriert ein erforderliches Integer-Feld mit einem Bereichsvalidator.
     *
     * @param field        Das zu konfigurierende Integer-Feld.
     * @param errorMessage Die Fehlermeldung, die bei fehlerhafter Validierung angezeigt wird.
     * @param min          Der minimale gültige Wert.
     * @param max          Der maximale gültige Wert.
     * @param getter       Die Getter-Methode für den Feldwert.
     * @param setter       Die Setter-Methode für den Feldwert.
     */
    private void configureIntegerField(IntegerField field, String errorMessage, int min, int max, ValueProvider<Wohnung, Integer> getter, Setter<Wohnung, Integer> setter) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new IntegerRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

    /**
     * Setzt die Wohnung (Wohnung) im Formular und befüllt die Felder mit ihren Daten.
     * Leert die Felder, wenn die Wohnung null ist.
     *
     * @param wohnung Die zu setzende Wohnung.
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
        binder.setBean(wohnung);

        if (wohnung != null) {
            // Setzt Werte oder leert Felder, wenn wohnung null ist
            land.setValue(wohnung.getLand() != null ? wohnung.getLand() : null);
            postleitzahl.setValue(wohnung.getPostleitzahl() != null ? wohnung.getPostleitzahl() : "");
            stadt.setValue(wohnung.getStadt() != null ? wohnung.getStadt() : "");
            strasse.setValue(wohnung.getStrasse() != null ? wohnung.getStrasse() : "");
            hausnummer.setValue(wohnung.getHausnummer() != null ? wohnung.getHausnummer() : "");
            gesamtQuadratmeter.setValue(wohnung.getGesamtQuadratmeter() != 0 ? wohnung.getGesamtQuadratmeter() : null);
            baujahr.setValue(wohnung.getBaujahr() != 0 ? wohnung.getBaujahr() : null);
            anzahlBaeder.setValue(wohnung.getAnzahlBaeder() != 0 ? wohnung.getAnzahlBaeder() : null);
            anzahlSchlafzimmer.setValue(wohnung.getAnzahlSchlafzimmer() != 0 ? wohnung.getAnzahlSchlafzimmer() : null);
            stockwerk.setValue(wohnung.getStockwerk() != null ? wohnung.getStockwerk() : "");
            wohnungsnummer.setValue(wohnung.getWohnungsnummer() != null ? wohnung.getWohnungsnummer() : "");

            // Zeigt Löschen-Schaltfläche an, wenn wohnung existiert
            loeschen.setVisible(wohnung.getWohnung_id() != null);
        } else {
            clearFields();
        }

        binder.readBean(wohnung);
    }

    /**
     * Leert alle Felder des Formulars.
     */
    void clearFields() {
        land.clear();
        postleitzahl.clear();
        stadt.clear();
        strasse.clear();
        hausnummer.clear();
        gesamtQuadratmeter.clear();
        baujahr.clear();
        anzahlBaeder.clear();
        anzahlSchlafzimmer.clear();
        hatBalkon.setValue(false);
        hatTerrasse.setValue(false);
        hatGarten.setValue(false);
        hatKlimaanlage.setValue(false);
        stockwerk.clear();
        wohnungsnummer.clear();
        mieterSpan.setText("");
    }

    /**
     * Erstellt das Layout der Schaltflächen für das Formular.
     *
     * @return das Layout der Schaltflächen
     */
    private HorizontalLayout createButtonsLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> validateAndSave());
        loeschen.addClickListener(event -> showDeleteConfirmationDialog());
        schliessen.addClickListener(event -> fireEvent(new WohnungForm.CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    /**
     * Validiert die Formulareingaben und speichert die Wohnung, wenn die Eingaben gültig sind.
     * Stellt sicher, dass der Mieter aus der ComboBox gesetzt wird und löst ein SaveEvent aus, wenn erfolgreich.
     * Zeigt entsprechende Benachrichtigungen basierend auf dem Erfolg oder Misserfolg der Operation an.
     */
    private void validateAndSave() {
        if (wohnung != null) {
            // Validierung manuell auslösen
            if (binder.writeBeanIfValid(wohnung)) {
                // Speichern-Ereignis mit der validierten Wohnung auslösen
                fireEvent(new SaveEvent(this, wohnung));

                // Erfolgsbenachrichtigung anzeigen
                NotificationPopup.showSuccessNotification("Wohnung erfolgreich gespeichert.");
            } else {
                // Fehlermeldung anzeigen, wenn die Validierung fehlschlägt
                NotificationPopup.showErrorNotification("Fehler beim Speichern der Wohnung.");
            }
        }
    }

    /**
     * Zeigt einen Bestätigungsdialog zur Bestätigung der Löschung der Wohnung an.
     * Bei Bestätigung wird ein DeleteEvent ausgelöst und eine Erfolgsbenachrichtigung angezeigt.
     */
    private void showDeleteConfirmationDialog() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Möchten Sie diese Wohnung wirklich löschen?",
                () -> {
                    // Löschen-Ereignis bei Bestätigung auslösen
                    fireEvent(new DeleteEvent(this, wohnung));
                    // Erfolgsbenachrichtigung anzeigen
                    NotificationPopup.showSuccessNotification("Wohnung erfolgreich gelöscht.");
                }
        );
        // Bestätigungsdialog öffnen
        confirmationDialog.open();
    }

    /**
     * Fügt einen Listener für das angegebene Ereignis hinzu.
     *
     * @param eventType der Typ des Ereignisses
     * @param listener der Listener für das Ereignis
     * @param <T> der Typ des Ereignisses
     * @return eine Registrierung des Ereignis-Listeners
     */
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class WohnungFormEvent extends ComponentEvent<WohnungForm> {
        private final Wohnung wohnung;

        protected WohnungFormEvent(WohnungForm source, Wohnung wohnung) {
            super(source, false);
            this.wohnung = wohnung;
        }

        public Wohnung getWohnung() {
            return wohnung;
        }
    }

    public static class SaveEvent extends WohnungForm.WohnungFormEvent {
        SaveEvent(WohnungForm source, Wohnung wohnung) {
            super(source, wohnung);
        }
    }

    public static class DeleteEvent extends WohnungForm.WohnungFormEvent {
        DeleteEvent(WohnungForm source, Wohnung wohnung) {
            super(source, wohnung);
        }
    }

    public static class CloseEvent extends WohnungForm.WohnungFormEvent {
        CloseEvent(WohnungForm source) {
            super(source, null);
        }
    }
}
