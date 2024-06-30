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
        // Create the form layout
        FormLayout formLayout = new FormLayout();

        // Create and bind fields
        PasswordField newPasswordField = new PasswordField("New Password");
        binder.forField(newPasswordField)
                .asRequired("New password is required")
                .withValidator(password -> password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}"),
                        "Password must be 8-20 characters long, include uppercase and lowercase letters, a number, and a special character (@#$%^&+=!).")
                .bind(user -> "", (user, password) -> {}); // Dummy binding

        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        binder.forField(confirmPasswordField)
                .asRequired("Please confirm the password")
                .withValidator(password -> password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}"),
                        "Password must be 8-20 characters long, include uppercase and lowercase letters, a number, and a special character (@#$%^&+=!).")
                .withValidator(confirmPassword -> confirmPassword.equals(newPasswordField.getValue()), "Passwords do not match")
                .bind(user -> "", (user, password) -> {}); // Dummy binding

        // Add fields to the form layout
        formLayout.add(newPasswordField, confirmPasswordField);

        // Create save button with save logic
        Button saveButton = new Button("Change", event -> changePassword(newPasswordField.getValue(), this));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);

        // Create cancel button
        Button cancelButton = new Button("Cancel", event -> this.close());
        cancelButton.addClickShortcut(Key.ESCAPE);

        // Create a layout for buttons
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Add form layout and buttons to the dialog
        add(formLayout, buttonsLayout);
        open();
    }

    private void changePassword(String newPassword, Dialog dialog) {
        try {
            userService.validatePassword(newPassword);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                    new IllegalStateException("Current user not found"));
            userService.updatePassword(currentUser, newPassword);
            NotificationPopup.showSuccessNotification("Password changed successfully");
            dialog.close();
        } catch (IllegalArgumentException e) {
            NotificationPopup.showErrorNotification(e.getMessage());
        }
    }
}