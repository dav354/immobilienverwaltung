package projektarbeit.immobilienverwaltung.ui.views;

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
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@PermitAll
@Route(value = "wohnungen", layout = MainLayout.class)
@PageTitle("Wohnungen")
@UIScope
public class WohnungListView extends VerticalLayout {

    private final WohnungService wohnungService;
    private final MietvertragService mietvertragService;
    TreeGrid<Wohnung> treeGrid = new TreeGrid<>(Wohnung.class);
    TextField filterText = new TextField();
    WohnungForm form;

    public WohnungListView(WohnungService wohnungService, MietvertragService mietvertragService) {
        this.wohnungService = wohnungService;
        this.mietvertragService = mietvertragService;
        addClassName("list-view");
        setSizeFull();

        configureTreeGrid();
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

    /**
     * Updates the columns in the TreeGrid to display Wohnung (apartment) details.
     * Sets up hierarchical and standard columns, including custom components and formatting.
     */
    private void updateGridColumns() {
        treeGrid.removeAllColumns();

        // Hierarchical column for the land (country) of the Wohnung
        treeGrid.addHierarchyColumn(wohnung -> wohnung.getLand().name()).setHeader("Land");

        // Column for the postal code (PLZ)
        treeGrid.addColumn(Wohnung::getPostleitzahl).setHeader("PLZ");

        // Column for the city (Stadt)
        treeGrid.addColumn(Wohnung::getStadt).setHeader("Stadt");

        // Column for the street and house number
        treeGrid.addColumn(Wohnung::getStrasseMitHausnummer).setHeader("Adresse").setAutoWidth(true);

        // Column for the floor (Stockwerk)
        treeGrid.addColumn(Wohnung::getStockwerk).setHeader(new Html("<div>Stock-<br>werk</div>")).setTextAlign(ColumnTextAlign.CENTER);

        // Column for the apartment number (Wohnungsnummer)
        treeGrid.addColumn(Wohnung::getWohnungsnummer).setHeader(new Html("<div>Wohnungs-<br>nummer</div>")).setTextAlign(ColumnTextAlign.CENTER);

        treeGrid.addColumn(new TextRenderer<>(wohnung -> {
            if (wohnung.isHeader()) {
                return "";
            } else {
                Mieter mieter = mietvertragService.findMieterByWohnung(wohnung); // Assuming you have a service to find Mieter by Wohnung
                return (mieter != null) ? mieter.getFullName() : "Kein Mieter";
            }
        })).setHeader("Mieter");

        // Column for the total square meters (GesamtQuadratmeter)
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getGesamtQuadratmeter())).setHeader("m²");

        // Column for the year of construction (Baujahr)
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getBaujahr())).setHeader("Baujahr");

        // Column for the number of bathrooms (Bäder)
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlBaeder()))
                .setHeader("Bäder").setTextAlign(ColumnTextAlign.CENTER);

        // Column for the number of bedrooms (Schlafzimmer)
        treeGrid.addColumn(wohnung -> wohnung.isHeader() ? "" : Integer.toString(wohnung.getAnzahlSchlafzimmer()))
                .setHeader(new Html("<div>Schlaf-<br>zimmer</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);

        // Column for the balcony availability (Balkon)
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatBalkon()))
                .setHeader("Balkon").setTextAlign(ColumnTextAlign.CENTER);

        // Column for the terrace availability (Terrasse)
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatTerrasse()))
                .setHeader("Terrasse").setTextAlign(ColumnTextAlign.CENTER);

        // Column for the garden availability (Garten)
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatGarten()))
                .setHeader("Garten").setTextAlign(ColumnTextAlign.CENTER);

        // Column for the air conditioning availability (Klimaanlage)
        treeGrid.addComponentColumn(wohnung -> wohnung.isHeader() ? createEmptyIcon() : createIcon(wohnung.isHatKlimaanlage()))
                .setHeader(new Html("<div>Klima-<br>anlage</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);

        // Make all columns auto-width and sortable
        treeGrid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
    }

    /**
     * Updates the list of Wohnungen (apartments) in the TreeGrid.
     * Fetches the hierarchical list of Wohnungen and expands the headers.
     */
    private void updateList() {
        List<Wohnung> wohnungen = wohnungService.findWohnungenWithHierarchy(filterText.getValue());
        treeGrid.setItems(wohnungen, Wohnung::getSubWohnungen);
        wohnungen.stream().filter(Wohnung::isHeader).forEach(treeGrid::expand);
        updateGridColumns();
    }

    /**
     * Configures the TreeGrid for displaying Wohnungen (apartments).
     * Sets class names, size, columns, and value change listeners.
     */
    private void configureTreeGrid() {
        treeGrid.addClassNames("wohnung-grid");
        treeGrid.setSizeFull();

        updateGridColumns();

        // Add a value change listener to handle the selection of a Wohnung
        treeGrid.asSingleSelect().addValueChangeListener(event -> editWohnung(event.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(treeGrid, form);
        content.setFlexGrow(2, treeGrid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new WohnungForm(wohnungService.findAllMieter(), mietvertragService);
        form.setWidth("200em");

        //Wartet darauf das die Knöpfe gedrückt werden und führt dann die passende Methode aus
        form.addListener(WohnungForm.SaveEvent.class, this::saveWohnung);
        form.addListener(WohnungForm.DeleteEvent.class, this::deleteWohnung);
        form.addListener(WohnungForm.CloseEvent.class, e -> closeEditor());
    }

    //Mieter speichern
    private void saveWohnung(WohnungForm.SaveEvent event) {
        wohnungService.save(event.getWohnung());
        updateList();
        closeEditor();
    }

    //Mieter löschen
    private void deleteWohnung(WohnungForm.DeleteEvent event) {
        wohnungService.delete(event.getWohnung());
        updateList();
        closeEditor();
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
        treeGrid.asSingleSelect().clear();
        Wohnung neueWohnung = new Wohnung();

        form.clearFields(); // Ensure fields are cleared before setting the new Wohnung
        form.setWohnung(neueWohnung);
        form.loeschen.setVisible(false); // Ensure the delete button is hidden
        form.setVisible(true); // Show the form
        addClassName("editing");
    }

    private Icon createIcon(boolean value) {
        Icon icon = value ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
        icon.setSize("14px");
        return icon;
    }

    /**
     * Creates an empty, invisible icon for use in the TreeGrid.
     * This is used to maintain consistent column alignment when no icon is needed.
     *
     * @return an empty, invisible Icon instance.
     */
    private Icon createEmptyIcon() {
        Icon icon = new Icon();
        icon.setVisible(false);
        return icon;
    }
}