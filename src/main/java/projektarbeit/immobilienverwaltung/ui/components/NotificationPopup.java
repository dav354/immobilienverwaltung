package projektarbeit.immobilienverwaltung.ui.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationPopup {

    public static void showSuccessNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static void showWarningNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    public static void showInfoNotification(String message) {
        Notification notification = Notification.show(message, 3000, Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }
}