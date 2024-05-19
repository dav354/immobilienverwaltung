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
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.service.DokumentService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.List;

@Route(value = "dokumente", layout = MainLayout.class)
@UIScope
@PageTitle("Dokumente")
@Component
public class DokumentListView extends VerticalLayout {

    private static final Logger logger = LoggerFactory.getLogger(DokumentListView.class);

    private final DokumentService dokumentService;
    private final Grid<Dokument> grid = new Grid<>(Dokument.class);

    @Autowired
    public DokumentListView(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
        add(new H1("Dokumente Übersicht"));
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
        grid.addColumn(Dokument::getDokumententyp).setHeader("Dokumententyp").setSortable(true);
        grid.addColumn(Dokument::getDateipfad).setHeader("Dateipfad").setSortable(true);
        grid.addColumn(dokument -> dokument.getWohnung() != null ? dokument.getWohnung().getAdresse().getStrasse() : "N/A")
                .setHeader("Strasse").setSortable(true);
        grid.addColumn(dokument -> dokument.getWohnung() != null ? dokument.getWohnung().getAdresse().getHausnummer() : "N/A")
                .setHeader("Hausnummer").setSortable(true);
        grid.addColumn(dokument -> dokument.getWohnung() != null ? dokument.getWohnung().getAdresse().getPostleitzahlObj().getPostleitzahl() : "N/A")
                .setHeader("Postleitzahl").setSortable(true);
        grid.addColumn(dokument -> dokument.getMieter() != null ? dokument.getMieter().getName() + ", " + dokument.getMieter().getVorname() : "N/A")
                .setHeader("Mieter").setSortable(true);

        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        logger.debug("updateList() method called");
        List<Dokument> dokumente = dokumentService.findAllDokumente();
        if (dokumente.isEmpty()) {
            logger.warn("Keine Dokumente gefunden");
        } else {
            logger.info("Dokumente gefunden: {}", dokumente.size());
            dokumente.forEach(d -> logger.debug(d.toString()));
        }
        grid.setItems(dokumente);
        logger.info("Grid items count: {}", grid.getDataProvider().size(new Query<>()));
    }
}