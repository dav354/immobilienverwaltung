package projektarbeit.immobilienverwaltung.ui.views.mieter;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.DokumentService;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Diese Klasse stellt ein Formular zur Bearbeitung eines Mieters dar.
 * Es ermöglicht die Eingabe, Validierung und Speicherung von Mieter- und Mietvertragsdaten.
 */
@PermitAll
@Route(value = "mieter-details/:id", layout = MainLayout.class)
@SuppressWarnings("SpellCheckingInspection")
public class MieterForm extends FormLayout {

    private final MietvertragService mietvertragService;
    private final MieterService mieterService;
    private final DokumentService dokumentService;
    private List<Wohnung> availableWohnungen;
    private Mieter mieter;
    private final WohnungService wohnungsService;

    //Datenbindung und Validierung zwischen UI-Komponenten und einem Java-Bean-Objekt zu erstellen
    Binder<Mieter> binder = new BeanValidationBinder<>(Mieter.class);
    Binder<Mietvertrag> mietvertragBinder = new BeanValidationBinder<>(Mietvertrag.class);

    // Die einzelnen Daten mit ihren Feldern
    TextField name = new TextField("Name");
    TextField vorname = new TextField("Vorname");
    TextField telefonnummer = new TextField("Telefonnummer");
    TextField email = new TextField("email");
    NumberField einkommen = new NumberField("Einkommen");
    NumberField miete = new NumberField("Miete");
    DatePicker mietbeginn = new DatePicker("Mietbeginn");
    DatePicker mietende = new DatePicker("Mietende");
    NumberField kaution = new NumberField("Kaution");
    IntegerField anzahlBewohner = new IntegerField("Anzahl Bewohner");
    MultiSelectComboBox<Wohnung> wohnungMultiSelectComboBox = new MultiSelectComboBox<>("Wohnung");

