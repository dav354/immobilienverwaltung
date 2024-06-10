package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport("./styles/shared-styles.css")
@AnonymousAllowed
public class LoginLayout extends VerticalLayout implements RouterLayout {

    public LoginLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-layout");

        enableDarkMode(); // Enable dark mode
    }

    private void enableDarkMode() {
        getElement().getThemeList().add(Lumo.DARK);
    }
}