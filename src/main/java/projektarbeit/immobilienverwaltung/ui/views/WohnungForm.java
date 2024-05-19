package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Land;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.AdresseService;
import projektarbeit.immobilienverwaltung.service.WohnungService;

public class WohnungForm extends Dialog {

    private final WohnungService wohnungService;
    private final AdresseService adresseService;
    private final Binder<Wohnung> binder = new Binder<>(Wohnung.class);
    private Wohnung wohnung;

    private TextField strasse = new TextField("Strasse");
    private TextField hausnummer = new TextField("Hausnummer");
    private IntegerField gesamtQuadratmeter = new IntegerField("Gesamt Quadratmeter");
    private IntegerField baujahr = new IntegerField("Baujahr");
    private IntegerField anzahlBaeder = new IntegerField("Anzahl Baeder");
    private IntegerField anzahlSchlafzimmer = new IntegerField("Anzahl Schlafzimmer");
    private Checkbox hatBalkon = new Checkbox("Hat Balkon");
    private Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
    private Checkbox hatGarten = new Checkbox("Hat Garten");
    private Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");
    private ComboBox<Land> land = new ComboBox<>("Land");
    private TextField postleitzahl = new TextField("Postleitzahl");
    private TextField stadt = new TextField("Stadt");

    private Div errorMessage = new Div();

    public WohnungForm(WohnungService wohnungService, AdresseService adresseService) {
        this.wohnungService = wohnungService;
        this.adresseService = adresseService;
        land.setItems(Land.values()); // Populate ComboBox with Enum values
        land.setItemLabelGenerator(Land::getName);
        add(createFormLayout(), createButtonLayout(), errorMessage);
        binder.bindInstanceFields(this);
        errorMessage.setVisible(false);

        getElement().getStyle().set("background-color", "var(--lumo-base-color)");
        getElement().getStyle().set("color", "var(--lumo-body-text-color)");
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
        binder.readBean(wohnung);
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(strasse, hausnummer, postleitzahl, stadt, land, gesamtQuadratmeter, baujahr, anzahlBaeder, anzahlSchlafzimmer, hatBalkon, hatTerrasse, hatGarten, hatKlimaanlage);
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
}