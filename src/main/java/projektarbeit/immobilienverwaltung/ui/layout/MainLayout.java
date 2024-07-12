package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.SecurityService;
import projektarbeit.immobilienverwaltung.service.UserService;
import projektarbeit.immobilienverwaltung.ui.views.MainView;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ChangePasswordDialog;
import projektarbeit.immobilienverwaltung.ui.views.dokumente.DokumenteListView;
import projektarbeit.immobilienverwaltung.ui.views.login.AdminView;
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterListView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungListView;

/**
 * Hauptlayout der Anwendung, das die Navigation und das Layout der Anwendung definiert.
 */
@CssImport("./styles/styles.css")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final UserService userService;
    private final ConfigurationService configurationService;
    private boolean isDarkMode;

    /**
     * Konstruktor für das MainLayout.
     *
     * @param securityService      der SecurityService zur Verwaltung der Sicherheitsfunktionen.
     * @param userService          der UserService zur Verwaltung der Benutzerdaten.
     * @param configurationService der ConfigurationService zur Verwaltung der Konfigurationseinstellungen.
     */
    public MainLayout(SecurityService securityService, UserService userService, ConfigurationService configurationService) {
        this.securityService = securityService;
        this.userService = userService;
        this.configurationService = configurationService;

        this.isDarkMode = configurationService.isDarkMode();

        createHeader();
        createDrawer();
        applyDarkMode();
    }

    /**
     * Erstellt die Kopfzeile des Layouts.
     */
    private void createHeader() {
        String currentUsername = getCurrentUsername();
        DrawerToggle toggle = new DrawerToggle();

        Div title = new Div();
        title.setText("Immobilienverwaltung");
        title.getStyle().set("font-weight", "bold").set("font-size", "24px");

        // Erstelle einen Layout für Logo und Titel
        HorizontalLayout logoTitleLayout = new HorizontalLayout(title);
        logoTitleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoTitleLayout.setSpacing(true);

        // Benutzername Button
        Button userButton = new Button(currentUsername);
        userButton.setSuffixComponent(VaadinIcon.KEY.create());
        userButton.addClickListener(event -> {
            ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(configurationService, userService);
            changePasswordDialog.open();
        });

        // Linke Seite des Headers
        HorizontalLayout leftHeader = new HorizontalLayout(toggle, logoTitleLayout);
        leftHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        leftHeader.setSpacing(true);

        // Rechte Seite des Headers
        HorizontalLayout rightHeader = new HorizontalLayout(userButton);
        rightHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        rightHeader.setSpacing(true);

        // Hauptheader
        HorizontalLayout header = new HorizontalLayout(leftHeader, rightHeader);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(false);
        header.getStyle().set("padding", "10px");
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

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
     * Wendet den aktuellen Dark Mode-Status an.
     */
    private void applyDarkMode() {
        if (isDarkMode) {
            getElement().getThemeList().add(Lumo.DARK);
        } else {
            getElement().getThemeList().remove(Lumo.DARK);
        }
    }

    /**
     * Erstellt die Fußzeile des Layouts.
     */
    private VerticalLayout createFooter() {
        Button logoutButton = new Button("Logout", event -> securityService.logout());
        logoutButton.addClassName("footer-button");

        Button toggleDarkModeButton = new Button(isDarkMode ? "Toggle Light Mode" : "Toggle Dark Mode");
        toggleDarkModeButton.addClassName("footer-button");

        toggleDarkModeButton.addClickListener(event -> {
            isDarkMode = !isDarkMode;
            configurationService.setDarkMode(isDarkMode);
            applyDarkMode();

            // Reload the page to apply the changes
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        VerticalLayout footerLayout = new VerticalLayout(logoutButton, toggleDarkModeButton);
        footerLayout.setSpacing(false);
        footerLayout.setPadding(true);
        footerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        footerLayout.setMargin(false);
        return footerLayout;
    }

    /**
     * Erstellt das Navigationsmenü im Drawer.
     */
    private void createDrawer() {
        SideNav nav = new SideNav();

        SideNavItem home = new SideNavItem("Home", MainView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem wohnungen = new SideNavItem("Wohnungen", WohnungListView.class, VaadinIcon.HOME.create());
        SideNavItem mieter = new SideNavItem("Mieter", MieterListView.class, VaadinIcon.USER.create());
        SideNavItem dokumente = new SideNavItem("Dokumente", DokumenteListView.class, VaadinIcon.FILE.create());

        nav.addItem(home, wohnungen, mieter, dokumente);

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