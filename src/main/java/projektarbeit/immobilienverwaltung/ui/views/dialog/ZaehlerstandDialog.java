package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.service.ZaehlerstandService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

/**
 * A dialog for editing a Zaehlerstand (meter reading).
 */
public class ZaehlerstandDialog extends Dialog {

    private final Binder<Zaehlerstand> binder = new Binder<>(Zaehlerstand.class);

    /**
     * Constructs a new ZaehlerstandDialog.
     *
     * @param zaehlerstandService the service to handle Zaehlerstand operations
     * @param zaehlerstand        the Zaehlerstand to be edited
     * @param onSave              the callback to execute after saving the Zaehlerstand
     */
    public ZaehlerstandDialog(ZaehlerstandService zaehlerstandService, Zaehlerstand zaehlerstand, Runnable onSave) {

        // Create the form layout
        FormLayout formLayout = new FormLayout();

        // Create and bind fields
        TextField nameField = new TextField("Name");
        binder.forField(nameField).asRequired("Name is required")
                .bind(Zaehlerstand::getName, Zaehlerstand::setName);

        DatePicker ablesedatumField = new DatePicker("Ablesedatum");
        binder.forField(ablesedatumField).asRequired("Ablesedatum is required")
                .bind(Zaehlerstand::getAblesedatum, Zaehlerstand::setAblesedatum);

        NumberField ablesewertField = new NumberField("Ablesewert");
        binder.forField(ablesewertField).asRequired("Ablesewert is required")
                .bind(Zaehlerstand::getAblesewert, Zaehlerstand::setAblesewert);

        // Read the current Zaehlerstand details into the binder
        binder.readBean(zaehlerstand);

        // Add fields to the form layout
        formLayout.add(nameField, ablesedatumField, ablesewertField);

        // Create save button with save logic
        Button saveButton = new Button("Save", event -> {
            if (binder.writeBeanIfValid(zaehlerstand)) {
                zaehlerstandService.saveOrUpdateZaehlerstand(zaehlerstand);
                onSave.run();
                this.close();
                NotificationPopup.showSuccessNotification("Zaehlerstand saved.");
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
        setHeight("320px");

        // Add keyboard shortcut for Enter key
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
    }
}