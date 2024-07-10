package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import projektarbeit.immobilienverwaltung.model.Land;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.DialogLayout;

import java.time.LocalDate;

/**
 * A dialog for editing a Wohnung (apartment).
 */
public class WohnungEditDialog extends DialogLayout {

    private final Binder<Wohnung> binder = new Binder<>(Wohnung.class);

    /**
     * Konstruktor für WohnungEditDialog.
     *
     * @param wohnungService der Service zur Verwaltung von Wohnungen.
     * @param wohnung die zu bearbeitende Wohnung.
     * @param onSave der Callback, der nach dem Speichern der Wohnung ausgeführt wird.
     * @param configurationService der ConfigurationService zur Verwaltung der Konfigurationseinstellungen.
     */
    public WohnungEditDialog(WohnungService wohnungService, Wohnung wohnung, Runnable onSave, ConfigurationService configurationService) {
        super(configurationService);

        // Create the form layout
        FormLayout formLayout = new FormLayout();

        // Create and bind fields
        TextField strasseField = new TextField("Strasse");
        strasseField.setClearButtonVisible(true);
        strasseField.setWidthFull();
        binder.forField(strasseField).asRequired("Strasse ist erforderlich")
                .withValidator(new RegexpValidator("Strasse darf nur Buchstaben enthalten", "^[\\p{L}äöüÄÖÜß\\s\\-]+$", true))
                .bind(Wohnung::getStrasse, Wohnung::setStrasse);

        TextField hausnummerField = new TextField("Hausnr.");
        hausnummerField.setClearButtonVisible(true);
        hausnummerField.setWidth("100px");
        binder.forField(hausnummerField).asRequired("Hausnummer ist erforderlich")
                .withValidator(new RegexpValidator("Hausnummer muss numerisch sein mit optionalem Buchstaben", "^\\d+[a-zA-Z]?$", true))
                .bind(Wohnung::getHausnummer, Wohnung::setHausnummer);

        TextField plzField = new TextField("PLZ");
        plzField.setClearButtonVisible(true);
        plzField.setWidth("150px");
        binder.forField(plzField).asRequired("Postleitzahl ist erforderlich")
                .withValidator(new RegexpValidator("Postleitzahl muss 4 bis 10 Ziffern lang sein", "^\\d{4,10}$", true))
                .bind(Wohnung::getPostleitzahl, Wohnung::setPostleitzahl);

        TextField stadtField = new TextField("Stadt");
        stadtField.setClearButtonVisible(true);
        stadtField.setWidthFull();
        binder.forField(stadtField).asRequired("Stadt ist erforderlich")
                .withValidator(new RegexpValidator("Stadt darf nur Buchstaben enthalten", "^[\\p{L}\\s]+$", true))
                .bind(Wohnung::getStadt, Wohnung::setStadt);

        TextField stockwerk = new TextField("Stockwerk");
        stockwerk.setClearButtonVisible(true);
        stockwerk.setWidth("300px");
        binder.forField(stockwerk)
                .withValidator(new RegexpValidator("Stockwerk darf nur Zahlen enthalten", "^[0-9]*$", true))
                .bind(Wohnung::getStockwerk, Wohnung::setStockwerk);

        TextField wohnungsnummer = new TextField("Wohnungsnummer");
        wohnungsnummer.setClearButtonVisible(true);
        wohnungsnummer.setWidth("300px");
        binder.forField(wohnungsnummer)
                .withValidator(new RegexpValidator("Wohnungsnummer darf nur Buchstaben und Zahlen enthalten", "^[a-zA-Z0-9]*$", true))
                .bind(Wohnung::getWohnungsnummer, Wohnung::setWohnungsnummer);

        IntegerField gesamtQuadratmeterField = new IntegerField("Quadratmeter");
        gesamtQuadratmeterField.setClearButtonVisible(true);
        gesamtQuadratmeterField.setWidth("300px");
        binder.forField(gesamtQuadratmeterField).asRequired("Gesamt Quadratmeter muss positiv sein")
                .withValidator(new IntegerRangeValidator("Gesamt Quadratmeter muss positiv sein", 1, Integer.MAX_VALUE))
                .bind(Wohnung::getGesamtQuadratmeter, Wohnung::setGesamtQuadratmeter);

        IntegerField baujahrField = new IntegerField("Baujahr");
        baujahrField.setClearButtonVisible(true);
        baujahrField.setWidth("300px");
        binder.forField(baujahrField).asRequired("Baujahr muss ein gültiges Jahr zwischen 1000 und " + LocalDate.now().getYear() + " sein")
                .withValidator(new IntegerRangeValidator("Baujahr muss ein gültiges Jahr zwischen 1000 und " + LocalDate.now().getYear() + " sein", 1000, LocalDate.now().getYear()))
                .bind(Wohnung::getBaujahr, Wohnung::setBaujahr);

        IntegerField anzahlBaederField = new IntegerField("Anzahl Bäder");
        anzahlBaederField.setClearButtonVisible(true);
        anzahlBaederField.setWidth("300px");
        binder.forField(anzahlBaederField).asRequired("Anzahl Bäder muss positiv sein")
                .withValidator(new IntegerRangeValidator("Anzahl Bäder muss positiv sein", 1, Integer.MAX_VALUE))
                .bind(Wohnung::getAnzahlBaeder, Wohnung::setAnzahlBaeder);

        IntegerField anzahlSchlafzimmerField = new IntegerField("Anzahl Schlafzimmer");
        anzahlSchlafzimmerField.setClearButtonVisible(true);
        anzahlSchlafzimmerField.setWidth("300px");
        binder.forField(anzahlSchlafzimmerField).asRequired("Anzahl Schlafzimmer muss null oder positiv sein")
                .withValidator(new IntegerRangeValidator("Anzahl Schlafzimmer muss null oder positiv sein", 0, Integer.MAX_VALUE))
                .bind(Wohnung::getAnzahlSchlafzimmer, Wohnung::setAnzahlSchlafzimmer);

        ComboBox<Land> landComboBox = new ComboBox<>("Land");
        landComboBox.setWidth("300px");
        landComboBox.setItems(Land.values());
        landComboBox.setItemLabelGenerator(Land::getName);
        binder.forField(landComboBox).asRequired("Land ist erforderlich").bind(Wohnung::getLand, Wohnung::setLand);

        Checkbox hatBalkon = new Checkbox("Hat Balkon");
        hatBalkon.setWidth("300px");
        binder.forField(hatBalkon).bind(Wohnung::isHatBalkon, Wohnung::setHatBalkon);

        Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
        hatTerrasse.setWidth("300px");
        binder.forField(hatTerrasse).bind(Wohnung::isHatTerrasse, Wohnung::setHatTerrasse);

        Checkbox hatGarten = new Checkbox("Hat Garten");
        hatGarten.setWidth("300px");
        binder.forField(hatGarten).bind(Wohnung::isHatGarten, Wohnung::setHatGarten);

        Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");
        hatKlimaanlage.setWidth("300px");
        binder.forField(hatKlimaanlage).bind(Wohnung::isHatKlimaanlage, Wohnung::setHatKlimaanlage);

        Div spacer = new Div();
        spacer.setHeight("20px");
        formLayout.add(spacer);

        // Read the current Wohnung details into the binder
        binder.readBean(wohnung);

        HorizontalLayout plzStadt = new HorizontalLayout(stadtField, plzField);
        HorizontalLayout strasseHausnummer = new HorizontalLayout(strasseField, hausnummerField);
        HorizontalLayout qmBaujahr = new HorizontalLayout(gesamtQuadratmeterField, baujahrField);
        HorizontalLayout baederSchlafzimmer = new HorizontalLayout(anzahlBaederField, anzahlSchlafzimmerField);
        HorizontalLayout stockwerkWohnungsnumemr = new HorizontalLayout(stockwerk, wohnungsnummer);

        HorizontalLayout gartenKlima = new HorizontalLayout(hatGarten, hatKlimaanlage);
        gartenKlima.setWidthFull();
        HorizontalLayout balkonTerasse = new HorizontalLayout(hatBalkon, hatTerrasse);
        balkonTerasse.setWidthFull();

        // Add fields to the form layout
        formLayout.add(landComboBox, plzStadt, strasseHausnummer, stockwerkWohnungsnumemr, qmBaujahr, baederSchlafzimmer, spacer, gartenKlima, balkonTerasse);

        // Create save button with save logic
        Button saveButton = new Button("Save", event -> saveWohnung(wohnung, wohnungService, onSave));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Create cancel button
        Button cancelButton = new Button("Cancel", event -> this.close());

        // Add form layout and buttons to the dialog
        add(formLayout, spacer, new HorizontalLayout(saveButton, cancelButton));

        // Set the size of the dialog
        setWidth("450px");
        setHeight("625px");

        // Enable the save button only if the binder is valid
        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        // Add keyboard shortcut for Enter key
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        // Add save callback after closing dialog
        addDialogCloseActionListener(e -> onSave.run());
    }

    private void saveWohnung(Wohnung wohnung, WohnungService wohnungService, Runnable onSave) {
        if (binder.writeBeanIfValid(wohnung)) {
            wohnungService.save(wohnung);
            onSave.run();
            this.close();
            NotificationPopup.showSuccessNotification("Wohnung saved.");
        } else {
            NotificationPopup.showWarningNotification("Please fill out all required fields.");
        }
    }
}