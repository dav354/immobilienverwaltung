package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.WohnungEditDialog;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;

import java.util.List;

/**
 * Die WohnungListView ist die Ansicht, die alle Wohnungen in einer hierarchischen Tabelle darstellt.
 * Sie ermöglicht das Filtern und Durchsuchen der Wohnungen sowie das Bearbeiten und Hinzufügen neuer Wohnungen.
 */
@SuppressWarnings("SpellCheckingInspection")
@PermitAll
@Route(value = "wohnungen", layout = MainLayout.class)
@PageTitle("Wohnungen")
@UIScope
public class WohnungListView extends VerticalLayout {

    private final WohnungService wohnungService;
    private final MietvertragService mietvertragService;
    private final ConfigurationService configurationService;

    TreeGrid<Wohnung> treeGrid = new TreeGrid<>(Wohnung.class);
    TextField searchField = new TextField();
    Accordion filter = new Accordion();
    Checkbox land = new Checkbox("Land");
    Checkbox quadratmeter = new Checkbox("Quadratmeter");
    Checkbox baujahr = new Checkbox("Baujahr");
    Checkbox baeder = new Checkbox("Bäder");
    Checkbox schlafzimmer = new Checkbox("Schlafzimmer");
    Checkbox balkon = new Checkbox("Balkon");
    Checkbox terasse = new Checkbox("Terasse");
    Checkbox garten = new Checkbox("Garten");
    Checkbox klimaanlage = new Checkbox("Klimaanlage");
    Checkbox mieter = new Checkbox("Mieter");

    private Grid.Column<Wohnung> wohnungsnummerColumn;
    private Grid.Column<Wohnung> stockwerkColumn;
    private Grid.Column<Wohnung> anzahlWohnungenColumn;

