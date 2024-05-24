package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import projektarbeit.immobilienverwaltung.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.vaadin.flow.component.Shortcuts.addShortcutListener;

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

    Button speichern = new Button("Save");
    Button loeschen = new Button("Delete");
    Button schliessen = new Button("Cancel");
    private Wohnung wohnung;

    public WohnungForm(List<Mieter> mieters) {
        addClassName("wohnung-form");
        binder.bindInstanceFields(this);

        land.setItems(Land.values()); // Populate ComboBox with Enum values
        land.setItemLabelGenerator(Land::getName);

        mieterComboBox.setItems(mieters);
        mieterComboBox.setItemLabelGenerator(Mieter::getFullName);

        add(land,
                postleitzahl,
                stadt,
                strasse,
                hausnummer,
                gesamtQuadratmeter,
                baujahr,
                anzahlBaeder,
                anzahlSchlafzimmer,
                mieterComboBox,
                hatBalkon,
                hatTerrasse,
                hatGarten,
                hatKlimaanlage,
                createButtonsLayout());
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
        if(wohnung != null){
            if (wohnung.getAdresse() != null && wohnung.getAdresse().getPostleitzahlObj() != null) {
                land.setItems(Land.values()); // Ensure items are set before setting value
                land.setValue(wohnung.getAdresse().getPostleitzahlObj().getLand());
                postleitzahl.setValue(wohnung.getAdresse().getPostleitzahlObj().getPostleitzahl());
                stadt.setValue(wohnung.getAdresse().getPostleitzahlObj().getStadt());
                strasse.setValue(wohnung.getAdresse().getStrasse());
                hausnummer.setValue(wohnung.getAdresse().getHausnummer());
            }
            if (wohnung.getMieter() != null) {
                mieterComboBox.setValue(wohnung.getMieter());
            }else{
                clearFields();
            }
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


    //Erstellt und konfiguriert eines Layout für die Buttons Speichern, Löschen und Schließen
    private HorizontalLayout createButtonsLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> validateAndSave());
        loeschen.addClickListener(event -> fireEvent(new WohnungForm.DeleteEvent(this, wohnung)));
        schliessen.addClickListener(event -> fireEvent(new WohnungForm.CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    //Validiert die Eingaben im Formular und speichert die Daten, falls sie gültig sind
    private void validateAndSave() {
        try{
            binder.writeBean(wohnung);
            fireEvent(new WohnungForm.SaveEvent(this, wohnung));
        }catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Events für die Buttons Speichern, Löschen und Schließen
    public static abstract class WohnungFormEvent extends ComponentEvent<WohnungForm> {
        private final Wohnung wohnung;

        //Konstruktor für das MieterFormEvent
        protected WohnungFormEvent(WohnungForm source, Wohnung wohnung) {
            super(source, false);
            this.wohnung = wohnung;
        }

        //Event um die Mieter in das Form zu lesen
        public Wohnung getWohnung() {
            return wohnung;
        }
    }

    //Ereignis, das ausgelöst wird, wenn ein Wohnung gespeichert wird
    public static class SaveEvent extends WohnungForm.WohnungFormEvent {
        SaveEvent(WohnungForm source, Wohnung wohnung) {
            super(source, wohnung);
        }
    }

    //Ereignis, das ausgelöst wird, wenn eine Wohnung gelöscht wird
    public static class DeleteEvent extends WohnungForm.WohnungFormEvent {
        DeleteEvent(WohnungForm source, Wohnung wohnung) {
            super(source, wohnung);
        }
    }

    //Ereignis, das ausgelöst wird, wenn das Formular geschlossen wird
    public static class CloseEvent extends WohnungForm.WohnungFormEvent {
        CloseEvent(WohnungForm source) {
            super(source, null);
        }
    }

    //Fügt einen Listener für die angegebene Ereignis hinzu (Speichern, Löschen und Schließen)
    public <T extends  ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }

    /*
    private final WohnungService wohnungService;
    private final AdresseService adresseService;
    private final MieterService mieterService;
    private final Binder<Wohnung> binder = new Binder<>(Wohnung.class);
    private Wohnung wohnung;

    TextField strasse = new TextField("Strasse");
    TextField hausnummer = new TextField("Hausnummer");
    IntegerField gesamtQuadratmeter = new IntegerField("Gesamt Quadratmeter");
    IntegerField baujahr = new IntegerField("Baujahr");
    IntegerField anzahlBaeder = new IntegerField("Anzahl Baeder");
    IntegerField anzahlSchlafzimmer = new IntegerField("Anzahl Schlafzimmer");
    Checkbox hatBalkon = new Checkbox("Hat Balkon");
    Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
    Checkbox hatGarten = new Checkbox("Hat Garten");
    Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");
    ComboBox<Land> land = new ComboBox<>("Land");
    TextField postleitzahl = new TextField("Postleitzahl");
    TextField stadt = new TextField("Stadt");
    ComboBox<Mieter> mieterComboBox = new ComboBox<>("Mieter");

    private final Div errorMessage = new Div();

    public WohnungForm(WohnungService wohnungService, AdresseService adresseService, MieterService mieterService) {
        this.wohnungService = wohnungService;
        this.adresseService = adresseService;
        this.mieterService = mieterService;

        land.setItems(Land.values()); // Populate ComboBox with Enum values
        land.setItemLabelGenerator(Land::getName);

        mieterComboBox.setItems(mieterService.findAllMieter());
        mieterComboBox.setItemLabelGenerator(Mieter::getFullName);

        add(createFormLayout(), createButtonLayout(), errorMessage);
        binder.bindInstanceFields(this);
        errorMessage.setVisible(false);

        getElement().getStyle().set("background-color", "var(--lumo-base-color)");
        getElement().getStyle().set("color", "var(--lumo-body-text-color)");

        addShortcutListener(this, ()  -> close(), Key.ESCAPE);
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
        if (wohnung.getAdresse() != null && wohnung.getAdresse().getPostleitzahlObj() != null) {
            land.setItems(Land.values()); // Ensure items are set before setting value
            land.setValue(wohnung.getAdresse().getPostleitzahlObj().getLand());
            postleitzahl.setValue(wohnung.getAdresse().getPostleitzahlObj().getPostleitzahl());
            stadt.setValue(wohnung.getAdresse().getPostleitzahlObj().getStadt());
            strasse.setValue(wohnung.getAdresse().getStrasse());
            hausnummer.setValue(wohnung.getAdresse().getHausnummer());
        }
        if (wohnung.getMieter() != null) {
            mieterComboBox.setValue(wohnung.getMieter());
        }
        binder.readBean(wohnung);
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(strasse, hausnummer, postleitzahl, stadt, land, gesamtQuadratmeter, baujahr, anzahlBaeder, anzahlSchlafzimmer, hatBalkon, hatTerrasse, hatGarten, hatKlimaanlage, mieterComboBox);
        return formLayout;
    }

    private HorizontalLayout createButtonLayout() {
        Button saveButton = new Button("Save", event -> save());
        Button cancelButton = new Button("Cancel", event -> cancel());
        return new HorizontalLayout(saveButton, cancelButton);
    }

    private void save() {
        try {
            // Create or update the address
            Postleitzahl plz = new Postleitzahl(postleitzahl.getValue(), stadt.getValue(), land.getValue());
            Adresse adresse = new Adresse(plz, strasse.getValue(), hausnummer.getValue());

            // Save the address first
            adresseService.save(adresse);

            wohnung.setAdresse(adresse);
            wohnung.setMieter(mieterComboBox.getValue()); // Set the selected tenant
            binder.writeBean(wohnung);
            wohnungService.save(wohnung);
            Notification.show("Wohnung saved", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            close();
        } catch (ValidationException e) {
            errorMessage.setText("Error: " + e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    private void cancel() {
        Notification.show("Edit cancelled", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        close();
    }

     */
}