package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import projektarbeit.immobilienverwaltung.ui.views.DokumentListView;
import projektarbeit.immobilienverwaltung.ui.views.MieterListView;
import projektarbeit.immobilienverwaltung.ui.views.WohnungListView;
import projektarbeit.immobilienverwaltung.ui.views.ZaehlerstandListView;

public class MainLayout extends AppLayout {

    private boolean isDarkMode = true; // Dark mode enabled by default

    public MainLayout() {
        createHeader();
        createDrawer();
        createFooter();
        enableDarkMode();
    }

    @SuppressWarnings("removal")
    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();
        Label title = new Label("Immobilienverwaltung");
        Div header = new Div(toggle, title);
        header.getStyle().set("padding", "10px");
        header.getStyle().set("display", "flex");
        header.getStyle().set("alignItems", "center");
        header.getStyle().set("gap", "10px");
        addToNavbar(header);
    }

    private void enableDarkMode() {
        getElement().getThemeList().add(Lumo.DARK);
    }

    private void createFooter() {
        Button toggleButton = new Button("Toggle Dark Mode", event -> toggleDarkMode());
        toggleButton.getStyle().set("margin", "10px");
        toggleButton.getStyle().set("position", "absolute");
        toggleButton.getStyle().set("bottom", "10px");
        toggleButton.getStyle().set("left", "10px");
        addToDrawer(toggleButton);
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
        layout.add(createNavigationLink("Wohnungen", WohnungListView.class));
        layout.add(createNavigationLink("Mieter", MieterListView.class));
        layout.add(createNavigationLink("Zählerstand", ZaehlerstandListView.class));
        layout.add(createNavigationLink("Dokumente", DokumentListView.class));
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
        link.getStyle().set("width", "80%");
        return link;
    }
}
