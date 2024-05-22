package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

public class MieterForm extends FormLayout {

    //Datenbindung und Validierung zwischen UI-Komponenten und einem Java-Bean-Objekt zu erstellen
    Binder<Mieter> binder = new BeanValidationBinder<>(Mieter.class);

    // Die einzelnen Daten mit ihren Feldern
    TextField name = new TextField("Name");
    TextField vorname = new TextField("Vorname");
    TextField telefonnummer = new TextField("Telefonnummer");
    NumberField einkommen = new NumberField("Einkommen"); // Hier NumberField verwenden
    DatePicker mietbeginn = new DatePicker("Mietbeginn");
    DatePicker mietende = new DatePicker("Mietende");
    NumberField kaution = new NumberField("Kaution");
    NumberField anzahlBewohner = new NumberField("Anzahl Bewohner");
    ComboBox <Wohnung> wohnungComboBox = new ComboBox<>("Wohnung");


    // Die 3 Knöpfe zum Speichern, Löschen und Schließen des Forms
    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button schliessen = new Button("Schließen");
    private Mieter mieter;

    //Erstellung des Forms
    public MieterForm(List<Wohnung> wohnungen) {
        addClassName("mieter-form");

        binder.bindInstanceFields(this);

        wohnungComboBox.setItems(wohnungen);
        wohnungComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);

        add(name,
                vorname,
                telefonnummer,
                einkommen,
                mietbeginn,
                mietende,
                kaution,
                anzahlBewohner,
                wohnungComboBox,
                createButtonsLayout());
    }

    //Die Mieter zum Binder binden
    public void setMieter(Mieter mieter) {
        if (mieter != null) {
            this.mieter = mieter;
            binder.readBean(mieter);
        }
    }

    //Erstellt und konfiguriert eines Layout für die Buttons Speichern, Löschen und Schließen
    private HorizontalLayout createButtonsLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> validateAndSave());
        loeschen.addClickListener(event -> fireEvent(new DeleteEvent(this, mieter)));
        schliessen.addClickListener(event -> fireEvent(new CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));
        return new HorizontalLayout(speichern, loeschen, schliessen);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    //Validiert die Eingaben im Formular und speichert die Daten, falls sie gültig sind
    private void validateAndSave() {
        try{
            binder.writeBean(mieter);
            fireEvent(new SaveEvent(this, mieter));
        }catch (ValidationException e){
            e.printStackTrace();
        }
    }

    // Events für die Buttons Speichern, Löschen und Schließen
    public static abstract class ContactFormEvent extends ComponentEvent<MieterForm> {
        private final Mieter mieter;

        //Konstruktor für das ContactFormEvent
        protected ContactFormEvent(MieterForm source, Mieter mieter) {
            super(source, false);
            this.mieter = mieter;
        }

        public Mieter getContact() {
            return mieter;
        }
    }

    //Ereignis, das ausgelöst wird, wenn ein Mieter gespeichert wird
    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    //Ereignis, das ausgelöst wird, wenn ein Mieter gelöscht wird
    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(MieterForm source, Mieter mieter) {
            super(source, mieter);
        }
    }

    //Ereignis, das ausgelöst wird, wenn das Formular geschlossen wird
    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(MieterForm source) {
            super(source, null);
        }
    }

    //Fügt einen Listener für die angegebene Ereignis hinzu (Speichern, Löschen und Schließen)
    public <T extends  ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }

}