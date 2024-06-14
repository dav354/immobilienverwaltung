package projektarbeit.immobilienverwaltung.ui.views.Login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import projektarbeit.immobilienverwaltung.ui.layout.LoginLayout;

/**
 * Diese Klasse stellt die Login-Ansicht der Immobilienverwaltung dar.
 * Sie ermöglicht es Benutzern, sich in die Anwendung einzuloggen.
 */
@Route(value = "login", layout = LoginLayout.class)
@PageTitle("Login | Immobilienverwaltung")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    /**
     * Konstruktor für die LoginView-Klasse.
     * Initialisiert das Layout und die Komponenten der Login-Ansicht.
     */
    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        add(new H1("Immobilienverwaltung"), login);
    }

    /**
     * Diese Methode wird vor dem Betreten der Ansicht aufgerufen.
     * Sie überprüft, ob ein Authentifizierungsfehler vorliegt und zeigt eine Fehlermeldung an, falls vorhanden.
     *
     * @param beforeEnterEvent das BeforeEnterEvent, das Informationen über die Navigation enthält
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Informiert den Benutzer über einen Authentifizierungsfehler
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}