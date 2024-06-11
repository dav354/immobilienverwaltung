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

    public WohnungForm(List<Mieter> mieters, MietvertragService mietvertragService) {
        this.mietvertragService = mietvertragService;
        addClassName("wohnung-form");
        binder.bindInstanceFields(this);

        land.setItems(Land.values()); // Populate ComboBox with Enum values
        land.setItemLabelGenerator(Land::getName);

        add(land, postleitzahl, stadt, strasse, hausnummer, stockwerk, wohnungsnummer, gesamtQuadratmeter, baujahr, anzahlBaeder, anzahlSchlafzimmer, mieterSpan, hatBalkon, hatTerrasse, hatGarten, hatKlimaanlage, createButtonsLayout());
        configureValidation();
    }

    /**
     * Configures the validation for the fields in the form.
     * Sets up regex validators for text fields, range validators for integer fields,
     * and a custom validator for the land field.
     */
    private void configureValidation() {
        // Required text fields with regex validation
        configureTextField(postleitzahl, "^\\d{4,10}$", "Postleitzahl must be 4 to 10 digits long", "Postleitzahl is required", Wohnung::getPostleitzahl, Wohnung::setPostleitzahl);
        configureTextField(stadt, "^[\\p{L}\\s]+$", "Stadt must contain only letters", "Stadt is required", Wohnung::getStadt, Wohnung::setStadt);
        configureTextField(strasse, "^[\\p{L}\\s]+$", "Strasse must contain only letters", "Strasse is required", Wohnung::getStrasse, Wohnung::setStrasse);
        configureTextField(hausnummer, "^\\d+[a-zA-Z]?$", "Hausnummer must be numeric with an optional letter", "Hausnummer is required", Wohnung::getHausnummer, Wohnung::setHausnummer);

        // Optional text fields with regex validation
        configureTextField(stockwerk, "^[0-9]*$", "Stockwerk must contain only numbers", Wohnung::getStockwerk, Wohnung::setStockwerk);
        configureTextField(wohnungsnummer, "^[a-zA-Z0-9]*$", "Wohnungsnummer must contain only letters and numbers", Wohnung::getWohnungsnummer, Wohnung::setWohnungsnummer);

        // Required integer fields with range validation
        configureIntegerField(gesamtQuadratmeter, "Gesamt Quadratmeter must be positive", 1, Integer.MAX_VALUE, Wohnung::getGesamtQuadratmeter, Wohnung::setGesamtQuadratmeter);
        configureIntegerField(baujahr, "Baujahr must be a valid year between 1000 and " + LocalDate.now().getYear(), 1000, LocalDate.now().getYear(), Wohnung::getBaujahr, Wohnung::setBaujahr);
        configureIntegerField(anzahlBaeder, "Anzahl Baeder must be positive", 1, Integer.MAX_VALUE, Wohnung::getAnzahlBaeder, Wohnung::setAnzahlBaeder);
        configureIntegerField(anzahlSchlafzimmer, "Anzahl Schlafzimmer must be zero or positive", 0, Integer.MAX_VALUE, Wohnung::getAnzahlSchlafzimmer, Wohnung::setAnzahlSchlafzimmer);

        // Required land field
        binder.forField(land).asRequired("Land is required").bind(Wohnung::getLand, Wohnung::setLand);

        Mietvertrag mietvertrag = mietvertragService.findByWohnung(wohnung);
        if (mietvertrag != null && mietvertrag.getMieter() != null) {
            mieterSpan.setText(mietvertrag.getMieter().getFullName());
        } else {
            mieterSpan.setText("Kein Mieter");
        }

        // Enable the save button only if the binder is valid
        binder.addStatusChangeListener(event -> speichern.setEnabled(binder.isValid()));
    }

    /**
     * Configures a required text field with a regex validator.
     *
     * @param field           The text field to configure.
     * @param regex           The regex pattern for validation.
     * @param errorMessage    The error message to display if validation fails.
     * @param requiredMessage The error message to display if the field is empty.
     * @param getter          The getter method for the field value.
     * @param setter          The setter method for the field value.
     */
    private void configureTextField(TextField field, String regex, String errorMessage, String requiredMessage, ValueProvider<Wohnung, String> getter, Setter<Wohnung, String> setter) {
        binder.forField(field)
                .asRequired(requiredMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Configures an optional text field with a regex validator.
     *
     * @param field        The text field to configure.
     * @param regex        The regex pattern for validation.
     * @param errorMessage The error message to display if validation fails.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     */
    private void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<Wohnung, String> getter, Setter<Wohnung, String> setter) {
        binder.forField(field)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    /**
     * Configures a required integer field with a range validator.
     *
     * @param field        The integer field to configure.
     * @param errorMessage The error message to display if validation fails.
     * @param min          The minimum valid value.
     * @param max          The maximum valid value.
     * @param getter       The getter method for the field value.
     * @param setter       The setter method for the field value.
     */
    private void configureIntegerField(IntegerField field, String errorMessage, int min, int max, ValueProvider<Wohnung, Integer> getter, Setter<Wohnung, Integer> setter) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new IntegerRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

    /**
     * Sets the Wohnung (apartment) to the form and populates the fields with its data.
     * Clears the fields if the Wohnung is null.
     *
     * @param wohnung The Wohnung to set.
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
        binder.setBean(wohnung);

        if (wohnung != null) {
            // Set values or clear fields if wohnung is null
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

            // Show delete button if wohnung exists
            loeschen.setVisible(wohnung.getWohnung_id() != null);
        } else {
            clearFields();
        }

        binder.readBean(wohnung);
    }

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
     * Validates the form inputs and saves the Wohnung (apartment) if the inputs are valid.
     * Ensures the Mieter (tenant) is set from the ComboBox and triggers a SaveEvent if successful.
     * Shows appropriate notifications based on the success or failure of the operation.
     */
    private void validateAndSave() {
        if (wohnung != null) {
            // Trigger validation manually
            if (binder.writeBeanIfValid(wohnung)) {
                // Fire the save event with the validated Wohnung
                fireEvent(new SaveEvent(this, wohnung));

                // Show success notification
                NotificationPopup.showSuccessNotification("Wohnung erfolgreich gespeichert.");
            } else {
                // Show error notification if validation fails
                NotificationPopup.showErrorNotification("Fehler beim Speichern der Wohnung.");
            }
        }
    }

    /**
     * Displays a confirmation dialog to confirm the deletion of the Wohnung (apartment).
     * If confirmed, triggers a DeleteEvent and shows a success notification.
     */
    private void showDeleteConfirmationDialog() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Are you sure you want to delete this Wohnung?",
                () -> {
                    // Fire the delete event if confirmed
                    fireEvent(new DeleteEvent(this, wohnung));
                    // Show success notification
                    NotificationPopup.showSuccessNotification("Wohnung erfolgreich gelöscht.");
                }
        );
        // Open the confirmation dialog
        confirmationDialog.open();
    }

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