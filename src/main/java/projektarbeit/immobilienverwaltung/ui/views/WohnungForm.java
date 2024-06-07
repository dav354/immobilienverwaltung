package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import projektarbeit.immobilienverwaltung.model.Land;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.time.LocalDate;
import java.util.List;

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
    ComboBox<Mieter> mieterComboBox = new ComboBox<>("Mieter");
    Checkbox hatBalkon = new Checkbox("Hat Balkon");
    Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
    Checkbox hatGarten = new Checkbox("Hat Garten");
    Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");

    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");
    private Wohnung wohnung;

    public WohnungForm(List<Mieter> mieters) {
        addClassName("wohnung-form");
        binder.bindInstanceFields(this);

        land.setItems(Land.values()); // Populate ComboBox with Enum values
        land.setItemLabelGenerator(Land::getName);

        mieterComboBox.setItems(mieters);
        mieterComboBox.setItemLabelGenerator(Mieter::getFullName);
        mieterComboBox.setClearButtonVisible(true); // Allow clearing the selection

        add(land, postleitzahl, stadt, strasse, hausnummer, gesamtQuadratmeter, baujahr, anzahlBaeder, anzahlSchlafzimmer, mieterComboBox, hatBalkon, hatTerrasse, hatGarten, hatKlimaanlage, createButtonsLayout());
        configureValidation();
    }

    private void configureValidation() {
        configureTextField(postleitzahl, "^\\d{4,10}$", "Postleitzahl must be 4 to 10 digits long", Wohnung::getPostleitzahl, Wohnung::setPostleitzahl);
        configureTextField(stadt, "^[\\p{L}\\s]+$", "Stadt must contain only letters", Wohnung::getStadt, Wohnung::setStadt);
        configureTextField(strasse, "^[\\p{L}\\s]+$", "Strasse must contain only letters", Wohnung::getStrasse, Wohnung::setStrasse);
        configureTextField(hausnummer, "^\\d+[a-zA-Z]?$", "Hausnummer must be numeric with an optional letter", Wohnung::getHausnummer, Wohnung::setHausnummer);

        configureIntegerField(gesamtQuadratmeter, "Gesamt Quadratmeter must be positive", 1, Integer.MAX_VALUE, Wohnung::getGesamtQuadratmeter, Wohnung::setGesamtQuadratmeter);
        configureIntegerField(baujahr, "Baujahr must be a valid year between 1000 and " + LocalDate.now().getYear(), 1000, LocalDate.now().getYear(), Wohnung::getBaujahr, Wohnung::setBaujahr);
        configureIntegerField(anzahlBaeder, "Anzahl Baeder must be positive", 1, Integer.MAX_VALUE, Wohnung::getAnzahlBaeder, Wohnung::setAnzahlBaeder);
        configureIntegerField(anzahlSchlafzimmer, "Anzahl Schlafzimmer must be zero or positive", 0, Integer.MAX_VALUE, Wohnung::getAnzahlSchlafzimmer, Wohnung::setAnzahlSchlafzimmer);

        binder.forField(land)
                .asRequired("Land is required")
                .bind(Wohnung::getLand, Wohnung::setLand);

        binder.addStatusChangeListener(event -> speichern.setEnabled(binder.isValid()));
    }

    private void configureTextField(TextField field, String regex, String errorMessage, ValueProvider<Wohnung, String> getter, Setter<Wohnung, String> setter) {
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new RegexpValidator(errorMessage, regex, true))
                .bind(getter, setter);
    }

    private void configureIntegerField(IntegerField field, String errorMessage, int min, int max, ValueProvider<Wohnung, Integer> getter, Setter<Wohnung, Integer> setter) {
        field.setClearButtonVisible(true);
        binder.forField(field)
                .asRequired(errorMessage)
                .withValidator(new IntegerRangeValidator(errorMessage, min, max))
                .bind(getter, setter);
    }

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

            if (wohnung.getMieter() != null) {
                mieterComboBox.setValue(wohnung.getMieter());
            } else {
                mieterComboBox.clear();
            }

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
        mieterComboBox.clear();
        hatBalkon.setValue(false);
        hatTerrasse.setValue(false);
        hatGarten.setValue(false);
        hatKlimaanlage.setValue(false);
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

    private void validateAndSave() {
        if (wohnung != null) {
            // Trigger validation manually
            if (binder.writeBeanIfValid(wohnung)) {
                // Ensure Mieter is set from ComboBox
                wohnung.setMieter(mieterComboBox.getValue());
                fireEvent(new SaveEvent(this, wohnung));
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.add(new Text("Are you sure you want to delete this Wohnung?"));

        Button confirmButton = new Button("Delete", event -> {
            confirmationDialog.close();
            fireEvent(new DeleteEvent(this, wohnung));
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
        confirmationDialog.add(buttons);
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