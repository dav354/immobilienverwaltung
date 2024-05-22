package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.service.MService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
public class MieterListView extends VerticalLayout {

    //Einzelnen Bestandteile der Seite
    private final MService mieterService;
    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    MieterForm form;

    //Der Aufbau der Seite mit Überschrift, Eingabe und der Tabelle
    public MieterListView(MService mieterService) {
        this.mieterService = mieterService;

        HorizontalLayout header = new HorizontalLayout(new H1("Mieter Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(header.getComponentAt(0));

        addClassName("mieter-list");
        setSizeFull();
        configureGrid();
        configureForm();

        add(header, getToolbar(), getContent());
        updateList();

        closeEditor();
    }

    //Dass der Editor beim Start der Seite geschlossen ist
    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    //Um die Liste zu Updaten
    private void updateList() {
        grid.setItems(mieterService.findAllMieter(filterText.getValue()));
    }

    //Wie die Tabelle und der Editor auf der Seite aufgebaut werden
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    //Einstellungen des Forms für die Erstellung/Bearbeitung der Mieter
    private void configureForm() {
        form = new MieterForm(mieterService.findAllWohnungen());
        form.setWidth("400em");
        //Wartet darauf das die Knöpfe gedrückt werden und führt dann die passende Methode aus
        form.addListener(MieterForm.SaveEvent.class, this::saveMieter);
        form.addListener(MieterForm.DeleteEvent.class, this::deleteMieter);
        form.addListener(MieterForm.CloseEvent.class, e -> closeEditor());
    }

    //Mieter speichern
    private void saveMieter(MieterForm.SaveEvent event) {
        mieterService.saveMieter(event.getContact());
        updateList();
        closeEditor();
    }
    //Mieter löschen
    private void deleteMieter(MieterForm.DeleteEvent event) {
        mieterService.deleteMieter(event.getContact());
        updateList();
        closeEditor();
    }

    //Anlegen der Tabelle für die Mieter
    private void configureGrid() {
        grid.addClassNames("mieter-grid");
        grid.setSizeFull();
        //Hier bei getFirst muss man noch änderen da kein Mieter zu den Wohnungen zugeordnet ist
        grid.addColumn(mieter -> mieter.getWohnung().getFirst()).setHeader("Wohnung");
        grid.setColumns("name", "vorname", "telefonnummer", "einkommen", "mietbeginn", "mietende", "kaution", "anzahlBewohner");
        //Automatische Größe und Sotieren zulassen
        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        grid.asSingleSelect().addValueChangeListener(e -> editMieter(e.getValue()));
    }

    //Mieter bearbeiten funktion
    private void editMieter(Mieter mieter) {
        if (mieter == null) {
            closeEditor();
        } else {
            form.setMieter(mieter);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    //Erstellung des Suchfelds und des Button
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Mieter hinzufügen");
        addContactButton.addClickListener(e -> addMieter());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    //Mieter hinzufügen
    private void addMieter() {
        grid.asSingleSelect().clear();
        editMieter(new Mieter());
    }
}
    /*Nicht notwendig noch von David

        // Adding column for the complete address
        grid.addColumn(mieter -> mieter.getWohnung().isEmpty()
                        ? "Keine Wohnung"
                        : mieter.getWohnung().stream()
                        .map(wohnung -> {
                            Adresse adresse = wohnung.getAdresse();
                            Postleitzahl postleitzahlObj = adresse.getPostleitzahlObj();
                            return String.format("%s %s %s %s %s",
                                    postleitzahlObj.getLand().name(),
                                    postleitzahlObj.getPostleitzahl(),
                                    postleitzahlObj.getStadt(),
                                    adresse.getStrasse(),
                                    adresse.getHausnummer());
                        })
                        .collect(Collectors.joining("\n")))
                .setHeader("Mietobjekt").setSortable(true);

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

     */