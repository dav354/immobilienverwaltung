package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Diese Klasse stellt das Layout für die Login-Seite der Immobilienverwaltung dar.
 * Sie definiert das allgemeine Erscheinungsbild und die Anordnung der Login-Ansicht.
 */
@CssImport("./styles/shared-styles.css")
@AnonymousAllowed
public class    LoginLayout extends VerticalLayout implements RouterLayout {

    /**
     * Konstruktor für die LoginLayout-Klasse.
     * Initialisiert das Layout und aktiviert den Darkmode.
     */
    public LoginLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-layout");

        enableDarkMode(); // Enable dark mode
    }

    /**
     * Aktiviert den Darkmode für das Layout.
     */
    private void enableDarkMode() {
        getElement().getThemeList().add(Lumo.DARK);
    }
}