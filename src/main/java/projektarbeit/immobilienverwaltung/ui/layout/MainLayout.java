package projektarbeit.immobilienverwaltung.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.Lumo;
import projektarbeit.immobilienverwaltung.service.MService;
import projektarbeit.immobilienverwaltung.ui.views.DokumentListView;
import projektarbeit.immobilienverwaltung.ui.views.MieterListView;
import projektarbeit.immobilienverwaltung.ui.views.WohnungListView;
import projektarbeit.immobilienverwaltung.ui.views.ZaehlerstandListView;

public class MainLayout extends AppLayout {

    private boolean isDarkMode = true; // Dark mode enabled by default

    public MainLayout(MService mService) {
        createHeader();
        createDrawer(mService);
        createFooter();
        enableDarkMode();
    }

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

    private void createDrawer(MService mService) {
        SideNav sideNav = new SideNav();

        long wohnungCount = mService.getWohnungCount();
        long mieterCount = mService.getMieterCount();
        long zaehlerstandCount = mService.getZaehlerstandCount();
        long dokumentCount = mService.getDokumentCount();

        SideNavItem wohnungItem = new SideNavItem("Wohnungen", WohnungListView.class);
        Span wohnungCounter = new Span(String.valueOf(wohnungCount));
        wohnungCounter.getElement().getThemeList().add("badge contrast pill");
        wohnungItem.setSuffixComponent(wohnungCounter);

        SideNavItem mieterItem = new SideNavItem("Mieter", MieterListView.class);
        Span mieterCounter = new Span(String.valueOf(mieterCount));
        mieterCounter.getElement().getThemeList().add("badge contrast pill");
        mieterItem.setSuffixComponent(mieterCounter);

        SideNavItem zaehlerstandItem = new SideNavItem("Zählerstand", ZaehlerstandListView.class);
        Span zaehlerstandCounter = new Span(String.valueOf(zaehlerstandCount));
        zaehlerstandCounter.getElement().getThemeList().add("badge contrast pill");
        zaehlerstandItem.setSuffixComponent(zaehlerstandCounter);

        SideNavItem dokumentItem = new SideNavItem("Dokumente", DokumentListView.class);
        Span dokumentCounter = new Span(String.valueOf(dokumentCount));
        dokumentCounter.getElement().getThemeList().add("badge contrast pill");
        dokumentItem.setSuffixComponent(dokumentCounter);

        sideNav.addItem(wohnungItem, mieterItem, zaehlerstandItem, dokumentItem);

        addToDrawer(sideNav);
    }
}