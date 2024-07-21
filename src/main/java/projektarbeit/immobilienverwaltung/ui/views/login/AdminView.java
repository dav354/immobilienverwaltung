package projektarbeit.immobilienverwaltung.ui.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import projektarbeit.immobilienverwaltung.model.Role;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.SecurityService;
import projektarbeit.immobilienverwaltung.service.UserService;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static projektarbeit.immobilienverwaltung.ui.components.TableUtils.createCustomHeader;

/**
 * Diese Klasse stellt die Admin-Ansicht dar, die es einem Administrator ermöglicht, Benutzer zu verwalten.
 * Die Klasse erlaubt das Hinzufügen, Löschen und Ändern von Passwörtern für Benutzer, die vom aktuellen Administrator erstellt wurden.
 */

@Route(value = "admin", layout = MainLayout.class)
@PermitAll
@Secured("ROLE_ADMIN")
@PageTitle("Admin Panel")
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final Grid<User> userGrid;

    /**
     * Konstruktor für die AdminView-Klasse.
     *
     * @param userService     der UserService zur Verwaltung der Benutzer
     * @param roleRepository  das RoleRepository zur Verwaltung der Rollen
     * @param securityService der SecurityService zur Handhabung der Sicherheitsfunktionen
     */

    @Autowired
    public AdminView(UserService userService, RoleRepository roleRepository, SecurityService securityService, ConfigurationService configurationService) {
        this.userService = userService;

        if (userService == null || roleRepository == null || securityService == null) {
            throw new IllegalArgumentException("userService, roleRepository, and securityService cannot be null");
        }

        // Erstellen des Titels
        User currentAdmin = getCurrentAdmin();
        H1 titleLabel = new H1("Admin Panel");
        titleLabel.setWidthFull();

        HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.START);

        // Titel zum Layout hinzufügen
        add(titleLayout);

        FormLayout formLayout = new FormLayout();

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPattern("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}");
        passwordField.setErrorMessage("Password must be 8-20 characters long, include uppercase and lowercase letters, a number, and a special character (@#$%^&+=!).");

        ComboBox<Role> roleComboBox = new ComboBox<>("Role");
        List<Role> roles = roleRepository.findAll();
        roleComboBox.setItems(roles);
        roleComboBox.setItemLabelGenerator(Role::getName);

        Button addButton = new Button("Add User");
        addButton.setPrefixComponent(VaadinIcon.PLUS.create());
        addButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            Role selectedRole = roleComboBox.getValue();

            try {
                userService.validateUsername(username);
                userService.validatePassword(password);

                if (selectedRole == null) {
                    NotificationPopup.showWarningNotification("Role must not be empty");
                    return;
                }

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setCreatedByAdmin(currentAdmin);

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(selectedRole);
                newUser.setRoles(userRoles);

                userService.saveUser(newUser, currentAdmin);
                updateUserGrid();
                NotificationPopup.showSuccessNotification("User added successfully");

                // Felder leeren
                usernameField.clear();
                passwordField.clear();
                roleComboBox.clear();
            } catch (IllegalArgumentException e) {
                NotificationPopup.showErrorNotification(e.getMessage());
            }
        });

        formLayout.add(usernameField, passwordField, roleComboBox, addButton);
        add(formLayout);

        // Erstellen des Benutzergrids
        userGrid = new Grid<>(User.class);
        userGrid.setColumns(); // Clear existing columns
        userGrid.addColumn(User::getUsername).setHeader(createCustomHeader("Username"));
        userGrid.addColumn(new ComponentRenderer<>(user -> new Span(
                user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "))
        ))).setHeader(createCustomHeader("Roles"));
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button deleteButton = getDeleteButton(userService, configurationService, user);

            actions.add(deleteButton);

            // Prüfen, ob der Benutzer nicht die Rolle "ADMIN" hat
            boolean isNotAdmin = user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"));
            if (isNotAdmin) {
                Button updatePasswordButton = new Button("Change Password");
                updatePasswordButton.addClickListener(e -> {
                    // Dialog zum Ändern des Passworts öffnen
                    openChangePasswordDialog(user);
                });
                actions.add(updatePasswordButton);
            }

            return actions;
        })).setHeader(createCustomHeader("Actions"));

        add(userGrid);

        // Benutzer in das Grid laden
        updateUserGrid();
    }

    private Button getDeleteButton(UserService userService, ConfigurationService configurationService, User user) {
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(e -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    "Löschen",
                    "Are you sure you want to delete the user " + user.getUsername() + "?",
                    () -> {
                        boolean deleted = userService.deleteUser(user);
                        if (deleted) {
                            updateUserGrid();
                            NotificationPopup.showSuccessNotification("User deleted successfully");
                        } else {
                            NotificationPopup.showErrorNotification("Cannot delete the last admin");
                        }
                    },
                    configurationService
            );
            confirmationDialog.open();
        });
        return deleteButton;
    }

    /**
     * Holt den aktuellen angemeldeten Admin-Benutzer.
     *
     * @return der aktuelle Admin-Benutzer
     */
    private User getCurrentAdmin() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                new IllegalStateException("Admin user not found"));
    }

    /**
     * Aktualisiert das Benutzergrid mit den vom aktuellen Admin erstellten Benutzern,
     * und passt die Größe der Tabelle automatisch an.
     */
    private void updateUserGrid() {
        User currentAdmin = getCurrentAdmin();
        List<User> users = userService.findUsersCreatedByAdmin(currentAdmin);
        users.add(currentAdmin); // Add the current admin to the list

        // Verwenden von TableUtils zur Konfiguration des Grids
        TableUtils.configureGrid(userGrid, users, 60);
    }

    /**
     * Öffnet einen Dialog zum Ändern des Passworts eines Benutzers.
     *
     * @param user der Benutzer, dessen Passwort geändert werden soll
     */
    private void openChangePasswordDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);

        PasswordField newPasswordField = new PasswordField("New Password");
        Button changeButton = new Button("Change", event -> {
            String newPassword = newPasswordField.getValue();
            try {
                userService.validatePassword(newPassword);
                userService.updatePassword(user, newPassword);
                NotificationPopup.showSuccessNotification("Password changed successfully for " + user.getUsername());
                dialog.close();
            } catch (IllegalArgumentException ex) {
                NotificationPopup.showErrorNotification(ex.getMessage());
            }
        });

        // Cancel Button hinzufügen
        Button cancelButton = new Button("Cancel", event -> dialog.close());

        // Layout für Buttons erstellen
        HorizontalLayout buttonsLayout = new HorizontalLayout(changeButton, cancelButton);

        dialogLayout.add(newPasswordField, buttonsLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Methode, die vor dem Betreten der Ansicht ausgeführt wird, um zu überprüfen, ob der Benutzer ein Admin ist.
     *
     * @param event das BeforeEnterEvent
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            event.forwardTo("");
        }
    }
}