package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import projektarbeit.immobilienverwaltung.service.SecurityService;
import projektarbeit.immobilienverwaltung.ui.views.login.AdminView;
import projektarbeit.immobilienverwaltung.ui.views.MainView;
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterListView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungListView;

/**
 * Hauptlayout der Anwendung, das die Navigation und das Layout der Anwendung definiert.
 */
public class MainLayout extends AppLayout {

    private boolean isDarkMode = true; // Dark mode standardmäßig aktiviert
    private final SecurityService securityService;

    /**
     * Konstruktor für das MainLayout.
     *
     * @param securityService der SecurityService zur Verwaltung der Sicherheitsfunktionen.
     */
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
        enableDarkMode();
    }

    /**
     * Erstellt die Kopfzeile des Layouts.
     */
    private void createHeader() {
        String currentUsername = getCurrentUsername();
        DrawerToggle toggle = new DrawerToggle();
        Div title = new Div();
        title.setText("Immobilienverwaltung - " + currentUsername);

        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(false);
        header.getStyle().set("padding", "10px");
        addToNavbar(header);
    }

    /**
     * Gibt den Benutzernamen des aktuellen Benutzers zurück.
     *
     * @return der Benutzername des aktuellen Benutzers.
     */
    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Aktiviert den Darkmode des Layouts.
     */
    private void enableDarkMode() {
        getElement().getThemeList().add(Lumo.DARK);
    }

    /**
     * Erstellt die Fußzeile des Layouts.
     */
    private VerticalLayout createFooter() {
        Button logoutButton = new Button("Logout", event -> securityService.logout());
        logoutButton.addClassName("footer-button");

        Button toggleButton = new Button("Toggle Dark Mode", event -> toggleDarkMode());
        toggleButton.addClassName("footer-button");

        VerticalLayout footerLayout = new VerticalLayout(logoutButton, toggleButton);
        footerLayout.setSpacing(false);
        footerLayout.setPadding(true);
        footerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        footerLayout.setMargin(false);
        return footerLayout;
    }

    /**
     * Wechselt den Darkmode des Layouts.
     */
    private void toggleDarkMode() {
        if (isDarkMode) {
            getElement().getThemeList().remove(Lumo.DARK);
        } else {
            getElement().getThemeList().add(Lumo.DARK);
        }
        isDarkMode = !isDarkMode;
    }

    /**
     * Erstellt das Navigationsmenü im Drawer.
     */
    private void createDrawer() {
        SideNav nav = new SideNav();

        SideNavItem home = new SideNavItem("Home", MainView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem wohnungen = new SideNavItem("Wohnungen", WohnungListView.class, VaadinIcon.HOME.create());
        SideNavItem mieter = new SideNavItem("Mieter", MieterListView.class, VaadinIcon.USER.create());

        nav.addItem(home, wohnungen, mieter);

        // Füge den Admin-Seiten-Link hinzu, wenn der Benutzer ein ADMIN ist
        if (securityService.getAuthenticatedUserRoles().contains("ADMIN")) {
            SideNavItem admin = new SideNavItem("Admin", AdminView.class, VaadinIcon.WRENCH.create());
            nav.addItem(admin);
        }

        nav.getElement().getStyle().set("width", "100%");

        // Add the nav and footer to a flex layout to push footer to the bottom
        VerticalLayout drawerContent = new VerticalLayout(nav, createFooter());
        drawerContent.setSizeFull();
        drawerContent.setFlexGrow(1, nav);
        drawerContent.setAlignItems(FlexComponent.Alignment.STRETCH);

        addToDrawer(drawerContent);
    }
}