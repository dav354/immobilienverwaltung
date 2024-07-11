package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.ZaehlerstandService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.DialogLayout;

/**
 * Ein Dialog zum Bearbeiten eines Zaehlerstands (Zählerstand).
 */
public class ZaehlerstandDialog extends DialogLayout {

    private final Binder<Zaehlerstand> binder = new Binder<>(Zaehlerstand.class);

    /**
     * Konstruktor für ZaehlerstandDialog.
     *
     * @param zaehlerstandService der Service zur Verwaltung der Zaehlerstand-Operationen.
     * @param zaehlerstand der zu bearbeitende Zaehlerstand.
     * @param onSave der Callback, der nach dem Speichern des Zaehlerstands ausgeführt wird.
     * @param configurationService der ConfigurationService zur Verwaltung der Konfigurationseinstellungen.
     */
    public ZaehlerstandDialog(ZaehlerstandService zaehlerstandService, Zaehlerstand zaehlerstand, Runnable onSave, ConfigurationService configurationService) {
        super(configurationService);

        // Erstellen des Formularlayouts
        FormLayout formLayout = new FormLayout();

        // Erstellen und Binden der Felder
        TextField nameField = new TextField("Name");
        binder.forField(nameField).asRequired("Name ist erforderlich")
                .bind(Zaehlerstand::getName, Zaehlerstand::setName);

        DatePicker ablesedatumField = new DatePicker("Ablesedatum");
        binder.forField(ablesedatumField).asRequired("Ablesedatum ist erforderlich")
                .bind(Zaehlerstand::getAblesedatum, Zaehlerstand::setAblesedatum);

        NumberField ablesewertField = new NumberField("Ablesewert");
        binder.forField(ablesewertField).asRequired("Ablesewert ist erforderlich")
                .bind(Zaehlerstand::getAblesewert, Zaehlerstand::setAblesewert);

        // Aktuelle Zaehlerstand-Details in den Binder einlesen
        binder.readBean(zaehlerstand);

        // Felder zum Formularlayout hinzufügen
        formLayout.add(nameField, ablesedatumField, ablesewertField);

        // Erstellen der Schaltfläche "Speichern" mit Logik zum Speichern
        Button saveButton = new Button("Speichern", event -> {
            if (binder.writeBeanIfValid(zaehlerstand)) {
                zaehlerstandService.saveZaehlerstand(zaehlerstand);
                onSave.run();
                this.close();
                NotificationPopup.showSuccessNotification("Zaehlerstand gespeichert.");
            } else {
                NotificationPopup.showWarningNotification("Bitte füllen Sie alle erforderlichen Felder aus.");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Erstellen der Schaltfläche "Abbrechen"
        Button cancelButton = new Button("Abbrechen", event -> this.close());

        // Formularlayout und Schaltflächen zum Dialog hinzufügen
        add(formLayout, new HorizontalLayout(saveButton, cancelButton));

        // Größe des Dialogs festlegen
        setWidth("500px");
        setHeight("320px");

        // Tastenkürzel für Enter-Taste hinzufügen
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
    }
}