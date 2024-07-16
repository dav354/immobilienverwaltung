package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;

public class MieterEditDialog extends Dialog {

    private final Binder<Mieter> binder = new Binder<>(Mieter.class);

    public MieterEditDialog(MieterService mieterService, Mieter mieter, Runnable onSuccess, ConfigurationService configurationService) {
        //TODO darkmode mit configurationService
        //super(configurationService);

        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Name");
        nameField.setWidthFull();
        binder.forField(nameField).asRequired("Name ist erforderlich")
                .withValidator(new RegexpValidator("Name darf nur Buchstaben enthalten", "^[\\p{L}äöüÄÖÜß\\s\\-]+$", true))
                .bind(Mieter::getName, Mieter::setName);

        TextField vornameField = new TextField("Vorname");
        binder.forField(vornameField).asRequired("Vorname ist erforderlich")
                .withValidator(new RegexpValidator("Vorname darf nur Buchstaben enthalten", "^[\\p{L}äöüÄÖÜß\\s\\-]+$", true))
                .bind(Mieter::getVorname, Mieter::setVorname);

        TextField telefonnummerField = new TextField("Telefonnummer");
        binder.forField(telefonnummerField).asRequired("Telefonnummer ist erforderlich")
                .withValidator(new RegexpValidator("Telefonnummer darf nur Zahlen enthalten", "^[0-9]*$", true))
                .bind(Mieter::getTelefonnummer, Mieter::setTelefonnummer);

        TextField emailField = new TextField("Email");
        binder.forField(emailField).asRequired("Email ist erforderlich")
                .withValidator(new RegexpValidator("Ungültige E-Mail-Adresse", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", true))
                .bind(Mieter::getEmail, Mieter::setEmail);

        NumberField einkommenField = new NumberField("Einkommen");
        binder.forField(einkommenField).asRequired("Einkommen ist erforderlich")
                .withValidator(new DoubleRangeValidator("Einkommen muss positiv sein", 0.1 , 1000000000.0))
                .bind(Mieter::getEinkommen, Mieter::setEinkommen);

        // Aktuelle Wohnung in den Binder einlesen
        binder.readBean(mieter);

        formLayout.add(nameField, vornameField, telefonnummerField, emailField, einkommenField);

        Button saveButton = new Button("Speichern", event -> saveMieter(mieter, mieterService, onSuccess));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Abbrechen", event -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Tastenkürzel für Enter-Taste hinzufügen
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        add(formLayout, saveButton, cancelButton);

        // Aktivieren der Schaltfläche "Speichern" nur, wenn der Binder gültig ist
        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        // Größe des Dialogs festlegen
        setWidth("450px");
        setHeight("475px");

        // Speichern-Callback nach dem Schließen des Dialogs hinzufügen
        addDialogCloseActionListener(e -> onSuccess.run());
    }

    private void saveMieter(Mieter mieter, MieterService mieterService, Runnable onSuccess) {
        if (binder.writeBeanIfValid(mieter)) {
            mieterService.saveMieter(mieter);
            onSuccess.run();
            this.close();
            NotificationPopup.showSuccessNotification("Mieter gespeichert.");
        } else {
            NotificationPopup.showWarningNotification("Bitte füllen Sie alle erforderlichen Felder aus.");
        }
    }
}
