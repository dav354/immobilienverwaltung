package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

@PermitAll
@Route(value = "/", layout = MainLayout.class)
@UIScope
@PageTitle("Home")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Willkommen zur Immobilienverwaltung"));
        add(new Paragraph("Wählen Sie eine der Optionen aus dem Menü links, um zu beginnen."));
    }
}