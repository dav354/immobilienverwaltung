package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.DashboardService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.LeafletMap;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungDetailsView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Die MainView-Klasse stellt die Startseite der Anwendung dar und zeigt aktuelle Statistiken
 * zu Mieteinnahmen, Immobilien und Mietern an.
 */
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@SpringComponent
@UIScope
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

    private final static String FONT_BIG = "38px";
    private final static String FONT_NORMAL = "28px";
    private final static String FONT_SMALL = "18px";

    private final DashboardService dashboardService;
    private final WohnungService wohnungService;
    private final ConfigurationService configurationService;

    private Div mieteinnahmenDiv;
    private Div immobilienDiv;
    private Div mieterDiv;
    private LeafletMap leafletMap;
    private Checkbox vermieteteCheckbox;
    private Checkbox unvermieteteCheckbox;

    /**
     * Konstruktor für die MainView-Klasse.
     *
     * @param dashboardService der Service, der die Daten für die Statistiken bereitstellt.
     * @param wohnungService   der Service, der die Daten für die Wohnungen bereitstellt.
     */
    @Autowired
    public MainView(DashboardService dashboardService, WohnungService wohnungService, ConfigurationService configurationService) {
        this.dashboardService = dashboardService;
        this.wohnungService = wohnungService;
        this.configurationService = configurationService;
    }

    /**
     * Initialisiert die Ansicht nach der Konstruktion.
     */
    @PostConstruct
    public void init() {
        // Erstellen eines VerticalLayout zur Zentrierung der Statistiken
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(Alignment.START);

        // Initiale Erstellung der Divs
        mieteinnahmenDiv = createMieteinnahmenDiv(dashboardService.getMieteinnahmen());
        immobilienDiv = createStatDiv("Immobilien", new Div());
        mieterDiv = createStatDiv("Anzahl der Mieter", new Div());
        leafletMap = new LeafletMap();
        leafletMap.getStyle().set("border-radius", "10px");

        // Erstellen eines HorizontalLayouts zur Platzierung der Divs nebeneinander
        HorizontalLayout statsLayout = new HorizontalLayout(mieteinnahmenDiv, immobilienDiv, mieterDiv);
        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        statsLayout.getStyle().set("margin-bottom", "20px");

        // Hinzufügen des HorizontalLayouts und der Karte zum Hauptlayout
        mainLayout.add(statsLayout, leafletMap, createCheckboxLayout(), createSupportText());

        // Hinzufügen des MainLayouts zur Hauptansicht
        add(mainLayout);

        // Initiale Aktualisierung der Daten
        updateStats();
        updateMap();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Aktualisierung der Statistiken, wenn die Seite aufgerufen wird
        updateStats();
        updateMap();
    }

    /**
     * Aktualisiert die Statistiken.
     */
    private void updateStats() {
        // Aktualisieren der Mieteinnahmen
        double mieteinnahmen = dashboardService.getMieteinnahmen();
        mieteinnahmenDiv.removeAll();
        Div newMieteinnahmenDiv = createMieteinnahmenDiv(mieteinnahmen);
        newMieteinnahmenDiv.getChildren().forEach(mieteinnahmenDiv::add);

        // Aktualisieren der Immobilienstatistiken
        Map<String, Long> immobilienStats = dashboardService.getImmobilienStats();
        immobilienDiv.removeAll();
        Div newImmobilienDiv = createImmobilienDiv(immobilienStats);
        newImmobilienDiv.getChildren().forEach(immobilienDiv::add);

        // Aktualisieren der Mieterstatistiken
        long totalMieter = dashboardService.getTotalMieter();
        mieterDiv.removeAll();
        Div newMieterDiv = createMieterDiv(totalMieter);
        newMieterDiv.getChildren().forEach(mieterDiv::add);
    }

    /**
     * Aktualisiert die Karte mit den Standorten aller Wohnungen basierend auf den Checkbox-Werten.
     */
    private void updateMap() {
        List<Wohnung> wohnungen = wohnungService.findAllWohnungen();

        // Clear all markers on the map
        leafletMap.getElement().executeJs("window.clearMapMarkers()");

        for (Wohnung wohnung : wohnungen) {
            Double latitude = wohnung.getLatitude();
            Double longitude = wohnung.getLongitude();

            if (latitude != null && longitude != null) {
                boolean isVermietet = wohnung.getMietvertrag() != null;

                if ((isVermietet && vermieteteCheckbox.getValue()) ||
                        (!isVermietet && unvermieteteCheckbox.getValue())) {

                    RouterLink link = new RouterLink(wohnung.getFormattedAddress(), WohnungDetailsView.class, wohnung.getWohnung_id());
                    link.getElement().setAttribute("href", link.getHref() + "?previousView=dashboard");

                    String popupContent = "<a href='" + link.getHref() + "'>" + wohnung.getFormattedAddress() + "</a>";
                    leafletMap.addMarker(latitude, longitude, popupContent);
                }
            }
        }
    }

    /**
     * Erstellt ein Div-Element für eine Statistik.
     *
     * @param title          der Titel der Statistik.
     * @param valueComponent der Wert der Statistik.
     * @return ein Div-Element, das die Statistik anzeigt.
     */
    private Div createStatDiv(String title, Component valueComponent) {
        Div statDiv = new Div();
        statDiv.addClassName("stat-div");

        statDiv.getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 8px 0 rgba(0,0,0,0.2)")
                .set("border-radius", "10px")
                .set("background", "var(--lumo-base-color)")
                .set("position", "relative");

        H1 statTitle = new H1(title);
        statTitle.getStyle().set("margin", "0").set("font-size", FONT_BIG).set("color", "white");

        statDiv.add(statTitle, valueComponent);

        return statDiv;
    }

    /**
     * Erstellt ein Div-Element zur Anzeige der Mieteinnahmen.
     *
     * @param mieteinnahmen die Gesamtsumme der Mieteinnahmen.
     * @return ein Div-Element, das die Mieteinnahmen anzeigt.
     */
    private Div createMieteinnahmenDiv(double mieteinnahmen) {
        // Formatieren der Zahl mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedMieteinnahmen = "€ " + numberFormat.format(mieteinnahmen);

        // Hauptwert-Layout
        HorizontalLayout valueLayout = new HorizontalLayout();
        valueLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        valueLayout.setSpacing(false);

        // Hauptwert-Div
        Div value = new Div();
        value.setText(formattedMieteinnahmen);
        value.getStyle()
                .set("font-size", FONT_BIG)
                .set("color", "white")
                .set("font-weight", "bold");

        // Untertext-Div
        Div subtext = new Div();
        subtext.setText("pro Monat");
        subtext.getStyle()
                .set("font-size", FONT_SMALL) // Kleinere Schriftgröße
                .set("color", "white")
                .set("margin-left", "10px");

        valueLayout.add(value, subtext);

        return createStatDiv("Mieteinnahmen", valueLayout);
    }

    /**
     * Erstellt ein Div-Element zur Anzeige der Immobilienstatistik.
     *
     * @param stats eine Karte, die die Gesamtzahl der Immobilien und die Anzahl der vermieteten Immobilien enthält.
     * @return ein Div-Element, das die Immobilienstatistik anzeigt.
     */
    private Div createImmobilienDiv(Map<String, Long> stats) {
        H1 title = new H1("Immobilien");
        title.getStyle().set("text-align", "center");

        // Formatieren der Zahlen mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String total = "Gesamt: " + numberFormat.format(stats.get("total"));
        String vermietet = "Vermietet: " + numberFormat.format(stats.get("vermietet"));

        Div totalDiv = new Div();
        totalDiv.setText(total);
        totalDiv.getStyle().set("font-size", FONT_BIG).set("text-align", "center").set("margin-top", "0");

        Div vermietetDiv = new Div();
        vermietetDiv.setText(vermietet);
        vermietetDiv.getStyle().set("font-size", FONT_BIG).set("text-align", "center").set("margin-top", "0");

        Div container = new Div();
        container.add(title, totalDiv, vermietetDiv);
        container.getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "100%")
                .set("padding", "20px");

        return container;
    }

    /**
     * Erstellt ein Div-Element zur Anzeige der Anzahl der Mieter.
     *
     * @param totalMieter die Gesamtzahl der Mieter.
     * @return ein Div-Element, das die Anzahl der Mieter anzeigt.
     */
    private Div createMieterDiv(long totalMieter) {
        H1 title = new H1("Anzahl der Mieter");
        title.getStyle().set("text-align", "center");

        // Formatieren der Zahlen mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedTotalMieter = numberFormat.format(totalMieter);

        Div value = new Div();
        value.setText(formattedTotalMieter);
        value.getStyle().set("font-size", FONT_BIG).set("text-align", "center").set("margin-top", "0");

        return createStatDiv("Anzahl der Mieter", value);
    }

    /**
     * Erstellt und konfiguriert ein Div-Element mit einem Support-Text und einem GitHub-Link.
     *
     * @return Ein Div-Element, das den Support-Text und den GitHub-Link anzeigt.
     */
    private HorizontalLayout createSupportText() {
        Div text = new Div();
        text.setText("Finde uns auf GitHub: ");
        text.getStyle().set("display", "inline");

        Anchor githubLink = new Anchor("https://github.com/dav354/immobilienverwaltung", "");
        githubLink.setTarget("_blank");
        githubLink.getStyle().set("display", "inline");

        boolean isDarkMode = configurationService.isDarkMode();
        String svgColor = isDarkMode ? "#fff" : "#000";

        String svgContent = "<svg height=\"20\" width=\"20\" viewBox=\"0 0 16 16\" version=\"1.1\" aria-hidden=\"true\">" +
                "<path fill=\"" + svgColor + "\" d=\"M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.19 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z\"></path>" +
                "</svg>";

        Div svgIcon = new Div();
        svgIcon.getElement().setProperty("innerHTML", svgContent);
        svgIcon.getStyle().set("display", "inline").set("vertical-align", "middle").set("margin-left", "5px");

        githubLink.add(svgIcon);

        HorizontalLayout supportLayout = new HorizontalLayout(text, githubLink);
        supportLayout.setWidthFull();
        supportLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return supportLayout;
    }

    /**
     * Erstellt und konfiguriert ein HorizontalLayout mit Checkboxen zur Auswahl von vermieteten und unvermieteten Wohnungen.
     *
     * @return Ein HorizontalLayout mit den Checkboxen.
     */
    private HorizontalLayout createCheckboxLayout() {
        vermieteteCheckbox = new Checkbox("Vermietete Wohnungen anzeigen");
        unvermieteteCheckbox = new Checkbox("Unvermietete Wohnungen anzeigen");

        // Werte aus der Konfiguration setzen, um die Zustände der Checkboxen beim Laden beizubehalten
        boolean vermieteteChecked = configurationService.getVermieteteChecked();
        boolean unvermieteteChecked = configurationService.getUnvermieteteChecked();

        vermieteteCheckbox.setValue(vermieteteChecked);
        unvermieteteCheckbox.setValue(unvermieteteChecked);

        vermieteteCheckbox.addValueChangeListener(event -> {
            configurationService.setVermieteteChecked(vermieteteCheckbox.getValue());
            getUI().ifPresent(ui -> ui.getPage().reload());
        });
        unvermieteteCheckbox.addValueChangeListener(event -> {
            configurationService.setUnvermieteteChecked(unvermieteteCheckbox.getValue());
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        HorizontalLayout checkboxLayout = new HorizontalLayout(vermieteteCheckbox, unvermieteteCheckbox);
        checkboxLayout.setAlignItems(FlexComponent.Alignment.START);
        checkboxLayout.setSpacing(true);
        checkboxLayout.setWidthFull();
        checkboxLayout.getStyle().set("margin-top", "0");

        return checkboxLayout;
    }
}