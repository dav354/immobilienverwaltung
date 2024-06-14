package projektarbeit.immobilienverwaltung.ui.views.Login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import projektarbeit.immobilienverwaltung.model.Role;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;
import projektarbeit.immobilienverwaltung.service.SecurityService;
import projektarbeit.immobilienverwaltung.service.UserService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse stellt die Admin-Ansicht dar, die es einem Administrator ermöglicht, Benutzer zu verwalten.
 * Die Klasse erlaubt das Hinzufügen, Löschen und Ändern von Passwörtern für Benutzer, die vom aktuellen Administrator erstellt wurden.
 */

@Route(value = "admin", layout = MainLayout.class)
@PermitAll
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private Grid<User> userGrid;

    /**
     * Konstruktor für die AdminView-Klasse.
     *
     * @param userService      der UserService zur Verwaltung der Benutzer
     * @param roleRepository   das RoleRepository zur Verwaltung der Rollen
     * @param securityService  der SecurityService zur Handhabung der Sicherheitsfunktionen
     */

    @Autowired
    public AdminView(UserService userService, RoleRepository roleRepository, SecurityService securityService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.securityService = securityService;

        if (userService == null || roleRepository == null || securityService == null) {
            throw new IllegalArgumentException("userService, roleRepository, and securityService cannot be null");
        }

        // Erstellen des Headers mit dem Button zum Ändern des eigenen Passworts
        Button changeOwnPasswordButton = new Button("Change Own Password", event -> openChangePasswordDialog(getCurrentAdmin()));
        changeOwnPasswordButton.getStyle().set("margin-left", "auto");

        HorizontalLayout headerLayout = new HorizontalLayout(changeOwnPasswordButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Header zum Layout hinzufügen
        add(headerLayout);

        // Erstellen des Titels
        User currentAdmin = getCurrentAdmin();
        Label titleLabel = new Label("Admin Panel - " + currentAdmin.getUsername());
        titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold");
        titleLabel.getStyle().set("text-align", "center");
        titleLabel.setWidthFull();

        HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Titel zum Layout hinzufügen
        add(titleLayout);

        FormLayout formLayout = new FormLayout();

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        ComboBox<Role> roleComboBox = new ComboBox<>("Role");
        List<Role> roles = roleRepository.findAll();
        roleComboBox.setItems(roles);
        roleComboBox.setItemLabelGenerator(Role::getName);

        Button addButton = new Button("Add User");
        addButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            Role selectedRole = roleComboBox.getValue();

            try {
                userService.validateUsername(username);
                userService.validatePassword(password, username);

                if (selectedRole == null) {
                    Notification.show("Role must not be empty");
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
                Notification.show("User added successfully");

                // Felder leeren
                usernameField.clear();
                passwordField.clear();
                roleComboBox.clear();
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage());
            }
        });

        formLayout.add(usernameField, passwordField, roleComboBox, addButton);
        add(formLayout);

        // Erstellen des Benutzergrids
        userGrid = new Grid<>(User.class);
        userGrid.setColumns(); // Clear existing columns
        userGrid.addColumn(User::getUsername).setHeader("Username");
        userGrid.addColumn(new ComponentRenderer<>(user -> new Label(
                user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "))
        ))).setHeader("Roles");
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                boolean deleted = userService.deleteUser(user);
                if (deleted) {
                    updateUserGrid();
                    Notification.show("User deleted successfully");
                } else {
                    Notification.show("Cannot delete the last admin", 3000, Notification.Position.MIDDLE);
                }
            });

            Button updatePasswordButton = new Button("Change Password");
            updatePasswordButton.setEnabled(user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN")));
            updatePasswordButton.addClickListener(e -> {
                // Dialog zum Ändern des Passworts öffnen
                openChangePasswordDialog(user);
            });

            actions.add(deleteButton, updatePasswordButton);
            return actions;
        })).setHeader("Actions");
        add(userGrid);

        // Benutzer in das Grid laden
        updateUserGrid();
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
     * Aktualisiert das Benutzergrid mit den vom aktuellen Admin erstellten Benutzern.
     */
    private void updateUserGrid() {
        User currentAdmin = getCurrentAdmin();
        List<User> users = userService.findUsersCreatedByAdmin(currentAdmin);
        users.add(currentAdmin); // Add the current admin to the list
        userGrid.setItems(users);
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
                userService.validatePassword(newPassword, user.getUsername());
                userService.updatePassword(user, newPassword);
                Notification.show("Password changed successfully for user: " + user.getUsername());
                dialog.close();
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage());
            }
        });

        dialogLayout.add(newPasswordField, changeButton);
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