package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.WohnungEditDialog;

import java.util.List;

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
    TextField filterText = new TextField();

    /**
     * Konstruktor für WohnungListView.
     * Initialisiert den Dienst, die Benutzeroberfläche und die Grid-Konfiguration.
     *
     * @param wohnungService   der Dienst für Wohnungsoperationen
     * @param mietvertragService der Dienst für Mietvertragsoperationen
     */
    public WohnungListView(WohnungService wohnungService, MietvertragService mietvertragService, ConfigurationService configurationService) {
        this.wohnungService = wohnungService;
        this.mietvertragService = mietvertragService;
        addClassName("list-view");
        setSizeFull();

        configureTreeGrid();

        HorizontalLayout header = new HorizontalLayout(new H1("Wohnungen Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(header.getComponentAt(0));

        add(header, getToolbar(), getContent());

        updateList();
        this.configurationService = configurationService;
    }

    /**
     * Aktualisiert die Spalten im TreeGrid, um Wohnungsdetails anzuzeigen.
     * Richtet hierarchische und standardmäßige Spalten ein, einschließlich benutzerdefinierter Komponenten und Formatierungen.
     */
    private void updateGridColumns() {
        treeGrid.removeAllColumns();

        treeGrid.addHierarchyColumn(wohnung -> wohnung.getLand().name()).setHeader("Land");
        treeGrid.addColumn(Wohnung::getPostleitzahl).setHeader("PLZ");
        treeGrid.addColumn(Wohnung::getStadt).setHeader("Stadt");
        treeGrid.addColumn(Wohnung::getStrasseMitHausnummer).setHeader("Adresse").setAutoWidth(true);
        treeGrid.addColumn(Wohnung::getStockwerk).setHeader(new Html("<div>Stock-<br>werk</div>")).setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addColumn(Wohnung::getWohnungsnummer).setHeader(new Html("<div>Wohnungs-<br>nummer</div>")).setTextAlign(ColumnTextAlign.CENTER);

        treeGrid.addColumn(new TextRenderer<>(wohnung -> {
            if (wohnung.isHeader()) {
                return "";
            } else {
                Mieter mieter = mietvertragService.findMieterByWohnung(wohnung);
                return (mieter != null) ? mieter.getFullName() : "Kein Mieter";
            }
        })).setHeader("Mieter");

        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getGesamtQuadratmeter())).setHeader("m²");
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getBaujahr())).setHeader("Baujahr");
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlBaeder()))
                .setHeader("Bäder").setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlSchlafzimmer()))
                .setHeader(new Html("<div>Schlaf-<br>zimmer</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatBalkon()))
                .setHeader("Balkon").setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatTerrasse()))
                .setHeader("Terrasse").setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatGarten()))
                .setHeader("Garten").setTextAlign(ColumnTextAlign.CENTER);
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatKlimaanlage()))
                .setHeader(new Html("<div>Klima-<br>anlage</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);

        treeGrid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
    }

    /**
     * Aktualisiert die Liste der Wohnungen im TreeGrid.
     * Ruft die hierarchische Liste der Wohnungen ab und erweitert die Kopfzeilen.
     */
    private void updateList() {
        List<Wohnung> wohnungen = wohnungService.findWohnungenWithHierarchy(filterText.getValue());
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
                UI.getCurrent().navigate(WohnungDetailsView.class, event.getValue().getWohnung_id());
            }
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
        filterText.setPlaceholder("Filter nach Adresse...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Wohnung hinzufügen");
        addContactButton.addClickListener(e -> {
            Wohnung newWohnung = new Wohnung();
            WohnungEditDialog dialog = new WohnungEditDialog(wohnungService, newWohnung, () -> {
                updateList();
                getUI().ifPresent(ui -> ui.navigate(WohnungDetailsView.class, newWohnung.getWohnung_id()));
            },
            configurationService);
            dialog.open();
        });

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
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
        icon.setSize

                ("14px");
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