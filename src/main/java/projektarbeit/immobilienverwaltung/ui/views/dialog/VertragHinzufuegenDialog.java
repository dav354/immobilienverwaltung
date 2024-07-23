package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import projektarbeit.immobilienverwaltung.ui.layout.DialogLayout;

/**
 * Diese Klasse stellt einen Dialog zum Hinzufügen eines Mietvertrags für einen Mieter dar.
 * Benutzer können eine Wohnung auswählen und Details wie Miete, Mietbeginn, Mietende, Kaution und Anzahl der Bewohner eingeben.
 * Nach dem Speichern wird der Mietvertrag zum Mieter hinzugefügt.
 */
public class VertragHinzufuegenDialog extends DialogLayout {

    private final Binder<Mietvertrag> binder = new Binder<>(Mietvertrag.class);

    /**
     * Konstruktor für den VertragHinzufuegenDialog. Erstellung des Dialogfensters mit seinen Eingabefeldern.
     *
     * @param mieter               Der Mieter, dem der Mietvertrag hinzugefügt wird.
     * @param mietvertragService   Der Service für Mietverträge.
     * @param onSuccess            Eine Callback-Funktion, die nach erfolgreichem Speichern ausgeführt wird.
     * @param configurationService Der Service für die Konfiguration.
     * @param wohnungService       Der Service für Wohnungen.
     */
    public VertragHinzufuegenDialog(Mieter mieter, MietvertragService mietvertragService, Runnable onSuccess, ConfigurationService configurationService, WohnungService wohnungService) {

        super(configurationService);

        Mietvertrag mietvertrag = new Mietvertrag();
        mietvertrag.setMieter(mieter); // Mieter setzen

        // Setze den Mietvertrag im Binder
        binder.setBean(mietvertrag);

        FormLayout formLayout = new FormLayout();

        ComboBox<Wohnung> wohnungComboBox = new ComboBox<>("Wohnung");
        wohnungComboBox.setItems(wohnungService.findWohnungenWithoutMietvertrag());
        wohnungComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);
        binder.forField(wohnungComboBox).asRequired("Wohnung ist erforderlich")
                .bind(Mietvertrag::getWohnung, Mietvertrag::setWohnung);

        NumberField miete = new NumberField("Miete");
        binder.forField(miete).asRequired("Miete muss positiv sein")
                .withValidator(new DoubleRangeValidator("Miete muss positiv sein", 0.1, Double.MAX_VALUE))
                .bind(Mietvertrag::getMiete, Mietvertrag::setMiete);

        DatePicker mietbeginn = new DatePicker("Mietbeginn");
        binder.forField(mietbeginn).asRequired("Mietbeginn nicht vorhanden")
                .bind(Mietvertrag::getMietbeginn, Mietvertrag::setMietbeginn);

        DatePicker mietende = new DatePicker("Mietende");
        binder.forField(mietende)
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
                .withValidator(new IntegerRangeValidator("Anzahl Bewohner muss null oder positiv sein", 1, Integer.MAX_VALUE))
                .bind(Mietvertrag::getAnzahlBewohner, Mietvertrag::setAnzahlBewohner);

        // Aktuelle Werte in den Binder einlesen
        binder.readBean(mietvertrag);

        formLayout.add(wohnungComboBox, miete, mietbeginn, mietende, kaution, anzahlBewohner);

        Button saveButton = new Button("Speichern", event -> saveMietvertrag(mietvertrag, mietvertragService, onSuccess));
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

    /**
     * Methode zum Speichern des Mietvertrags.
     *
     * @param mietvertrag        Der Mietvertrag, der gespeichert werden soll.
     * @param mietvertragService Der Service für Mietverträge.
     * @param onSuccess          Die Callback-Funktion, die nach erfolgreichem Speichern aufgerufen wird.
     */
    private void saveMietvertrag(Mietvertrag mietvertrag, MietvertragService mietvertragService, Runnable onSuccess) {
        // Daten aus Binder in den Mietvertrag übertragen, wenn gültig
        if (binder.writeBeanIfValid(mietvertrag)) {
            mietvertragService.saveMietvertrag(mietvertrag);
            NotificationPopup.showSuccessNotification("Mietvertrag gespeichert");
            onSuccess.run();
            this.close();
        } else {
            NotificationPopup.showWarningNotification("Bitte füllen Sie alle erforderlichen Felder aus.");
        }
    }
}