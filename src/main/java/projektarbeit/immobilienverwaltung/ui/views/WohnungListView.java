package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.WService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.Collections;

@Route(value = "wohnungen", layout = MainLayout.class)
@PageTitle("Wohnungen")
@UIScope
public class WohnungListView extends VerticalLayout {

    private final WService wService;
    Grid<Wohnung> grid = new Grid<>(Wohnung.class);
    TextField filterText = new TextField();
    WohnungForm form;

    public WohnungListView(WService wService) {
        this.wService = wService;
        addClassName("list-view");
        setSizeFull();
        
        configureGrid();
        configureForm();

        HorizontalLayout header = new HorizontalLayout(new H1("Wohnungen Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(header.getComponentAt(0));

        add(header, getToolbar(), getContent());

        updateList();

        closeEditor();
    }

    private void closeEditor() {
        form.setWohnung(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(wService.findAllWohnungen(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new WohnungForm(wService.findAllMieter());
        form.setWidth("200em");

        //Wartet darauf das die Knöpfe gedrückt werden und führt dann die passende Methode aus
        form.addListener(WohnungForm.SaveEvent.class, this::saveWohnung);
        form.addListener(WohnungForm.DeleteEvent.class, this::deleteWohnung);
        form.addListener(WohnungForm.CloseEvent.class, e -> closeEditor());
    }

    //Mieter speichern
    private void saveWohnung(WohnungForm.SaveEvent event) {
        wService.saveWohnungen(event.getWohnung());
        updateList();
        closeEditor();
    }
    //Mieter löschen
    private void deleteWohnung(WohnungForm.DeleteEvent event) {
        wService.deleteWohnungen(event.getWohnung());
        updateList();
        closeEditor();
    }
    private void configureGrid() {
        grid.addClassNames("wohung-grid");
        grid.setSizeFull();

        grid.removeAllColumns();

        grid.addColumn(wohnung -> wohnung.getAdresse().getPostleitzahlObj().getLand().name()).setHeader("Land");
        grid.addColumn(wohnung -> wohnung.getAdresse().getPostleitzahlObj().getPostleitzahl()).setHeader("PLZ");
        grid.addColumn(wohnung -> wohnung.getAdresse().getPostleitzahlObj().getStadt()).setHeader("Stadt");
        grid.addColumn(wohnung -> wohnung.getAdresse().getStrasse() + " " + wohnung.getAdresse().getHausnummer()).setHeader("Adresse");
        grid.addColumn(wohnung -> {
            Mieter mieter = wohnung.getMieter();
            return (mieter != null) ? mieter.getFullName() : "Kein Mieter";
        }).setHeader("Mieter");

        grid.addColumn(Wohnung::getGesamtQuadratmeter).setHeader("m²");
        grid.addColumn(Wohnung::getBaujahr).setHeader("Baujahr");
        grid.addColumn(Wohnung::getAnzahlBaeder).setHeader("Bäder");
        grid.addColumn(Wohnung::getAnzahlSchlafzimmer).setHeader("Schlafzimmer");

        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatBalkon())).setHeader("Balkon");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatTerrasse())).setHeader("Terrasse");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatGarten())).setHeader("Garten");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatKlimaanlage())).setHeader("Klimaanlage");

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        grid.asSingleSelect().addValueChangeListener(event -> editWohnung(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter nach Adresse...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Wohnung hinzufügen");
        addContactButton.addClickListener(e -> addWohnung());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void editWohnung(Wohnung wohnung) {
        if (wohnung == null) {
            form.clearFields();
            closeEditor();
        } else {
            form.setWohnung(wohnung);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addWohnung() {
        grid.asSingleSelect().clear();
        Wohnung neueWohnung = new Wohnung();
        form.clearFields(); // Sicherstellen, dass Felder geleert werden
        editWohnung(neueWohnung);
    }

    private Icon createIcon(boolean value) {
        Icon icon = value ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
        icon.addClassName("v-icon"); // Ensure the icon gets the correct class
        return icon;
    }
    /*
    private static final Logger logger = LoggerFactory.getLogger(WohnungListView.class);

    private final WohnungService wohnungService;
    private final Grid<Wohnung> grid = new Grid<>(Wohnung.class);
    private final WohnungForm form;

    @Autowired
    public WohnungListView(WohnungService wohnungService, AdresseService adresseService, MieterService mieterService) {
        this.wohnungService = wohnungService;
        this.form = new WohnungForm(wohnungService, adresseService, mieterService);
        this.form.addDialogCloseActionListener(event -> updateList());

        HorizontalLayout header = new HorizontalLayout(new H1("Wohnungen Übersicht"), createAddButton());
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(header.getComponentAt(0)); // Expand the H1 to push the button to the right

        add(header);

        HorizontalLayout layout = new HorizontalLayout(grid, form);
        layout.setSizeFull();
        add(layout);

        setupGrid();
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
        grid.addColumn(wohnung -> wohnung.getAdresse().getPostleitzahlObj().getLand().name()).setHeader("Land").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(wohnung -> wohnung.getAdresse().getPostleitzahlObj().getPostleitzahl()).setHeader("PLZ").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(wohnung -> wohnung.getAdresse().getStrasse() + " " + wohnung.getAdresse().getHausnummer()).setHeader("Adresse").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(wohnung -> {
            Mieter mieter = wohnung.getMieter();
            return (mieter != null) ? mieter.getFullName() : "Kein Mieter";
        }).setHeader("Mieter").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(Wohnung::getGesamtQuadratmeter).setHeader("m²").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(Wohnung::getBaujahr).setHeader("Baujahr").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(Wohnung::getAnzahlBaeder).setHeader("Bäder").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addColumn(Wohnung::getAnzahlSchlafzimmer).setHeader("Schlafzimmer").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");

        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatBalkon())).setHeader("Balkon").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatTerrasse())).setHeader("Terrasse").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatGarten())).setHeader("Garten").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatKlimaanlage())).setHeader("Klimaanlage").setSortable(true).setClassNameGenerator(wohnung -> "centered-column");

        grid.addComponentColumn(wohnung -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> editWohnung(wohnung));
            Button deleteButton = new Button("✕", e -> deleteWohnung(wohnung));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader("Actions").setClassNameGenerator(wohnung -> "centered-column");

        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Add details generator for expanding rows
        grid.setItemDetailsRenderer(new ComponentRenderer<>(wohnung -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setWidthFull();
            detailsLayout.getStyle().set("border-top", "1px solid #ccc");

            VerticalLayout infoLayout = new VerticalLayout();
            infoLayout.add(new H3("Zusätzliche Informationen"));

            // Additional information
            infoLayout.add(new VerticalLayout(
                    new HorizontalLayout(
                            new H4("Land: "), new Span(wohnung.getAdresse().getPostleitzahlObj().getLand().toString())
                    )
            ));

            // Documents list
            Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class, false);
            dokumentGrid.addColumn(Dokument::getDokumententyp).setHeader("Dokumententyp");
            dokumentGrid.addColumn(dokument -> {
                Button downloadButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));
                downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                downloadButton.addClickListener(e -> {
                    // Add your download logic here
                    Notification.show("Download: " + dokument.getDateipfad());
                });

                Button viewButton = new Button(new Icon(VaadinIcon.EYE));
                viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                viewButton.addClickListener(e -> {
                    // Add your view logic here
                    Notification.show("View: " + dokument.getDateipfad());
                });

                HorizontalLayout actions = new HorizontalLayout(downloadButton, viewButton);
                return actions;
            }).setHeader("Aktionen");

            List<Dokument> dokumente = wohnungService.findDokumenteByWohnung(wohnung);
            dokumentGrid.setItems(dokumente);
            dokumentGrid.setHeight(dokumente.size() > 4 ? "200px" : null);
            dokumentGrid.setMaxHeight("200px");
            dokumentGrid.getStyle().set("overflow-y", "auto");

            VerticalLayout dokumenteLayout = new VerticalLayout();
            dokumenteLayout.add(new H4("Dokumente"), dokumentGrid);
            dokumenteLayout.setWidth("400px");
            dokumenteLayout.getStyle().set("padding-left", "20px");

            detailsLayout.add(infoLayout, dokumenteLayout);
            return detailsLayout;
        }));

        grid.addItemClickListener(event -> {
            grid.setDetailsVisible(event.getItem(), !grid.isDetailsVisible(event.getItem()));
        });
    }

    private Icon createIcon(boolean value) {
        Icon icon = value ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
        icon.addClassName("v-icon"); // Ensure the icon gets the correct class
        return icon;
    }

    private Button createAddButton() {
        Button addButton = new Button("Add New Wohnung");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.getStyle().set("margin-left", "auto"); // Align to the right
        addButton.addClickListener(e -> {
            form.setWohnung(new Wohnung());
            form.open();
        });
        return addButton;
    }

    private void editWohnung(Wohnung wohnung) {
        if (wohnung == null) {
            closeEditor();
        } else {
            form.setWohnung(wohnung);
            form.open();
            form.addDialogCloseActionListener(event -> {
                updateList();
            });
        }
    }

    private void deleteWohnung(Wohnung wohnung) {
        wohnungService.delete(wohnung);
        updateList();
        Notification.show("Wohnung deleted", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void closeEditor() {
        form.setWohnung(null);
        form.close();
    }

    private void updateList() {
        logger.debug("updateList() method called");
        List<Wohnung> wohnungen = wohnungService.findAllWohnungen();
        if (wohnungen.isEmpty()) {
            logger.warn("Keine Wohnungen gefunden");
        } else {
            logger.info("Wohnungen gefunden: {}", wohnungen.size());
            wohnungen.forEach(w -> logger.debug(w.toString()));
        }
        grid.setItems(wohnungen);
        logger.info("Grid items count: {}", grid.getDataProvider().size(new Query<>()));
    }

     */
}