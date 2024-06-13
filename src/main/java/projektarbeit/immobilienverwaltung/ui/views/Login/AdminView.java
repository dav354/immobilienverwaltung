package projektarbeit.immobilienverwaltung.ui.views.Login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route("admin")
@PermitAll
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private Grid<User> userGrid;

    @Autowired
    public AdminView(UserService userService, RoleRepository roleRepository, SecurityService securityService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.securityService = securityService;

        if (userService == null || roleRepository == null || securityService == null) {
            throw new IllegalArgumentException("userService, roleRepository, and securityService cannot be null");
        }

        // Create the header with the back to home button
        Anchor backToHome = new Anchor("/", "Back to Home");
        backToHome.getStyle().set("margin-right", "auto");

        HorizontalLayout headerLayout = new HorizontalLayout(backToHome);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.START);

        // Add header to the layout
        add(headerLayout);

        // Create the title
        Label titleLabel = new Label("Admin Panel");
        titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold");
        titleLabel.getStyle().set("text-align", "center");
        titleLabel.setWidthFull();

        HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Add title to the layout
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

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(selectedRole);
                newUser.setRoles(userRoles);

                userService.saveUser(newUser);
                updateUserGrid();
                Notification.show("User added successfully");

                // Clear the fields
                usernameField.clear();
                passwordField.clear();
                roleComboBox.clear();
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage());
            }
        });

        formLayout.add(usernameField, passwordField, roleComboBox, addButton);
        add(formLayout);

        // Create the user grid
        userGrid = new Grid<>(User.class);
        userGrid.setColumns(); // Clear existing columns
        userGrid.addColumn(User::getUsername).setHeader("Username");
        userGrid.addColumn(new ComponentRenderer<>(user -> new Label(
                user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "))
        ))).setHeader("Roles");
        userGrid.addColumn(new ComponentRenderer<>(user -> {
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
            return deleteButton;
        })).setHeader("Actions");
        add(userGrid);

        // Load users into the grid
        updateUserGrid();
    }

    private void updateUserGrid() {
        List<User> users = userService.findAllUsers();
        userGrid.setItems(users);
    }

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
