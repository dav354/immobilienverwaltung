package projektarbeit.immobilienverwaltung.ui.views.mieter;

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
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PermitAll
@SuppressWarnings("SpellCheckingInspection")
public class MieterForm extends FormLayout {

    private final MietvertragService mietvertragService;
    private final MieterService mieterService;
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

    // Die 3 Knöpfe zum Speichern, Löschen und Schließen des Forms
    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");

    /**
     * Constructs a MieterForm with the given services.
     *
     * @param mieterService the MieterService to use for managing Mieter entities
     * @param mietvertragService the MietvertragService to use for managing Mietvertrag entities
     * @param wohnungsService the WohnungService to use for managing Wohnung entities
     */
    public MieterForm(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungsService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungsService = wohnungsService;
        this.availableWohnungen = wohnungsService.findWohnungenWithoutMietvertrag();
        addClassName("mieter-form");

        binder.bindInstanceFields(this);
        mietvertragBinder.bindInstanceFields(this);

        mietbeginn.setClearButtonVisible(true);
        mietende.setClearButtonVisible(true);

        wohnungMultiSelectComboBox.setItems(availableWohnungen);
        wohnungMultiSelectComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);

        add(name, vorname, telefonnummer, email, einkommen, mietbeginn, mietende, kaution, miete, anzahlBewohner, wohnungMultiSelectComboBox, createButtonsLayout());
        configureValidation();
    }

    /**
     * Configures the validation for the fields in the form.
     * Sets up regex validators for text fields, range validators for number fields,
     * and custom validators for date fields.
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
     * Configures a text field with a regex validator.
     *
     * @param <T>          The type of the bean.
     * @param field        The text field to configure.
     * @param regex        The regex pattern for validation.
     * @param errorMessage The error message to display if validation fails.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     * @param binder       The binder to use for validation.
     */
    private <T> void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<T, String> getter, Setter<T, String> setter, Binder<T> binder) {
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Configures a number field with a range validator.
     *
     * @param <T>          The type of the bean.
     * @param field        The number field to configure.
     * @param errorMessage The error message to display if validation fails.
     * @param min          The minimum valid value.
     * @param max          The maximum valid value.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     * @param binder       The binder to use for validation.
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
     * Configures an integer field with a range validator.
     *
     * @param <T>          The type of the bean.
     * @param field        The integer field to configure.
     * @param errorMessage The error message to display if validation fails.
     * @param min          The minimum valid value.
     * @param max          The maximum valid value.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     * @param binder       The binder to use for validation.
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
     * Binds the given Mieter entity to the form.
     * Updates the form fields with the Mieter's data and associated Mietvertrag information.
     *
     * @param mieter the Mieter entity to bind to the form
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
        mietvertragBinder.addStatusChangeListener(e -> speichern.setEnabled(mietvertragBinder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    /**
     * Validates the form inputs and saves the Mieter (tenant) if the inputs are valid.
     * Checks for duplicate Mieters and manages the assignment and removal of Wohnungen (apartments).
     * Shows appropriate notifications based on the success or failure of the operation.
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
     * Validates that the Mietbeginn (start date) is before the Mietende (end date).
     *
     * @return true if the Mietperiode is valid, false otherwise.
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
        availableWohnungen = wohnungsService.findAvailableWohnungen();
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

        protected MieterFormEvent(MieterForm source, Mieter mieter) {
            super(source, false);
            this.mieter = mieter;
        }

        public Mieter getContact() {
            return mieter;
        }
    }

    /**
     * Displays a confirmation dialog to confirm the deletion of a Mieter.
     * If confirmed, fires a DeleteEvent and shows a success notification.
     */
    private void showDeleteConfirmationDialog() {
        // Create a new ConfirmationDialog with a message and a confirm action
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

    public static class CloseEvent extends MieterFormEvent {
        CloseEvent(MieterForm source) {
            super(source, null);
        }
    }
}