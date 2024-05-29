package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.data.renderer.NumberRenderer;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.text.NumberFormat;
import java.util.Locale;

@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
public class MieterListView extends VerticalLayout {

    //Einzelnen Bestandteile der Seite
    private final MieterService mieterService;
    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    MieterForm form;

    //Der Aufbau der Seite mit Überschrift, Eingabe und der Tabelle
    public MieterListView(MieterService mieterService) {
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
        form = new MieterForm(mieterService);
        form.setWidth("200em");
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
        grid.setColumns("name", "vorname", "telefonnummer", "mietbeginn", "mietende");

        // Einkommen-Spalte mit Formatierung hinzufügen
        grid.addColumn(new NumberRenderer<>(
                        Mieter::getEinkommen,
                        NumberFormat.getCurrencyInstance(Locale.GERMANY)))
                .setHeader("Einkommen")
                .setAutoWidth(true)
                .setSortable(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        // Kaution-Spalte mit Formatierung hinzufügen
        grid.addColumn(new NumberRenderer<>(
                        Mieter::getKaution,
                        NumberFormat.getCurrencyInstance(Locale.GERMANY)))
                .setHeader("Kaution")
                .setAutoWidth(true)
                .setSortable(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        // Add column with line break in header
        grid.addColumn(Mieter::getAnzahlBewohner).setHeader(new Html("<div>Anzahl<br>Bewohner</div>")).setTextAlign(ColumnTextAlign.CENTER);

        // Adding column for the complete address using getFormattedAddress with HTML support
        grid.addColumn(new ComponentRenderer<>(mieter -> {
            Span span = new Span();
            span.getElement().setProperty("innerHTML", mieter.getFormattedWohnung());
            return span;
        })).setHeader("Mietobjekt");

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

    //Erstellung des Suchfelds und des Buttons
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
        Mieter neuerMieter = new Mieter();
        form.setMieter(neuerMieter); // Call setMieter with a new Mieter object
        form.loeschen.setVisible(false); // Ensure the delete button is hidden
        editMieter(neuerMieter);
    }
}