    /**
     * Konstruktor für WohnungListView.
     * Initialisiert den Dienst, die Benutzeroberfläche und die Grid-Konfiguration.
     *
     * @param wohnungService       der Dienst für Wohnungsoperationen
     * @param mietvertragService   der Dienst für Mietvertragsoperationen
     * @param configurationService der Dienst für Konfigurationseinstellungen
     */
    public WohnungListView(WohnungService wohnungService, MietvertragService mietvertragService, ConfigurationService configurationService) {
        this.wohnungService = wohnungService;
        this.mietvertragService = mietvertragService;
        this.configurationService = configurationService;
        addClassName("list-view");
        setSizeFull();

        configureTreeGrid();

        HorizontalLayout header = new HorizontalLayout(new H1("Wohnungen Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);

        add(header, getToolbar(), createFilterAccordion(), getContent());

        updateList();
    }

    /**
     * Erstellt ein Akkordeon für Filteroptionen.
     *
     * @return das konfigurierte Akkordeon
     */
    private Accordion createFilterAccordion() {
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout col1 = new VerticalLayout(land, mieter);
        VerticalLayout col2 = new VerticalLayout(quadratmeter, baujahr);
        VerticalLayout col3 = new VerticalLayout(schlafzimmer, balkon);
        VerticalLayout col4 = new VerticalLayout(terasse, garten);
        VerticalLayout col5 = new VerticalLayout(baeder, klimaanlage);

        mieter.setValue(true);
        baujahr.setValue(true);

        layout.add(col1, col2, col3, col4, col5);

        // Listener für Filteroptionen hinzufügen
        land.addValueChangeListener(event -> updateList());
        mieter.addValueChangeListener(event -> updateList());
        quadratmeter.addValueChangeListener(event -> updateList());
        baujahr.addValueChangeListener(event -> updateList());
        baeder.addValueChangeListener(event -> updateList());
        schlafzimmer.addValueChangeListener(event -> updateList());
        balkon.addValueChangeListener(event -> updateList());
        terasse.addValueChangeListener(event -> updateList());
        garten.addValueChangeListener(event -> updateList());
        klimaanlage.addValueChangeListener(event -> updateList());

        filter.add("Tabellen Spalten auswählen", layout);
        return filter;
    }

    /**
     * Aktualisiert die Spalten im TreeGrid, um Wohnungsdetails anzuzeigen.
     * Richtet hierarchische und standardmäßige Spalten ein, einschließlich benutzerdefinierter Komponenten und Formatierungen.
     */
    private void updateGridColumns() {
        treeGrid.removeAllColumns();

        treeGrid.addHierarchyColumn(Wohnung::getPostleitzahl).setHeader("PLZ");
        treeGrid.addColumn(Wohnung::getStadt).setHeader("Stadt");
        treeGrid.addColumn(Wohnung::getStrasseMitHausnummer).setHeader("Adresse").setAutoWidth(true);
        if (land.getValue()) treeGrid.addColumn(wohnung -> wohnung.getLand().getName()).setHeader("Land");

        stockwerkColumn = treeGrid.addColumn(wohnung -> {
            return wohnung.getStockwerk() != null ? wohnung.getStockwerk() : "";
        }).setHeader(new Html("<div>Stock-<br>werk</div>")).setTextAlign(ColumnTextAlign.CENTER);

        wohnungsnummerColumn = treeGrid.addColumn(wohnung -> {
            return wohnung.getWohnungsnummer() != null ? wohnung.getWohnungsnummer() : "";
        }).setHeader(new Html("<div>Wohnungs-<br>nummer</div>")).setTextAlign(ColumnTextAlign.CENTER);

        anzahlWohnungenColumn = treeGrid.addColumn(wohnung -> {
            return wohnung.getSubWohnungen().size() > 1 ? wohnung.getSubWohnungen().size() : "";
        }).setHeader(new Html("<div>Anzahl<br>Wohnungen</div>")).setTextAlign(ColumnTextAlign.CENTER);
        anzahlWohnungenColumn.setVisible(false);

        // Spalten für Filteroptionen hinzufügen
        if (quadratmeter.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getGesamtQuadratmeter())).setHeader("m²");
        if (baujahr.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getBaujahr())).setHeader("Baujahr");
        if (baeder.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlBaeder()))
                    .setHeader("Bäder").setTextAlign(ColumnTextAlign.CENTER);
        if (schlafzimmer.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlSchlafzimmer()))
                    .setHeader(new Html("<div>Schlaf-<br>zimmer</div>"))
                    .setTextAlign(ColumnTextAlign.CENTER);
        if (balkon.getValue())
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatBalkon()))
                    .setHeader("Balkon").setTextAlign(ColumnTextAlign.CENTER);
        if (terasse.getValue())
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatTerrasse()))
                    .setHeader("Terrasse").setTextAlign(ColumnTextAlign.CENTER);
        if (garten.getValue())
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatGarten()))
                    .setHeader("Garten").setTextAlign(ColumnTextAlign.CENTER);
        if (klimaanlage.getValue())
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatKlimaanlage()))
                    .setHeader(new Html("<div>Klima-<br>anlage</div>"));

        if (mieter.getValue()) treeGrid.addColumn(new TextRenderer<>(wohnung -> {
            if (wohnung.isHeader()) {
                return "";
            } else {
                Mieter mieter = mietvertragService.findMieterByWohnung(wohnung);
                return (mieter != null) ? mieter.getFullName() : "Kein Mieter";
            }
        })).setHeader("Mieter");
        treeGrid.setSizeFull();

        treeGrid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
    }

    /**
     * Aktualisiert die Liste der Wohnungen im TreeGrid.
     * Ruft die hierarchische Liste der Wohnungen ab und erweitert die Kopfzeilen.
     */
    private void updateList() {
        List<Wohnung> wohnungen = wohnungService.findWohnungenWithHierarchy(searchField.getValue());
        TableUtils.configureTreeGrid(treeGrid, wohnungen, 50, Wohnung::getSubWohnungen);
        treeGrid.setItems(wohnungen, Wohnung::getSubWohnungen);
        wohnungen.stream().filter(Wohnung::isHeader).forEach(treeGrid::expand);
        updateGridColumns();
    }

    /**
     * Konfiguriert das TreeGrid zur Anzeige von Wohnungen.
     * Setzt Klassennamen, Größe, Spalten und Wertänderungs-Listener.
     */
    private void configureTreeGrid() {
        treeGrid.addClassNames("wohnung-grid");
        treeGrid.setSizeFull();

        updateGridColumns();

        treeGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                RouterLink link = new RouterLink("Navigate", WohnungDetailsView.class, event.getValue().getWohnung_id());
                UI.getCurrent().getPage().setLocation(link.getHref());
            }
        });

        treeGrid.addExpandListener(event -> {
            updateGridColumns();
            wohnungsnummerColumn.setVisible(true);
            stockwerkColumn.setVisible(true);
            anzahlWohnungenColumn.setVisible(false);
        });
        treeGrid.addCollapseListener(event -> {
            updateGridColumns();
            wohnungsnummerColumn.setVisible(false);
            stockwerkColumn.setVisible(false);
            anzahlWohnungenColumn.setVisible(true);
        });
    }

    /**
     * Erstellt das Inhaltslayout.
     * Kombiniert das TreeGrid und das Formular in einem horizontalen Layout.
     *
     * @return das konfigurierte Inhaltslayout
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(treeGrid);
        content.setFlexGrow(2, treeGrid);
        content.setFlexGrow(1);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    /**
     * Erstellt die Werkzeugleiste.
     * Enthält das Filtertextfeld und den Button zum Hinzufügen einer neuen Wohnung.
     *
     * @return die konfigurierte Werkzeugleiste
     */
    private HorizontalLayout getToolbar() {
        searchField.setPlaceholder("Filter nach Adresse...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> updateList());

        Button addButton = new Button("Wohnung hinzufügen");
        addButton.setPrefixComponent(VaadinIcon.PLUS.create());
        addButton.addClickListener(e -> {
            Wohnung newWohnung = new Wohnung();
            WohnungEditDialog dialog = new WohnungEditDialog(wohnungService, newWohnung, () -> {
                updateList();
                getUI().ifPresent(ui -> ui.navigate(WohnungDetailsView.class, newWohnung.getWohnung_id()));
            },
                    configurationService);
            dialog.open();
        });

        var toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        toolbar.setFlexGrow(1, searchField);
        return toolbar;
    }

    /**
     * Erstellt ein Icon zur Anzeige im Grid.
     *
     * @param value der Wahrheitswert, ob das Icon ein Häkchen oder ein Kreuz sein soll
     * @return das erstellte Icon
     */
    private Icon createIcon(boolean value) {
        Icon icon = value ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
        icon.setSize("14px");
        return icon;
    }

    /**
     * Erstellt ein leeres, unsichtbares Icon für das TreeGrid.
     * Wird verwendet, um eine konsistente Spaltenausrichtung zu gewährleisten, wenn kein Icon benötigt wird.
     *
     * @return eine Instanz eines leeren, unsichtbaren Icons
     */
    private Icon createEmptyIcon() {
        Icon icon = new Icon();
        icon.setVisible(false);
        return icon;
    }
}