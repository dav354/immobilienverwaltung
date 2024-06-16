package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import projektarbeit.immobilienverwaltung.controller.DashboardController;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import jakarta.annotation.PostConstruct;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@SpringComponent
@UIScope
public class MainView extends VerticalLayout {

    private final DashboardController dashboardController;

    @Autowired
    public MainView(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @PostConstruct
    public void init() {
        add(new H1("Willkommen zur Immobilienverwaltung"));
        add(new Paragraph("Hier sind Ihre aktuellen Statistiken:"));

        // Mieteinnahmen Anzeige
        double mieteinnahmen = dashboardController.getMieteinnahmen();
        Div mieteinnahmenDiv = createMieteinnahmenDiv(mieteinnahmen);

        // Immobilien Statistik Anzeige
        Map<String, Long> immobilienStats = dashboardController.getImmobilienStats();
        Div immobilienDiv = createImmobilienDiv(immobilienStats);

        // Mieter Statistik Anzeige
        long totalMieter = dashboardController.getTotalMieter();
        Div mieterDiv = createMieterDiv(totalMieter);

        // Creating a HorizontalLayout to place the Divs side by side
        HorizontalLayout statsLayout = new HorizontalLayout(mieteinnahmenDiv, immobilienDiv, mieterDiv);
        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Adding the HorizontalLayout to the main layout
        add(statsLayout);
    }

    private Div createMieteinnahmenDiv(double mieteinnahmen) {
        H2 title = new H2("Mieteinnahmen");
        title.getStyle().set("text-align", "center");

        // Format the number with thousand separators
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedMieteinnahmen = "€ " + numberFormat.format(mieteinnahmen);

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

    private Div createImmobilienDiv(Map<String, Long> stats) {
        H2 title = new H2("Immobilienstatistik");
        title.getStyle().set("text-align", "center");

        // Format the numbers with thousand separators
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

    private Div createMieterDiv(long totalMieter) {
        H2 title = new H2("Anzahl der Mieter");
        title.getStyle().set("text-align", "center");

        // Format the number with thousand separators
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String formattedTotalMieter = numberFormat.format(totalMieter);

        Div value = new Div();
        value.setText(formattedTotalMieter);
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
}
