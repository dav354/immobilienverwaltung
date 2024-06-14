package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import projektarbeit.immobilienverwaltung.service.SecurityService;
import projektarbeit.immobilienverwaltung.ui.views.MainView;
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterListView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungListView;

import java.util.ArrayList;
import java.util.List;

public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private boolean isDarkMode = true; // Dark mode enabled by default
    private final List<RouterLink> navLinks = new ArrayList<>();
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
        createFooter();
        enableDarkMode();
    }

    private void createHeader() {
        String currentUsername = getCurrentUsername();
        DrawerToggle toggle = new DrawerToggle();
        Div title = new Div();
        title.setText("Immobilienverwaltung - "+ currentUsername);


        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(false);
        header.getStyle().set("padding", "10px");
        addToNavbar(header);
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    private void enableDarkMode() {
        getElement().getThemeList().add(Lumo.DARK);
    }

    private void createFooter() {
        Button logoutButton = new Button("Logout", event -> securityService.logout());
        logoutButton.addClassName("footer-button");

        Button toggleButton = new Button("Toggle Dark Mode", event -> toggleDarkMode());
        toggleButton.addClassName("footer-button");

        VerticalLayout footerLayout = new VerticalLayout(logoutButton, toggleButton);
        footerLayout.setSpacing(false);
        footerLayout.setPadding(true);
        footerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        footerLayout.setMargin(false);
        addToDrawer(footerLayout);
    }

    private void toggleDarkMode() {
        if (isDarkMode) {
            getElement().getThemeList().remove(Lumo.DARK);
        } else {
            getElement().getThemeList().add(Lumo.DARK);
        }
        isDarkMode = !isDarkMode;
    }

    private void createDrawer() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Span("Navigation"));
        layout.add(createNavigationLink("Home", MainView.class));
        layout.add(createNavigationLink("Wohnungen", WohnungListView.class));
        layout.add(createNavigationLink("Mieter", MieterListView.class));

        // Add Admin page link if the user is an ADMIN
        if (securityService.getAuthenticatedUserRoles().contains("ADMIN")) {
            layout.add(createNavigationLink("Admin", projektarbeit.immobilienverwaltung.ui.views.Login.AdminView.class));
        }

        layout.setWidthFull();
        layout.setSizeFull();
        layout.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
        layout.getStyle().set("padding", "10px");
        addToDrawer(layout);
    }

    private RouterLink createNavigationLink(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink();
        link.setText(text);
        link.setRoute(navigationTarget);
        link.getStyle().set("display", "block");
        link.getStyle().set("padding", "10px");
        link.getStyle().set("margin", "5px 0");
        link.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        link.getStyle().set("border-radius", "5px");
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("color", "var(--lumo-body-text-color)");
        link.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
        link.getStyle().set("width", "90%");
        navLinks.add(link); // Add link to the list
        return link;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String currentRoute = event.getLocation().getFirstSegment();
        navLinks.forEach(link -> {
            if (link.getHref().equals(currentRoute)) {
                link.getStyle().set("background-color", "var(--lumo-primary-color-10pct)");
                link.getStyle().set("color", "var(--lumo-body-text-color)");
            } else {
                link.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
                link.getStyle().set("color", "var(--lumo-body-text-color)");
            }
        });
    }
}
