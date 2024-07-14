package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
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
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterDetailsView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<String> expandedNodeIds = new HashSet<>();

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

        Html helpText = new Html("<span>Um mehr Infos zu den Wohnungen zu bekommen, in die entsprechende Zeile oder Mieter klicken.<br>Wenn mehrere Wohnungen im selben Haus existieren, expandiere diese um die Wohnungen zu sehen.</span>");
        HorizontalLayout help = new HorizontalLayout(helpText);
        help.setWidthFull();
        help.setAlignItems(Alignment.START);

        add(header, help, getToolbar(), createFilterAccordion(), getContent());

        updateList();
    }

    /**
     * Erstellt ein Akkordeon für Filteroptionen.
     *
     * @return das konfigurierte Akkordeon
     */
    private Accordion createFilterAccordion() {
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout col1 = createVerticalLayoutWithSpacing(land, mieter);
        VerticalLayout col2 = createVerticalLayoutWithSpacing(quadratmeter, baujahr);
        VerticalLayout col3 = createVerticalLayoutWithSpacing(schlafzimmer, balkon);
        VerticalLayout col4 = createVerticalLayoutWithSpacing(terasse, garten);
        VerticalLayout col5 = createVerticalLayoutWithSpacing(baeder, klimaanlage);

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

        boolean isExpanded = configurationService.getAccordionState("filterAccordionExpanded", true);
        if (isExpanded) {
            filter.open(0);
        } else {
            filter.close();
        }

        filter.addOpenedChangeListener(event -> {
            int openedIndex = event.getOpenedIndex().orElse(-1);
            boolean isOpened = openedIndex == 0;
            configurationService.setAccordionState("filterAccordionExpanded", isOpened);
        });

        return filter;
    }

    /**
     * Erstellt ein VerticalLayout mit festgelegtem Abstand zwischen den Komponenten.
     *
     * @param components die Komponenten, die dem Layout hinzugefügt werden sollen.
     * @return das konfigurierte VerticalLayout.
     */
    private VerticalLayout createVerticalLayoutWithSpacing(Component... components) {
        VerticalLayout layout = new VerticalLayout(components);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);
        return layout;
    }

    /**
     * Aktualisiert die Spalten im TreeGrid, um Wohnungsdetails anzuzeigen.
     * Richtet hierarchische und standardmäßige Spalten ein, einschließlich benutzerdefinierter Komponenten und Formatierungen.
     */
    private void updateGridColumns() {
        treeGrid.removeAllColumns();

        treeGrid.addHierarchyColumn(Wohnung::getPostleitzahl).setHeader(createCustomHeader("PLZ"));
        treeGrid.addColumn(Wohnung::getStadt).setHeader(createCustomHeader("Stadt"));
        treeGrid.addColumn(Wohnung::getStrasseMitHausnummer).setHeader(createCustomHeader("Adresse")).setAutoWidth(true);
        if (land.getValue()) treeGrid.addColumn(wohnung -> wohnung.getLand().getName()).setHeader(createCustomHeader("Land"));

        stockwerkColumn = treeGrid.addColumn(wohnung -> {
            return wohnung.getStockwerk() != null ? wohnung.getStockwerk() : "";
        }).setHeader(createCustomHeader("<div>Stock-<br>werk</div>")).setTextAlign(ColumnTextAlign.CENTER);

        wohnungsnummerColumn = treeGrid.addColumn(wohnung -> {
            return wohnung.getWohnungsnummer() != null ? wohnung.getWohnungsnummer() : "";
        }).setHeader(createCustomHeader("<div>Wohnungs-<br>nummer</div>")).setTextAlign(ColumnTextAlign.CENTER);

        // Spalten für Filteroptionen hinzufügen
        if (quadratmeter.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getGesamtQuadratmeter())).setHeader(createCustomHeader("m²"));
        if (baujahr.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getBaujahr())).setHeader(createCustomHeader("Baujahr"));
        if (baeder.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlBaeder()))
                    .setHeader(createCustomHeader("Bäder")).setTextAlign(ColumnTextAlign.CENTER);
        if (schlafzimmer.getValue())
            treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlSchlafzimmer()))
                    .setHeader(createCustomHeader("<div>Schlaf-<br>zimmer</div>"))
                    .setTextAlign(ColumnTextAlign.CENTER);
        if (balkon.getValue()) {
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatBalkon()))
                    .setHeader(createCustomHeader("Balkon"))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setSortable(true)
                    .setComparator((wohnung1, wohnung2) -> Boolean.compare(wohnung1.isHatBalkon(), wohnung2.isHatBalkon()));
        }

        if (terasse.getValue()) {
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatTerrasse()))
                    .setHeader(createCustomHeader("Terrasse"))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setSortable(true)
                    .setComparator((wohnung1, wohnung2) -> Boolean.compare(wohnung1.isHatTerrasse(), wohnung2.isHatTerrasse()));
        }

        if (garten.getValue()) {
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatGarten()))
                    .setHeader(createCustomHeader("Garten"))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setSortable(true)
                    .setComparator((wohnung1, wohnung2) -> Boolean.compare(wohnung1.isHatGarten(), wohnung2.isHatGarten()));
        }

        if (klimaanlage.getValue()) {
            treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatKlimaanlage()))
                    .setHeader(createCustomHeader("<div>Klima-<br>anlage</div>"))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setSortable(true)
                    .setComparator((wohnung1, wohnung2) -> Boolean.compare(wohnung1.isHatKlimaanlage(), wohnung2.isHatKlimaanlage()));
        }

        if (mieter.getValue()) {
            treeGrid.addComponentColumn(wohnung -> {
                if (wohnung.isHeader()) {
                    return new Div();
                } else {
                    Mieter mieter = mietvertragService.findMieterByWohnung(wohnung);
                    if (mieter != null) {
                        RouterLink link = new RouterLink(mieter.getFullName(), MieterDetailsView.class, mieter.getMieter_id());
                        link.getElement().setAttribute("href", link.getHref() + "?previousView=wohnungen");
                        return link;
                    } else {
                        return new Div(new Text("Kein Mieter"));
                    }
                }
            }).setHeader(createCustomHeader("Mieter"));
        }
        treeGrid.setSizeFull();
        treeGrid.setSizeFull();

        treeGrid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        boolean anyExpanded = !expandedNodeIds.isEmpty();
        stockwerkColumn.setVisible(anyExpanded);
        wohnungsnummerColumn.setVisible(anyExpanded);
    }

    /**
     * Aktualisiert die Liste der Wohnungen im TreeGrid.
     * Ruft die hierarchische Liste der Wohnungen ab und erweitert die Kopfzeilen.
     */
    private void updateList() {
        List<Wohnung> wohnungen = wohnungService.findWohnungenWithHierarchy(searchField.getValue());
        TableUtils.configureTreeGrid(treeGrid, wohnungen, 50, Wohnung::getSubWohnungen);
        treeGrid.setItems(wohnungen, Wohnung::getSubWohnungen);
        restoreExpandedState();
        wohnungen.stream()
                .filter(wohnung -> expandedNodeIds.contains(wohnung.getUniqueIdentifier()))
                .forEach(treeGrid::expand);
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
            event.getItems().forEach(item -> expandedNodeIds.add(item.getUniqueIdentifier()));
            saveExpandedState();
            updateGridColumns();
        });

        treeGrid.addCollapseListener(event -> {
            event.getItems().forEach(item -> expandedNodeIds.remove(item.getUniqueIdentifier()));
            saveExpandedState();
            updateGridColumns();
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
     * @param value der boolean, ob das Icon ein Häkchen oder ein Kreuz sein soll
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

    /**
     * Erstellt eine benutzerdefinierte Überschrift für die Tabelle.
     *
     * @param text der Text der Überschrift.
     * @return das konfigurierte Div-Element mit der benutzerdefinierten CSS-Klasse.
     */
    private Html createCustomHeader(String text) {
        return new Html("<div class='custom-header'>" + text + "</div>");
    }

    /**
     * Speichert den erweiterten Zustand der TreeGrid-Knoten.
     * Nur gültige (nicht-leere) Knotennamen werden gespeichert.
     */
    private void saveExpandedState() {
        Set<String> validExpandedNodeIds = new HashSet<>();
        for (String nodeId : expandedNodeIds) {
            if (nodeId != null && !nodeId.isEmpty()) {
                validExpandedNodeIds.add(nodeId);
            }
        }
        configurationService.saveExpandedNodes(new ArrayList<>(validExpandedNodeIds));
    }

    /**
     * Stellt den erweiterten Zustand der TreeGrid-Knoten wieder her.
     * Basierend auf dem gespeicherten Zustand wird die Sichtbarkeit der Spalten 'stockwerk' und 'wohnungsnummer' aktualisiert.
     */
    private void restoreExpandedState() {
        expandedNodeIds = new HashSet<>(configurationService.getExpandedNodes());
        boolean anyExpanded = !expandedNodeIds.isEmpty();
        stockwerkColumn.setVisible(anyExpanded);
        wohnungsnummerColumn.setVisible(anyExpanded);
    }
}