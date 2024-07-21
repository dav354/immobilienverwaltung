package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.ui.layout.DialogLayout;

/**
 * Ein Bestätigungsdialog für Vaadin-Anwendungen.
 * Dieser Dialog zeigt eine Nachricht an und bietet Schaltflächen für "Löschen" und "Abbrechen".
 * Wenn die "Löschen"-Schaltfläche gedrückt wird, wird der bereitgestellte ConfirmationListener ausgelöst.
 */
public class ConfirmationDialog extends DialogLayout {

    /**
     * Eine Schnittstelle zum Handhaben von Bestätigungsaktionen.
     */
    public interface ConfirmationListener {
        void onConfirm();
    }

    /**
     * Konstruktor für ConfirmationDialog mit der angegebenen Nachricht und dem Bestätigungslistener.
     *
     * @param message                Die Nachricht, die im Dialog angezeigt werden soll.
     * @param confirmationListener   Der Listener, der ausgelöst wird, wenn die "Löschen"-Schaltfläche gedrückt wird.
     * @param configurationService   Der Dienst für Konfigurationseinstellungen.
     */
    public ConfirmationDialog(String message, ConfirmationListener confirmationListener, ConfigurationService configurationService) {
        super(configurationService);
        // Setzen des Dialog-Layouts
        setDialogLayout();

        // Hauptlayout für den Dialog erstellen
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);

        // Hinzufügen des Nachrichtentexts zum Hauptlayout
        mainLayout.add(new Text(message));

        // Erstellen der "Löschen"-Schaltfläche und Hinzufügen eines Klick-Listeners zum Auslösen des Bestätigungslisteners
        Button confirmButton = new Button("Löschen", event -> {
            confirmationListener.onConfirm();
            close(); // Dialog nach Bestätigung schließen
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR); // Hinzufügen des Fehlerthemas zur "Löschen"-Schaltfläche

        // Erstellen der "Abbrechen"-Schaltfläche und Hinzufügen eines Klick-Listeners zum Schließen des Dialogs
        Button cancelButton = new Button("Abbrechen", event -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY); // Hinzufügen des tertiären Themas zur "Abbrechen"-Schaltfläche

        // Anordnen der Schaltflächen in einem horizontalen Layout
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Schaltflächenlayout zum Hauptlayout hinzufügen
        mainLayout.add(buttonLayout);
        mainLayout.setSizeFull();
        mainLayout.expand(buttonLayout);

        // Hauptlayout dem Dialog hinzufügen
        add(mainLayout);
    }

    /**
     * Einrichten des Layouts für den Dialog.
     */
    private void setDialogLayout() {
        setWidth("400px");  // Breite des Dialogs festlegen
        setHeight("200px"); // Höhe des Dialogs festlegen
    }
}