package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.AttachEvent;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import projektarbeit.immobilienverwaltung.service.DashboardService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import jakarta.annotation.PostConstruct;
import java.text.NumberFormat;
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
public class MainView extends VerticalLayout {

    private final DashboardService dashboardService;

    private Div mieteinnahmenDiv;
    private Div immobilienDiv;
    private Div mieterDiv;

    /**
     * Konstruktor für die MainView-Klasse.
     *
     * @param dashboardService der Service, der die Daten für die Statistiken bereitstellt.
     */
    @Autowired
    public MainView(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Initialisiert die Ansicht nach der Konstruktion.
     */
    @PostConstruct
    public void init() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H2 title = new H2("Hier sind Ihre aktuellen Statistiken");
        titleLayout.add(title);

        add(titleLayout);

        // Initiale Erstellung der Divs
        mieteinnahmenDiv = new Div();
        immobilienDiv = new Div();
        mieterDiv = new Div();

        // Erstellen eines HorizontalLayout zur Platzierung der Divs nebeneinander
        HorizontalLayout statsLayout = new HorizontalLayout(mieteinnahmenDiv, immobilienDiv, mieterDiv);
        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Hinzufügen des HorizontalLayout zum Hauptlayout
        add(statsLayout);

        // Initiale Aktualisierung der Daten
        updateStats();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Aktualisierung der Statistiken, wenn die Seite aufgerufen wird
        updateStats();
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
     * Erstellt ein Div-Element zur Anzeige der Mieteinnahmen.
     *
     * @param mieteinnahmen die Gesamtsumme der Mieteinnahmen.
     * @return ein Div-Element, das die Mieteinnahmen anzeigt.
     */
    private Div createMieteinnahmenDiv(double mieteinnahmen) {
        H2 title = new H2("Mieteinnahmen");
        title.getStyle().set("text-align", "center");

        // Formatieren der Zahl mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedMieteinnahmen = "€ " + numberFormat.format(mieteinnahmen);

        return getDiv(title, formattedMieteinnahmen);
    }

    private Div getDiv(H2 title, String formattedMieteinnahmen) {
        Div value = new Div();
        value.setText(formattedMieteinnahmen);
        value.getStyle().set("font-size", "48px").set("text-align", "center").set("margin-top", "0");

        Div container = new Div();
        container.add(title, value);
        container.getStyle().set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "100%")
                .set("padding", "20px");

        return container;
    }

    /**
     * Erstellt ein Div-Element zur Anzeige der Immobilienstatistik.
     *
     * @param stats eine Karte, die die Gesamtzahl der Immobilien und die Anzahl der vermieteten Immobilien enthält.
     * @return ein Div-Element, das die Immobilienstatistik anzeigt.
     */
    private Div createImmobilienDiv(Map<String, Long> stats) {
        H2 title = new H2("Immobilien");
        title.getStyle().set("text-align", "center");

        // Formatieren der Zahlen mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String total = "Gesamt: " + numberFormat.format(stats.get("total"));
        String vermietet = "Vermietet: " + numberFormat.format(stats.get("vermietet"));

        Div totalDiv = new Div();
        totalDiv.setText(total);
        totalDiv.getStyle().set("font-size", "36px").set("text-align", "center").set("margin-top", "0");

        Div vermietetDiv = new Div();
        vermietetDiv.setText(vermietet);
        vermietetDiv.getStyle().set("font-size", "36px").set("text-align", "center").set("margin-top", "0");

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
        H2 title = new H2("Anzahl der Mieter");
        title.getStyle().set("text-align", "center");

        // Formatieren der Zahlen mit Tausendertrennzeichen
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedTotalMieter = numberFormat.format(totalMieter);

        return getDiv(title, formattedTotalMieter);
    }
}