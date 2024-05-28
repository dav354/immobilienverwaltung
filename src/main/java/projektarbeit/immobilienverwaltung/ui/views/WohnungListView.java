package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

@Route(value = "wohnungen", layout = MainLayout.class)
@PageTitle("Wohnungen")
@UIScope
public class WohnungListView extends VerticalLayout {

    private final WohnungService wohnungService;
    Grid<Wohnung> grid = new Grid<>(Wohnung.class);
    TextField filterText = new TextField();
    WohnungForm form;

    public WohnungListView(WohnungService wohnungService) {
        this.wohnungService = wohnungService;
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
        grid.setItems(wohnungService.findAllWohnungen(filterText.getValue()));
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
        form = new WohnungForm(wohnungService.findAllMieter());
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

        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatBalkon())).setHeader("Balkon").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatTerrasse())).setHeader("Terrasse").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatGarten())).setHeader("Garten").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(wohnung -> createIcon(wohnung.isHatKlimaanlage())).setHeader("Klimaanlage").setTextAlign(ColumnTextAlign.CENTER);

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
}