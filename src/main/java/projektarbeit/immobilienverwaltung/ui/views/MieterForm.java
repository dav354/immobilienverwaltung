package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projektarbeit.immobilienverwaltung.demo.AssignMieterToWohnungDemo;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.ui.components.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MieterForm extends FormLayout {

    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);
    private final MieterService mieterService;
    //Datenbindung und Validierung zwischen UI-Komponenten und einem Java-Bean-Objekt zu erstellen
    Binder<Mieter> binder = new BeanValidationBinder<>(Mieter.class);
    // Die einzelnen Daten mit ihren Feldern
    TextField name = new TextField("Name");
    TextField vorname = new TextField("Vorname");
    TextField telefonnummer = new TextField("Telefonnummer");
    NumberField einkommen = new NumberField("Einkommen"); // Hier NumberField verwenden
    DatePicker mietbeginn = new DatePicker("Mietbeginn");
    DatePicker mietende = new DatePicker("Mietende");
    NumberField kaution = new NumberField("Kaution");
    IntegerField anzahlBewohner = new IntegerField("Anzahl Bewohner");
    MultiSelectComboBox<Wohnung> wohnungMultiSelectComboBox = new MultiSelectComboBox<>("Wohnung");
    // Die 3 Knöpfe zum Speichern, Löschen und Schließen des Forms
    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");
    private List<Wohnung> availableWohnungen;
    private Mieter mieter;

    //Erstellung des Forms
    public MieterForm(MieterService mieterService) {
        this.mieterService = mieterService;
        this.availableWohnungen = mieterService.findAvailableWohnungen();
        addClassName("mieter-form");

        binder.bindInstanceFields(this);

        // Enable clear button for date pickers
        mietbeginn.setClearButtonVisible(true);
        mietende.setClearButtonVisible(true);

        wohnungMultiSelectComboBox.setItems(availableWohnungen);
        wohnungMultiSelectComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);

        // Initially check if there are available Wohnungen and update the UI accordingly
        refreshAvailableWohnungen();

        add(name, vorname, telefonnummer, einkommen, mietbeginn, mietende, kaution, anzahlBewohner, wohnungMultiSelectComboBox, createButtonsLayout());
        configureValidation();
    }

    /**
     * Configures the validation for the fields in the form.
     * Sets up regex validators for text fields, range validators for number fields,
     * and custom validators for date fields.
     */
    private void configureValidation() {
        configureTextField(name, "^[a-zA-Z\\s]{1,50}$", "Name darf nur Buchstaben und Leerzeichen enthalten, 1-50 Zeichen", Mieter::getName, Mieter::setName);
        configureTextField(vorname, "^[a-zA-Z\\s]{1,50}$", "Vorname darf nur Buchstaben und Leerzeichen enthalten, 1-50 Zeichen", Mieter::getVorname, Mieter::setVorname);
        configureTextField(telefonnummer, "^\\d{6,12}$", "Telefonnummer must contain 6-12 numbers, no +,-", Mieter::getTelefonnummer, Mieter::setTelefonnummer);
        configureNumberField(einkommen, "Einkommen must be a positive number", 0.01, Double.MAX_VALUE, Mieter::getEinkommen, Mieter::setEinkommen);
        configureNumberField(kaution, "Kaution must be a positive number", 0.01, Double.MAX_VALUE, Mieter::getKaution, Mieter::setKaution);
        configureIntegerField(anzahlBewohner, "Anzahl Bewohner must be a positive integer", 1, Integer.MAX_VALUE, Mieter::getAnzahlBewohner, Mieter::setAnzahlBewohner);
        binder.forField(mietbeginn)
                .withValidator(new DateRangeValidator("Mietbeginn must be a past or present date", LocalDate.of(1900, 1, 1), LocalDate.now()))
                .bind(Mieter::getMietbeginn, Mieter::setMietbeginn);
        binder.forField(mietende)
                .withValidator((value, context) -> {
                    if (value == null || mietbeginn.getValue() == null) {
                        return ValidationResult.ok();
                    }
                    return value.isAfter(mietbeginn.getValue()) ? ValidationResult.ok() : ValidationResult.error("Mietende must be after Mietbeginn");
                })
                .bind(Mieter::getMietende, Mieter::setMietende);
    }

    /**
     * Configures a text field with a regex validator.
     *
     * @param field        The text field to configure.
     * @param regex        The regex pattern for validation.
     * @param errorMessage The error message to display if validation fails.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     */
    private void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<Mieter, String> getter, Setter<Mieter, String> setter) {
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Configures a number field with a range validator.
     *
     * @param field        The number field to configure.
     * @param errorMessage The error message to display if validation fails.
     * @param min          The minimum valid value.
     * @param max          The maximum valid value.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     */
    private void configureNumberField(NumberField field, String errorMessage, double min, double max, ValueProvider<Mieter, Double> getter, Setter<Mieter, Double> setter) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new DoubleRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

    /**
     * Configures an integer field with a range validator.
     *
     * @param field        The integer field to configure.
     * @param errorMessage The error message to display if validation fails.
     * @param min          The minimum valid value.
     * @param max          The maximum valid value.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     */
    private void configureIntegerField(IntegerField field, String errorMessage, int min, int max, ValueProvider<Mieter, Integer> getter, Setter<Mieter, Integer> setter) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new IntegerRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }


    // Bind the Mieter to the binder
    public void setMieter(Mieter mieter) {
        if (mieter != null) {
            this.mieter = mieter;
            binder.readBean(mieter);
            wohnungMultiSelectComboBox.setValue(new HashSet<>(mieter.getWohnung()));
            loeschen.setVisible(mieter.getMieter_id() != null);
        } else {
            this.mieter = new Mieter();
            binder.readBean(this.mieter);
            wohnungMultiSelectComboBox.clear();
            loeschen.setVisible(false);
        }
    }

    //Erstellt und konfiguriert eines Layout für die Buttons Speichern, Löschen und Schließen
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
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    /**
     * Validates the form inputs and saves the Mieter (tenant) if the inputs are valid.
     * Checks for duplicate Mieters and manages the assignment and removal of Wohnungen (apartments).
     * Shows appropriate notifications based on the success or failure of the operation.
     */
    private void validateAndSave() {
        if (binder.writeBeanIfValid(mieter)) {
            // Check for duplicate Mieters
            if (isDuplicateMieter(mieter)) {
                NotificationPopup.showErrorNotification("Ein Mieter mit dem gleichen Namen und Telefonnummer existiert bereits.");
                return;
            }

            // Get the current and selected Wohnungen
            List<Wohnung> currentWohnungen = new ArrayList<>(mieter.getWohnung());
            List<Wohnung> selectedWohnungen = new ArrayList<>(wohnungMultiSelectComboBox.getValue());

            // Determine which Wohnungen to remove
            List<Wohnung> toRemove = currentWohnungen.stream()
                    .filter(wohnung -> !selectedWohnungen.contains(wohnung))
                    .collect(Collectors.toList());

            // Save or update the Mieter and Wohnungen
            if (!toRemove.isEmpty()) {
                mieterService.removeWohnungFromMieter(mieter, toRemove);
            }
            mieterService.saveWohnungToMieter(mieter, selectedWohnungen);

            // Refresh available Wohnungen list
            refreshAvailableWohnungen();

            fireEvent(new SaveEvent(this, mieter));
            NotificationPopup.showSuccessNotification("Mieter erfolgreich gespeichert.");
        } else {
            NotificationPopup.showErrorNotification("Fehler beim Speichern des Mieters.");
        }
    }

    /**
     * Checks if a Mieter with the same name and phone number already exists in the system.
     *
     * @param mieter The Mieter to check for duplicates.
     * @return true if a duplicate Mieter exists, false otherwise.
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
     * Refreshes the list of available Wohnungen (apartments) and updates the items in the MultiSelectComboBox.
     * If no Wohnungen are available, sets a placeholder message in the ComboBox.
     */
    private void refreshAvailableWohnungen() {
        availableWohnungen = mieterService.findAvailableWohnungen();
        if (availableWohnungen.isEmpty()) {
            wohnungMultiSelectComboBox.setPlaceholder("Keine Wohnungen verfügbar");
        } else {
            wohnungMultiSelectComboBox.setItems(availableWohnungen);
        }
    }

    //Fügt einen Listener für die angegebene Ereignis hinzu (Speichern, Löschen und Schließen)
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events für die Buttons Speichern, Löschen und Schließen
    public static abstract class MieterFormEvent extends ComponentEvent<MieterForm> {
        private final Mieter mieter;

        //Konstruktor für das MieterFormEvent
        protected MieterFormEvent(MieterForm source, Mieter mieter) {
            super(source, false);
            this.mieter = mieter;
        }

        //Event um die Mieter in das Form zu lesen
        public Mieter getContact() {
            return mieter;
        }
    }

    private void showDeleteConfirmationDialog() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Are you sure you want to delete this Mieter?",
                () -> {
                    fireEvent(new DeleteEvent(this, mieter));
                    NotificationPopup.showSuccessNotification("Mieter erfolgreich gelöscht.");
                }
        );
        confirmationDialog.open();
    }

    //Ereignis, das ausgelöst wird, wenn ein Mieter gespeichert wird
    public static class SaveEvent extends MieterFormEvent {
        SaveEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    //Ereignis, das ausgelöst wird, wenn ein Mieter gelöscht wird
    public static class DeleteEvent extends MieterFormEvent {
        DeleteEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    //Ereignis, das ausgelöst wird, wenn das Formular geschlossen wird
    public static class CloseEvent extends MieterFormEvent {
        CloseEvent(MieterForm source) {
            super(source, null);
        }
    }

}