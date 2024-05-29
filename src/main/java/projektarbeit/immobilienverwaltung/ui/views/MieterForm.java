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
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projektarbeit.immobilienverwaltung.demo.AssignMieterToWohnungDemo;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.MieterService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MieterForm extends FormLayout {

    private final MieterService mieterService;
    private List<Wohnung> availableWohnungen;
    private Mieter mieter;
    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);

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

    private void configureValidation() {
        // Name validation
        binder.forField(name)
                .asRequired("Name is required")
                .withValidator(new RegexpValidator("Name darf nur Buchstaben enthalten", "^[a-zA-Z\\s]+$"))
                .withValidator(new StringLengthValidator("Name must be between 1 and 50 characters", 1,50))
                .bind(Mieter::getName, Mieter::setName);

        // Vorname validation
        binder.forField(vorname)
                .asRequired("Vorname is required")
                .withValidator(new StringLengthValidator("Vorname must be between 1 and 50 characters", 1, 50))
                .withValidator(new RegexpValidator("Vorname darf nur Buchstaben enthalten", "^[a-zA-Z\\s]+$"))
                .bind(Mieter::getVorname, Mieter::setVorname);

        // Telefonnummer validation
        binder.forField(telefonnummer)
                .asRequired("Telefonnummer is required")
                .withValidator(new RegexpValidator("Telefonnummer must contain 6-12 numbers, no +,-", "^\\d{6,12}$"))
                .bind(Mieter::getTelefonnummer, Mieter::setTelefonnummer);

        // Einkommen validation
        binder.forField(einkommen)
                .asRequired("Einkommen is required")
                .withValidator(new DoubleRangeValidator("Einkommen must be a positive number", 0.01, Double.MAX_VALUE))
                .bind(Mieter::getEinkommen, Mieter::setEinkommen);

        // Mietbeginn validation
        binder.forField(mietbeginn)
                .withValidator(new DateRangeValidator("Mietbeginn must be a past or present date", LocalDate.of(1900, 1, 1), LocalDate.now()))
                .bind(Mieter::getMietbeginn, Mieter::setMietbeginn);

        // Mietende validation
        binder.forField(mietende)
                .withValidator((value, context) -> {
                    if (value == null || mietbeginn.getValue() == null) {
                        return ValidationResult.ok();
                    }
                    return value.isAfter(mietbeginn.getValue()) ? ValidationResult.ok() : ValidationResult.error("Mietende must be after Mietbeginn");
                })
                .bind(Mieter::getMietende, Mieter::setMietende);

        // Kaution validation
        binder.forField(kaution)
                .asRequired("Kaution is required")
                .withValidator(new DoubleRangeValidator("Kaution must be a positive number", 0.01, Double.MAX_VALUE))
                .bind(Mieter::getKaution, Mieter::setKaution);

        // Anzahl Bewohner validation
        binder.forField(anzahlBewohner)
                .asRequired("Anzahl der Bewohner is required")
                .withValidator(new IntegerRangeValidator("Anzahl Bewohner must be a positive integer", 1, Integer.MAX_VALUE))
                .bind(Mieter::getAnzahlBewohner, Mieter::setAnzahlBewohner);
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
        loeschen.addClickListener(event -> fireEvent(new DeleteEvent(this, mieter)));
        schliessen.addClickListener(event -> fireEvent(new CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    // Validates the inputs in the form and saves the data if valid
    private void validateAndSave() {
        try {
            binder.writeBean(mieter);

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
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Refreshes the available Wohnungen list and updates the MultiSelectComboBox items
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