package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Utility class for displaying different types of notifications in a Vaadin application.
 * Provides methods to show success, error, warning, and info notifications.
 */
public class NotificationPopup {

    /**
     * Displays a success notification with the specified message.
     *
     * @param message The message to display in the notification.
     */
    public static void showSuccessNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Displays an error notification with the specified message.
     *
     * @param message The message to display in the notification.
     */
    public static void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Displays a warning notification with the specified message.
     *
     * @param message The message to display in the notification.
     */
    public static void showWarningNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    /**
     * Displays an info notification with the specified message.
     *
     * @param message The message to display in the notification.
     */
    public static void showInfoNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }
}