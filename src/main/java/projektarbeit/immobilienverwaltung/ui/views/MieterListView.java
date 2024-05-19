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
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.List;

@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
@Component
public class MieterListView extends VerticalLayout {

    private static final Logger logger = LoggerFactory.getLogger(MieterListView.class);

    private final MieterService mieterService;
    private final Grid<Mieter> grid = new Grid<>(Mieter.class);

    @Autowired
    public MieterListView(MieterService mieterService) {
        this.mieterService = mieterService;
        add(new H1("Mieter Übersicht"));
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
        grid.addColumn(Mieter::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Mieter::getVorname).setHeader("Vorname").setSortable(true);
        grid.addColumn(Mieter::getTelefonnummer).setHeader("Telefonnummer").setSortable(true);
        grid.addColumn(Mieter::getEinkommen).setHeader("Einkommen").setSortable(true);
        grid.addColumn(Mieter::getAusgaben).setHeader("Ausgaben").setSortable(true);
        grid.addColumn(Mieter::getMietbeginn).setHeader("Mietbeginn").setSortable(true);
        grid.addColumn(Mieter::getMietende).setHeader("Mietende").setSortable(true);
        grid.addColumn(Mieter::getKaution).setHeader("Kaution").setSortable(true);
        grid.addColumn(Mieter::getAnzahlBewohner).setHeader("Anzahl Bewohner").setSortable(true);
        grid.addColumn(mieter -> mieter.getWohnung().getAdresse().getStrasse()).setHeader("Strasse").setSortable(true);
        grid.addColumn(mieter -> mieter.getWohnung().getAdresse().getHausnummer()).setHeader("Hausnummer").setSortable(true);
        grid.addColumn(mieter -> mieter.getWohnung().getAdresse().getPostleitzahlObj().getPostleitzahl()).setHeader("Postleitzahl").setSortable(true);

        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        logger.debug("updateList() method called");
        List<Mieter> mieter = mieterService.findAllMieter();
        if (mieter.isEmpty()) {
            logger.warn("Keine Mieter gefunden");
        } else {
            logger.info("Mieter gefunden: {}", mieter.size());
            mieter.forEach(m -> logger.debug(m.toString()));
        }
        grid.setItems(mieter);
        logger.info("Grid items count: {}", grid.getDataProvider().size(new Query<>()));
    }
}