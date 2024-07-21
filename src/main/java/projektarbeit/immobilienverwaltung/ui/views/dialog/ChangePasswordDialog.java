package projektarbeit.immobilienverwaltung.ui.views.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.UserService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.DialogLayout;

/**
 * Öffnet einen Dialog zum Ändern des Passworts des aktuellen Benutzers.
 */
public class ChangePasswordDialog extends DialogLayout {

    private final UserService userService;
    private final Binder<User> binder = new Binder<>(User.class);

    public ChangePasswordDialog(ConfigurationService configurationService, UserService userService) {
        super(configurationService);
        this.userService = userService;
        createDialog();
    }

    private void createDialog() {
        // Erstellen des Formularlayouts
        FormLayout formLayout = new FormLayout();

        // Erstellen und Binden der Felder
        PasswordField newPasswordField = new PasswordField("Neues Passwort");
        binder.forField(newPasswordField)
                .asRequired("Neues Passwort ist erforderlich")
                .withValidator(password -> password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}"),
                        "Das Passwort muss 8-20 Zeichen lang sein, Groß- und Kleinbuchstaben, eine Zahl und ein Sonderzeichen (@#$%^&+=!) enthalten.")
                .bind(user -> "", (user, password) -> {
                }); // Dummy-Bindung

        PasswordField confirmPasswordField = new PasswordField("Passwort bestätigen");
        binder.forField(confirmPasswordField)
                .asRequired("Bitte bestätigen Sie das Passwort")
                .withValidator(password -> password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}"),
                        "Das Passwort muss 8-20 Zeichen lang sein, Groß- und Kleinbuchstaben, eine Zahl und ein Sonderzeichen (@#$%^&+=!) enthalten.")
                .withValidator(confirmPassword -> confirmPassword.equals(newPasswordField.getValue()), "Passwörter stimmen nicht überein")
                .bind(user -> "", (user, password) -> {
                }); // Dummy-Bindung

        // Felder zum Formularlayout hinzufügen
        formLayout.add(newPasswordField, confirmPasswordField);

        // Erstellen der Schaltfläche "Speichern" mit Logik zum Speichern
        Button saveButton = new Button("Ändern", event -> changePassword(newPasswordField.getValue(), this));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);

        // Erstellen der Schaltfläche "Abbrechen"
        Button cancelButton = new Button("Abbrechen", event -> this.close());
        cancelButton.addClickShortcut(Key.ESCAPE);

        // Layout für Schaltflächen erstellen
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Formularlayout und Schaltflächen zum Dialog hinzufügen
        add(formLayout, buttonsLayout);
        open();
    }

    private void changePassword(String newPassword, Dialog dialog) {
        try {
            userService.validatePassword(newPassword);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                    new IllegalStateException("Aktueller Benutzer nicht gefunden"));
            userService.updatePassword(currentUser, newPassword);
            NotificationPopup.showSuccessNotification("Passwort erfolgreich geändert");
            dialog.close();
        } catch (IllegalArgumentException e) {
            NotificationPopup.showErrorNotification(e.getMessage());
        }
    }
}