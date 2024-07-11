package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Dienstprogrammklasse zum Anzeigen verschiedener Arten von Benachrichtigungen in einer Vaadin-Anwendung.
 * Bietet Methoden zum Anzeigen von Erfolgs-, Fehler-, Warn- und Informationsbenachrichtigungen.
 */
public class NotificationPopup {

    /**
     * Zeigt eine Erfolgsbenachrichtigung mit der angegebenen Nachricht an.
     *
     * @param message Die Nachricht, die in der Benachrichtigung angezeigt werden soll.
     */
    public static void showSuccessNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Zeigt eine Fehlerbenachrichtigung mit der angegebenen Nachricht an.
     *
     * @param message Die Nachricht, die in der Benachrichtigung angezeigt werden soll.
     */
    public static void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Zeigt eine Warnbenachrichtigung mit der angegebenen Nachricht an.
     *
     * @param message Die Nachricht, die in der Benachrichtigung angezeigt werden soll.
     */
    public static void showWarningNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    /**
     * Zeigt eine Informationsbenachrichtigung mit der angegebenen Nachricht an.
     *
     * @param message Die Nachricht, die in der Benachrichtigung angezeigt werden soll.
     */
    public static void showInfoNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }
}