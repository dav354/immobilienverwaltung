package projektarbeit.immobilienverwaltung.ui.views.dialog;

import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.ui.layout.ConfirmDialogLayout;

/**
 * Ein Bestätigungsdialog für Vaadin-Anwendungen.
 * Dieser Dialog zeigt eine Nachricht an und bietet Schaltflächen für "Löschen" und "Abbrechen".
 * Wenn die "Löschen"-Schaltfläche gedrückt wird, wird der bereitgestellte ConfirmationListener ausgelöst.
 */
public class ConfirmationDialog extends ConfirmDialogLayout {

    /**
     * Konstruktor für ConfirmationDialog mit der angegebenen Nachricht und dem Bestätigungslistener.
     *
     * @param title                Der Titel des Dialogs.
     * @param message              Die Nachricht, die im Dialog angezeigt werden soll.
     * @param confirmationListener Der Listener, der ausgelöst wird, wenn die "Löschen"-Schaltfläche gedrückt wird.
     * @param configurationService Der Dienst für Konfigurationseinstellungen.
     */
    public ConfirmationDialog(String title, String message, ConfirmationListener confirmationListener, ConfigurationService configurationService) {
        super(configurationService);
        setHeader(title);
        setText(message);

        setRejectable(true);
        setRejectText("Abbrechen");

        setConfirmText("Löschen");
        addConfirmListener(event -> {
            confirmationListener.onConfirm();
            close();
        });
    }

    /**
     * Eine Schnittstelle zum Handhaben von Bestätigungsaktionen.
     */
    public interface ConfirmationListener {
        void onConfirm();
    }
}