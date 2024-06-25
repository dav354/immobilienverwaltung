package projektarbeit.immobilienverwaltung.ui.views.wohnung.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import projektarbeit.immobilienverwaltung.model.Land;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

/**
 * A dialog for editing a Wohnung (apartment).
 */
public class WohnungEditDialog extends Dialog {

    private final Binder<Wohnung> binder = new Binder<>(Wohnung.class);

    /**
     * Constructs a new WohnungEditDialog.
     *
     * @param wohnungService the service to handle Wohnung operations
     * @param wohnung        the Wohnung to be edited
     * @param onSave         the callback to execute after saving the Wohnung
     */
    public WohnungEditDialog(WohnungService wohnungService, Wohnung wohnung, Runnable onSave) {

        // Create the form layout
        FormLayout formLayout = new FormLayout();

        // Create and bind fields
        TextField strasseField = new TextField("Strasse");
        binder.forField(strasseField).bind(Wohnung::getStrasse, Wohnung::setStrasse);

        TextField hausnummerField = new TextField("Hausnummer");
        binder.forField(hausnummerField).bind(Wohnung::getHausnummer, Wohnung::setHausnummer);

        TextField plzField = new TextField("PLZ");
        binder.forField(plzField).bind(Wohnung::getPostleitzahl, Wohnung::setPostleitzahl);

        TextField stadtField = new TextField("Stadt");
        binder.forField(stadtField).bind(Wohnung::getStadt, Wohnung::setStadt);

        IntegerField gesamtQuadratmeterField = new IntegerField("GesamtQuadratmeter");
        binder.forField(gesamtQuadratmeterField).bind(Wohnung::getGesamtQuadratmeter, Wohnung::setGesamtQuadratmeter);

        IntegerField baujahrField = new IntegerField("Baujahr");
        binder.forField(baujahrField).bind(Wohnung::getBaujahr, Wohnung::setBaujahr);

        IntegerField anzahlBaederField = new IntegerField("Anzahl Bäder");
        binder.forField(anzahlBaederField).bind(Wohnung::getAnzahlBaeder, Wohnung::setAnzahlBaeder);

        IntegerField anzahlSchlafzimmerField = new IntegerField("Anzahl Schlafzimmer");
        binder.forField(anzahlSchlafzimmerField).bind(Wohnung::getAnzahlSchlafzimmer, Wohnung::setAnzahlSchlafzimmer);

        ComboBox<Land> landComboBox = new ComboBox<>("Land");
        landComboBox.setItems(Land.values());
        landComboBox.setItemLabelGenerator(Land::getName);
        binder.forField(landComboBox).bind(Wohnung::getLand, Wohnung::setLand);

        Checkbox hatBalkon = new Checkbox("Hat Balkon");
        binder.forField(hatBalkon).bind(Wohnung::isHatBalkon, Wohnung::setHatBalkon);

        Checkbox hatTerrasse = new Checkbox("Hat Terrasse");
        binder.forField(hatTerrasse).bind(Wohnung::isHatTerrasse, Wohnung::setHatTerrasse);

        Checkbox hatGarten = new Checkbox("Hat Garten");
        binder.forField(hatGarten).bind(Wohnung::isHatGarten, Wohnung::setHatGarten);

        Checkbox hatKlimaanlage = new Checkbox("Hat Klimaanlage");
        binder.forField(hatKlimaanlage).bind(Wohnung::isHatKlimaanlage, Wohnung::setHatKlimaanlage);

        HorizontalLayout balkonTerrasseLayout = new HorizontalLayout(hatBalkon, hatTerrasse);
        HorizontalLayout gartenKlimaanlageLayout = new HorizontalLayout(hatGarten, hatKlimaanlage);

        // Read the current Wohnung details into the binder
        binder.readBean(wohnung);

        // Add fields to the form layout
        formLayout.add(strasseField, hausnummerField, plzField, stadtField, gesamtQuadratmeterField, baujahrField, anzahlBaederField, anzahlSchlafzimmerField, landComboBox, balkonTerrasseLayout, gartenKlimaanlageLayout);

        // Create save button with save logic
        Button saveButton = new Button("Save", event -> {
            if (binder.writeBeanIfValid(wohnung)) {
                wohnungService.save(wohnung);
                onSave.run();
                this.close();
                NotificationPopup.showSuccessNotification("Wohnung saved.");
            } else {
                NotificationPopup.showWarningNotification("Please fill out all required fields.");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Create cancel button
        Button cancelButton = new Button("Cancel", event -> this.close());

        // Add form layout and buttons to the dialog
        add(formLayout, new HorizontalLayout(saveButton, cancelButton));

        // Set the size of the dialog
        setWidth("500px");
        setHeight("850px");
    }
}