package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;

public class VertragHinzufuegenDialog extends Dialog {

    private final Binder<Mietvertrag> binder = new Binder<>(Mietvertrag.class);

    public VertragHinzufuegenDialog(Mieter mieter, MietvertragService mietvertragService, Runnable onSuccess, ConfigurationService configurationService, WohnungService wohnungService) {

        Mietvertrag mietvertrag = new Mietvertrag();

        //TODO darkmode mit configurationService
        //super(configurationService);

        FormLayout formLayout = new FormLayout();

        ComboBox<Wohnung> wohnungComboBox = new ComboBox<>("Wohnung");
        wohnungComboBox.setItems(wohnungService.findAllWohnungen());
        wohnungComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);
        binder.forField(wohnungComboBox).asRequired("Wohnung ist erforderlich")
                .bind(Mietvertrag::getWohnung,Mietvertrag::setWohnung);

        NumberField miete = new NumberField("Miete");
        binder.forField(miete).asRequired("Miete muss positiv sein")
                .withValidator(new DoubleRangeValidator("Miete muss positiv sein", 0.1, Double.MAX_VALUE))
                .bind(Mietvertrag::getMiete, Mietvertrag::setMiete);

        DatePicker mietbeginn = new DatePicker("Mietbeginn");
        binder.forField(mietbeginn)
                .bind(Mietvertrag::getMietbeginn, Mietvertrag::setMietbeginn);

        DatePicker mietende = new DatePicker("Mietende");
        binder.forField(mietende).asRequired("Mietende nicht vorhanden")
                .withValidator((value, context) -> {
                    if (value == null) {
                        return ValidationResult.ok();
                    }
                    return value.isAfter(mietbeginn.getValue()) ? ValidationResult.ok() : ValidationResult.error("Mietende must be after Mietbeginn");
                })
                .bind(Mietvertrag::getMietende, Mietvertrag::setMietende);

        NumberField kaution = new NumberField("Kaution");
        binder.forField(kaution).asRequired("Kaution muss positiv sein")
                .withValidator(new DoubleRangeValidator("Kaution muss positiv sein", 0.1, Double.MAX_VALUE))
                .bind(Mietvertrag::getKaution, Mietvertrag::setKaution);

        IntegerField anzahlBewohner = new IntegerField("Anzahl Bewohner");
        binder.forField(anzahlBewohner).asRequired("Anzahl Bewohner muss null oder positiv sein")
                .withValidator(new IntegerRangeValidator("Anzahl Bewohner muss null oder positiv sein", 0, Integer.MAX_VALUE))
                .bind(Mietvertrag::getAnzahlBewohner, Mietvertrag::setAnzahlBewohner);

        // Aktuelle Werte in den Binder einlesen
        binder.readBean(mietvertrag);

        formLayout.add(wohnungComboBox, miete, mietbeginn, mietende, kaution, anzahlBewohner);

        Button saveButton = new Button("Speichern", event -> saveMietvertrag(mieter,mietvertrag, mietvertragService, onSuccess));
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
        setHeight("550px");

        // Speichern-Callback nach dem Schließen des Dialogs hinzufügen
        addDialogCloseActionListener(e -> onSuccess.run());
    }

    private void saveMietvertrag(Mieter currentMieter,Mietvertrag mietvertrag, MietvertragService mietvertragService, Runnable onSuccess) {
        // Neue Instanz von Mietvertrag erstellen
        Mietvertrag newMietvertrag = new Mietvertrag();

        // Daten aus Binder in den neuen Mietvertrag übertragen, wenn gültig
        if (binder.writeBeanIfValid(mietvertrag)) {
            // Sicherstellen, dass binder.getBean() nicht null ist
            Mietvertrag bean = binder.getBean();
            if (bean != null) {
                // Werte aus dem Binder in den neuen Mietvertrag übertragen
                newMietvertrag.setMieter(bean.getMieter());
                newMietvertrag.setWohnung(bean.getWohnung());
                newMietvertrag.setMietbeginn(bean.getMietbeginn());
                newMietvertrag.setMietende(bean.getMietende());
                newMietvertrag.setMiete(bean.getMiete());
                newMietvertrag.setKaution(bean.getKaution());
                newMietvertrag.setAnzahlBewohner(bean.getAnzahlBewohner());

                // Mietvertrag speichern
                mietvertragService.createAndSaveMietvertrag(
                        newMietvertrag.getMieter(),
                        newMietvertrag.getWohnung(),
                        newMietvertrag.getMietbeginn(),
                        newMietvertrag.getMietende(),
                        newMietvertrag.getMiete(),
                        newMietvertrag.getKaution(),
                        newMietvertrag.getAnzahlBewohner());

                onSuccess.run();
                this.close();
                NotificationPopup.showSuccessNotification("Mietvertrag gespeichert");
            } else {
                NotificationPopup.showWarningNotification("Fehler beim Zugriff auf die Eingabedaten.");
            }
        } else {
            NotificationPopup.showWarningNotification("Bitte füllen Sie alle erforderlichen Felder aus.");
        }
    }
}
