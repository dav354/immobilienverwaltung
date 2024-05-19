package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.service.ZaehlerstandService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.List;

@Route(value = "zaehlerstand", layout = MainLayout.class)
@PageTitle("Zaehlerstand")
@UIScope
@Component
public class ZaehlerstandListView extends VerticalLayout {

    private static final Logger logger = LoggerFactory.getLogger(ZaehlerstandListView.class);

    private final ZaehlerstandService zaehlerstandService;
    private final Grid<Zaehlerstand> grid = new Grid<>(Zaehlerstand.class);

    @Autowired
    public ZaehlerstandListView(ZaehlerstandService zaehlerstandService) {
        this.zaehlerstandService = zaehlerstandService;
        add(new H1("Zaehlerstand Übersicht"));
        setupGrid();
        add(grid);
        setSizeFull();
    }

    @PostConstruct
    public void init() {
        updateList();
    }

    private void setupGrid() {
        logger.debug("setupGrid() method called");

        // Define columns for the grid
        grid.removeAllColumns();
        grid.addColumn(Zaehlerstand::getAblesedatum).setHeader("Ablesedatum").setSortable(true);
        grid.addColumn(Zaehlerstand::getAblesewert).setHeader("Ablesewert").setSortable(true);
        grid.addColumn(zaehlerstand -> zaehlerstand.getWohnung().getAdresse().getStrasse())
                .setHeader("Strasse").setSortable(true);
        grid.addColumn(zaehlerstand -> zaehlerstand.getWohnung().getAdresse().getHausnummer())
                .setHeader("Hausnummer").setSortable(true);
        grid.addColumn(zaehlerstand -> zaehlerstand.getWohnung().getAdresse().getPostleitzahlObj().getPostleitzahl())
                .setHeader("Postleitzahl").setSortable(true);

        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        logger.debug("updateList() method called");
        List<Zaehlerstand> zaehlerstaende = zaehlerstandService.findAllZaehlerstaende();
        if (zaehlerstaende.isEmpty()) {
            logger.warn("Keine Zaehlerstände gefunden");
        } else {
            logger.info("Zaehlerstände gefunden: {}", zaehlerstaende.size());
            zaehlerstaende.forEach(z -> logger.debug(z.toString()));
        }
        grid.setItems(zaehlerstaende);
        logger.info("Grid items count: {}", grid.getDataProvider().size(new Query<>()));
    }
}