    // Grid for Dokumente
    Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);

    // Die 3 Knöpfe zum Speichern, Löschen und Schließen des Forms
    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");

    /**
     * Konstruktor für MieterForm mit den gegebenen Services.
     *
     * @param mieterService       der MieterService zur Verwaltung von Mieter-Entitäten
     * @param mietvertragService  der MietvertragService zur Verwaltung von Mietvertrag-Entitäten
     * @param wohnungsService     der WohnungService zur Verwaltung von Wohnung-Entitäten
     * @param dokumentService     der DokumentService zur Verwaltung von Dokument-Entitäten
     */
    public MieterForm(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungsService, DokumentService dokumentService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungsService = wohnungsService;
        this.dokumentService = dokumentService;
        this.availableWohnungen = wohnungsService.findWohnungenWithoutMietvertrag();
        addClassName("mieter-form");

        binder.bindInstanceFields(this);
        mietvertragBinder.bindInstanceFields(this);

        mietbeginn.setClearButtonVisible(true);
        mietende.setClearButtonVisible(true);

        wohnungMultiSelectComboBox.setItems(availableWohnungen);
        wohnungMultiSelectComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);

        configureDokumentGrid();

        H2 zwischen = new H2("Mietvertrag");
        H1 absatz = new H1("");
        H1 absatz1 = new H1("");
        H1 absatz2= new H1("");


        VerticalLayout gridLayout = new VerticalLayout(new H1("Dokumente"));
        gridLayout.add(dokumentGrid);

        add(name, vorname, telefonnummer, email, einkommen, absatz, zwischen, absatz1, mietbeginn, mietende, kaution, miete, anzahlBewohner, wohnungMultiSelectComboBox,
                absatz2, createButtonsLayout(), gridLayout);
        configureValidation();
    }

    /**
     * Konfiguriert das Grid für die Dokumente.
     * Setzt die sichtbaren Spalten und deren Header.
     */
    private void configureDokumentGrid() {
        dokumentGrid.setColumns("dokumententyp", "dateipfad");
        dokumentGrid.getColumnByKey("dokumententyp").setHeader("Dokumententyp");
        dokumentGrid.getColumnByKey("dateipfad").setHeader("Dateipfad");
    }

    /**
     * Konfiguriert die Validierung für die Formularfelder.
     * Setzt Regex-Validator für Textfelder, Bereichs-Validator für Zahlenfelder
     * und benutzerdefinierte Validator für Datumsfelder.
     */
    private void configureValidation() {
        configureTextField(name, "^[a-zA-Z\\s]{1,50}$", "Name darf nur Buchstaben und Leerzeichen enthalten, 1-50 Zeichen", Mieter::getName, Mieter::setName, binder);
        configureTextField(vorname, "^[a-zA-Z\\s]{1,50}$", "Vorname darf nur Buchstaben und Leerzeichen enthalten, 1-50 Zeichen", Mieter::getVorname, Mieter::setVorname, binder);
        configureTextField(telefonnummer, "^\\d{6,12}$", "Telefonnummer must contain 6-12 numbers, no +,-", Mieter::getTelefonnummer, Mieter::setTelefonnummer, binder);
        configureTextField(email, "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", "Enter valid Email", Mieter::getEmail, Mieter::setEmail, binder);
        configureNumberField(einkommen, "Einkommen must be a positive number", 1, Double.MAX_VALUE, Mieter::getEinkommen, Mieter::setEinkommen, binder);

        // Bind Mietvertrag fields
        configureNumberField(kaution, "Kaution must be a positive number", 1, Double.MAX_VALUE, Mietvertrag::getKaution, Mietvertrag::setKaution, mietvertragBinder);
        configureNumberField(miete, "Miete must be a positive number", 1, Double.MAX_VALUE, Mietvertrag::getMiete, Mietvertrag::setMiete, mietvertragBinder);
        configureIntegerField(anzahlBewohner, "Anzahl Bewohner must be a positive integer", 1, Integer.MAX_VALUE, Mietvertrag::getAnzahlBewohner, Mietvertrag::setAnzahlBewohner, mietvertragBinder);

        mietvertragBinder.forField(mietbeginn).bind(Mietvertrag::getMietbeginn, Mietvertrag::setMietbeginn);

        mietvertragBinder.forField(mietende)
                .withValidator((value, context) -> {
                    if (value == null) {
                        return ValidationResult.ok();
                    }
                    return value.isAfter(mietbeginn.getValue()) ? ValidationResult.ok() : ValidationResult.error("Mietende must be after Mietbeginn");
                })
                .bind(Mietvertrag::getMietende, Mietvertrag::setMietende);
    }

    /**
     * Konfiguriert ein Textfeld mit einem Regex-Validator.
     *
     * @param <T>          Der Typ des Beans.
     * @param field        Das Textfeld, das konfiguriert werden soll.
     * @param regex        Das Regex-Muster zur Validierung.
     * @param errorMessage Die Fehlermeldung bei Validierungsfehlern.
     * @param getter       Die Methode zum Abrufen des Feldwerts.
     * @param setter       Die Methode zum Setzen des Feldwerts.
     * @param binder       Der Binder zur Validierung.
     */
    private <T> void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<T, String> getter, Setter<T, String> setter, Binder<T> binder) {
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Konfiguriert ein Nummernfeld mit einem Bereichs-Validator.
     *
     * @param <T>          Der Typ des Beans.
     * @param field        Das Nummernfeld, das konfiguriert werden soll.
     * @param errorMessage Die Fehlermeldung bei Validierungsfehlern.
     * @param min          Der minimale gültige Wert.
     * @param max          Der maximale gültige Wert.
     * @param getter       Die Methode zum Abrufen des Feldwerts.
     * @param setter       Die Methode zum Setzen des Feldwerts.
     * @param binder       Der Binder zur Validierung.
     */
    @SuppressWarnings("SameParameterValue")
    private <T> void configureNumberField(NumberField field, String errorMessage, double min, double max, ValueProvider<T, Double> getter, Setter<T, Double> setter, Binder<T> binder) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new DoubleRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

    /**
     * Konfiguriert ein Integer-Feld mit einer Fehlermeldung, einem Wertebereich und bindet es an einen Binder.
     *
     * @param field       Das Integer-Feld, das konfiguriert werden soll
     * @param errorMessage Die Fehlermeldung für die Validierung
     * @param min         Der minimale gültige Wert
     * @param max         Der maximale gültige Wert
     * @param getter      Der Getter für den Wert aus der Datenquelle
     * @param setter      Der Setter für den Wert in die Datenquelle
     * @param binder      Der Binder, an den das Feld gebunden werden soll
     * @param <T>         Der Typ der Datenquelle
     */
    @SuppressWarnings("SameParameterValue")
    private <T> void configureIntegerField(IntegerField field, String errorMessage, int min, int max, ValueProvider<T, Integer> getter, Setter<T, Integer> setter, Binder<T> binder) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new IntegerRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

    /**
     * Bindet die übergebene Mieter-Entität an das Formular.
     * Aktualisiert die Formularfelder mit den Daten des Mieters, den zugehörigen Mietvertragsinformationen und die Dokumente
     *
     * @param mieter Die Mieter-Entität, die an das Formular gebunden werden soll
     */
    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
        // Retrieve Mietvertrag entities associated with the Mieter, or create an empty list if Mieter is null
        List<Mietvertrag> mietvertraege = mieter != null ? mietvertragService.findByMieter(mieter.getMieter_id()) : new ArrayList<>();

        // Bind the Mieter entity to the form fields
        binder.readBean(mieter);
        // Bind the first Mietvertrag entity (if exists) or a new Mietvertrag entity to the Mietvertrag fields
        mietvertragBinder.readBean(mietvertraege.isEmpty() ? new Mietvertrag() : mietvertraege.get(0));

        if (mieter != null && !mietvertraege.isEmpty()) {
            // If there are Mietvertraege, get the associated Wohnungen and set them in the MultiSelectComboBox
            Set<Wohnung> wohnungen = mietvertraege.stream().map(Mietvertrag::getWohnung).collect(Collectors.toSet());
            wohnungMultiSelectComboBox.setValue(wohnungen);
        } else {
            // If no Mietvertraege, clear the MultiSelectComboBox
            wohnungMultiSelectComboBox.clear();
        }

        // Show the delete button only if the Mieter is not null and has an ID
        loeschen.setVisible(mieter != null && mieter.getMieter_id() != null);

        // Update the Dokument grid
        List<Dokument> dokumente = mieter != null ? dokumentService.findDokumenteByMieter(mieter) : new ArrayList<>();
        TableUtils.configureGrid(dokumentGrid, dokumente, 40);
    }

    /**
     * Erstellt und konfiguriert ein Layout für die Buttons Speichern, Löschen und Schließen.
     * Jeder Button wird mit einer Theme-Variante versehen und entsprechende Click-Listener hinzugefügt.
     * Zudem werden Tastaturkürzel für die Buttons festgelegt und deren Aktivierung basierend auf dem Zustand der Binder überwacht.
     *
     * @return Ein HorizontalLayout, das die konfigurierten Buttons enthält
     */
    private HorizontalLayout createButtonsLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> validateAndSave());
        loeschen.addClickListener(event -> showDeleteConfirmationDialog());
        schliessen.addClickListener(event -> fireEvent(new CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));
        mietvertragBinder.addStatusChangeListener(e -> speichern.setEnabled(mietvertragBinder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    /**
     * Validiert die Formulareingaben und speichert den Mieter, wenn die Eingaben gültig sind.
     * Prüft auf doppelte Mieter und verwaltet die Zuweisung und Entfernung von Wohnungen.
     * Zeigt entsprechende Benachrichtigungen basierend auf dem Erfolg oder dem Scheitern der Operation an.
     */
    private void validateAndSave() {
        if (mieter != null && binder.isValid() && mietvertragBinder.isValid() && validateMietPeriod()) {
            if (binder.writeBeanIfValid(mieter)) {
                if (isDuplicateMieter(mieter)) {
                    NotificationPopup.showErrorNotification("Ein Mieter mit dem gleichen Namen und Telefonnummer existiert bereits.");
                    return;
                }

                List<Wohnung> selectedWohnungen = new ArrayList<>(wohnungMultiSelectComboBox.getValue());
                List<Mietvertrag> existingMietvertraege = mietvertragService.findByMieter(mieter.getMieter_id());

                List<Mietvertrag> toRemove = existingMietvertraege.stream()
                        .filter(mietvertrag -> !selectedWohnungen.contains(mietvertrag.getWohnung()))
                        .toList();
                for (Mietvertrag mietvertrag : toRemove) {
                    mietvertragService.deleteMietvertrag(mietvertrag);
                }

                for (Wohnung wohnung : selectedWohnungen) {
                    Mietvertrag existingMietvertrag = existingMietvertraege.stream()
                            .filter(mietvertrag -> mietvertrag.getWohnung().equals(wohnung))
                            .findFirst()
                            .orElse(null);

                    if (existingMietvertrag == null) {
                        mietvertragService.createAndSaveMietvertrag(mieter,
                                wohnung,
                                mietbeginn.getValue(),
                                mietende.getValue(), // Mietende kann null sein für unbefristete Mietverträge
                                miete.getValue(),
                                kaution.getValue(),
                                anzahlBewohner.getValue());
                    } else {
                        existingMietvertrag.setMietbeginn(mietbeginn.getValue());
                        existingMietvertrag.setMietende(mietende.getValue()); // Mietende kann null sein für unbefristete Mietverträge
                        existingMietvertrag.setMiete(miete.getValue());
                        existingMietvertrag.setKaution(kaution.getValue());
                        existingMietvertrag.setAnzahlBewohner(anzahlBewohner.getValue());
                        mietvertragService.saveMietvertrag(existingMietvertrag);
                    }
                }

                refreshAvailableWohnungen();
                fireEvent(new SaveEvent(this, mieter));
                NotificationPopup.showSuccessNotification("Mieter erfolgreich gespeichert.");
            } else {
                NotificationPopup.showErrorNotification("Fehler beim Speichern des Mieters.");
            }
        }
    }

    /**
     * Validiert, dass der Mietbeginn vor dem Mietende liegt.
     *
     * @return true, wenn die Mietperiode gültig ist, sonst false
     */
    private boolean validateMietPeriod() {
        // TODO: Error message if save without Mietbeginn but with mietende

        if (mietbeginn.getValue() != null && mietende.getValue() != null) {
            if (mietende.getValue().isBefore(mietbeginn.getValue())) {
                NotificationPopup.showErrorNotification("Mietende muss nach Mietbeginn liegen.");
                return false;
            }
        }
        return true;
    }

    /**
     * Überprüft, ob ein Mieter mit dem gleichen Namen und Telefonnummer bereits im System existiert.
     *
     * @param mieter Der Mieter, der auf Duplikate geprüft werden soll
     * @return true, wenn ein duplizierter Mieter existiert, sonst false
     */
    private boolean isDuplicateMieter(Mieter mieter) {
        List<Mieter> existingMieters = mieterService.findAllMieter();
        return existingMieters.stream().anyMatch(existingMieter ->
                existingMieter.getName().equalsIgnoreCase(mieter.getName()) &&
                        existingMieter.getVorname().equalsIgnoreCase(mieter.getVorname()) &&
                        existingMieter.getTelefonnummer().equals(mieter.getTelefonnummer()) &&
                        !existingMieter.getMieter_id().equals(mieter.getMieter_id())
        );
    }

    /**
     * Aktualisiert die Liste der verfügbaren Wohnungen und aktualisiert die Elemente im MultiSelectComboBox.
     * Wenn keine Wohnungen verfügbar sind, wird eine Platzhaltermeldung im ComboBox gesetzt.
     */
    private void refreshAvailableWohnungen() {
        availableWohnungen = wohnungsService.findAvailableWohnungen();
        if (availableWohnungen.isEmpty()) {
            wohnungMultiSelectComboBox.setPlaceholder("Keine Wohnungen verfügbar");
        } else {
            wohnungMultiSelectComboBox.setItems(availableWohnungen);
        }
    }

    /**
     * Fügt einen Listener für das angegebene Ereignis hinzu (Speichern, Löschen und Schließen).
     *
     * @param <T>        Der Typ des Ereignisses, das abgehört werden soll
     * @param eventType  Die Klasse des Ereignisses, auf das gehört werden soll
     * @param listener   Der Listener, der auf das Ereignis reagiert
     * @return Eine Registration, die verwendet werden kann, um den Listener zu entfernen
     */
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Ereignis, das ausgelöst wird, wenn ein Mieter bearbeitet wird.
     */
    public static abstract class MieterFormEvent extends ComponentEvent<MieterForm> {
        private final Mieter mieter;

        /**
         * Konstruktor für das MieterFormEvent.
         *
         * @param source Die Quelle des Ereignisses (MieterForm, die das Ereignis auslöst)
         * @param mieter Der Mieter, auf den sich das Ereignis bezieht
         */
        protected MieterFormEvent(MieterForm source, Mieter mieter) {
            super(source, false);
            this.mieter = mieter;
        }

        /**
         * Gibt den Mieter zurück, auf den sich das Ereignis bezieht.
         *
         * @return Der Mieter, auf den sich das Ereignis bezieht
         */
        public Mieter getContact() {
            return mieter;
        }
    }

    /**
     * Zeigt einen Bestätigungsdialog zur Löschung eines Mieters an.
     * Bei Bestätigung wird ein DeleteEvent ausgelöst und eine Erfolgsmeldung angezeigt.
     */
    private void showDeleteConfirmationDialog() {
        // Create a new ConfirmationDialog with a message and a confirmation action
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Are you sure you want to delete this Mieter?\nThe Mietvertrag will also be deleted.",
                () -> {
                    // If confirmed, fire a DeleteEvent and show a success notification
                    fireEvent(new DeleteEvent(this, mieter));
                    NotificationPopup.showSuccessNotification("Mieter erfolgreich gelöscht.");
                }
        );
        // Open the confirmation dialog
        confirmationDialog.open();
    }

    /**
     * Ereignis, das ausgelöst wird, wenn ein Mieter gespeichert wird.
     */
    public static class SaveEvent extends MieterFormEvent {
        /**
         * Konstruktor für das SaveEvent.
         *
         * @param source Die Quelle des Ereignisses (MieterForm, die das Ereignis auslöst)
         * @param mieter Der Mieter, der gespeichert wurde
         */
        SaveEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    /**
     * Ereignis, das ausgelöst wird, wenn ein Mieter gelöscht wird.
     */
    public static class DeleteEvent extends MieterFormEvent {
        /**
         * Konstruktor für das DeleteEvent.
         *
         * @param source Die Quelle des Ereignisses (MieterForm, die das Ereignis auslöst)
         * @param mieter Der Mieter, der gelöscht wird
         */
        DeleteEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    /**
     * Ereignis, das ausgelöst wird, wenn das Schließen der MieterForm ausgelöst wird.
     */
    public static class CloseEvent extends MieterFormEvent {
        /**
         * Konstruktor für das CloseEvent.
         *
         * @param source Die Quelle des Ereignisses (MieterForm, die das Ereignis auslöst)
         */
        CloseEvent(MieterForm source) {
            super(source, null);
        }
    }

}