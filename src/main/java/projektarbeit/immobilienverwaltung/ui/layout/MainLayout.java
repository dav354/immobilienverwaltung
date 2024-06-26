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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
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
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private boolean isDarkMode = true; // Dark mode standardmäßig aktiviert
    private final SecurityService securityService;
    private HorizontalLayout header;

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

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateHeader();
    }

    /**
     * Erstellt die Kopfzeile des Layouts.
     */
    private void createHeader() {
        header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(false);
        header.getStyle().set("padding", "10px");

        updateHeader();

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

    /**
     * Aktualisiert die Kopfzeile des Layouts basierend auf der aktuellen Ansicht.
     */
    private void updateHeader() {
        header.removeAll();

        DrawerToggle toggle = new DrawerToggle();
        Div title = new Div();
        title.setText("Immobilienverwaltung");

        HorizontalLayout titleLayout = new HorizontalLayout(toggle, title);
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setSpacing(true);

        // Add the title layout to the header
        header.add(titleLayout);

        // Create the back button if needed
        if (isInSubview()) {
            Button backButton = new Button("Zurück", event -> getUI().ifPresent(ui -> ui.navigate(getPreviousView())));
            backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
            HorizontalLayout backButtonLayout = new HorizontalLayout(backButton);
            backButton.getStyle().set("margin-left", "20px");
            header.add(backButtonLayout);
        }

        // Create and add the username layout
        String currentUsername = getCurrentUsername();
        Div username = new Div();
        username.setText(currentUsername);
        HorizontalLayout usernameLayout = new HorizontalLayout(username);
        usernameLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        usernameLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        usernameLayout.setWidthFull();

        // Add the username layout to the header
        header.add(usernameLayout);
    }

    /**
     * Überprüft, ob die aktuelle Ansicht eine Unteransicht ist, die einen Zurück-Button erfordert.
     *
     * @return true, wenn die aktuelle Ansicht eine Unteransicht ist, ansonsten false.
     */
    private boolean isInSubview() {
        String currentRoute = getCurrentView();
        return currentRoute.startsWith("mieter-details") || currentRoute.startsWith("wohnung-details");
    }
    /**
     * Gibt den Namen der vorherigen Ansicht zurück, zu der der Benutzer navigieren soll.
     *
     * @return der Name der vorherigen Ansicht.
     */
    private String getPreviousView() {
        String currentRoute = getCurrentView();
        if (currentRoute.startsWith("mieter-details")) {
            return "mieter";
        } else if (currentRoute.startsWith("wohnung-details")) {
            return "wohnungen";
        }
        return "";
    }

    /**
     * Gibt den aktuellen Routennamen zurück.
     *
     * @return der aktuelle Routename.
     */
    private String getCurrentView() {
        return getUI().map(ui -> ui.getInternals().getActiveViewLocation().getPath()).orElse("");
    }


}