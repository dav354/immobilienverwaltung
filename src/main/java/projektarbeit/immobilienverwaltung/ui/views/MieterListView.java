package projektarbeit.immobilienverwaltung.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
@PermitAll
@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
public class MieterListView extends VerticalLayout {

    // Einzelnen Bestandteile der Seite
    private final MieterService mieterService;
    private final MietvertragService mietvertragService;
    private final WohnungService wohnungService;

    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    MieterForm form;

    // Der Aufbau der Seite mit Überschrift, Eingabe und der Tabelle
    public MieterListView(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungService = wohnungService;

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

    // Dass der Editor beim Start der Seite geschlossen ist
    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    // Um die Liste zu Updaten
    private void updateList() {
        grid.setItems(mieterService.findAllMieter(filterText.getValue()));
    }

    // Wie die Tabelle und der Editor auf der Seite aufgebaut werden
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    // Einstellungen des Forms für die Erstellung/Bearbeitung der Mieter
    private void configureForm() {
        assert wohnungService != null;
        form = new MieterForm(mieterService, mietvertragService, wohnungService);
        form.setWidth("200em");
        // Wartet darauf das die Knöpfe gedrückt werden und führt dann die passende Methode aus
        form.addListener(MieterForm.SaveEvent.class, this::saveMieter);
        form.addListener(MieterForm.DeleteEvent.class, this::deleteMieter);
        form.addListener(MieterForm.CloseEvent.class, e -> closeEditor());
    }

    // Mieter speichern
    private void saveMieter(MieterForm.SaveEvent event) {
        mieterService.saveMieter(event.getContact());
        updateList();
        closeEditor();
    }

    // Mieter löschen
    private void deleteMieter(MieterForm.DeleteEvent event) {
        mieterService.deleteMieter(event.getContact());
        updateList();
        closeEditor();
    }

    /**
     * Configures the grid for displaying Mieter (tenants) with their corresponding Mietverträge (rental contracts).
     * Each tenant's rental information is displayed in separate columns with formatted data.
     */
    private void configureGrid() {
        grid.addClassNames("mieter-grid");
        grid.setSizeFull();

        grid.setColumns("name", "vorname", "telefonnummer", "email");

        // Add einkommen with German Formatting
        grid.addColumn(new ComponentRenderer<>(item -> {
            Double einkommen = item.getEinkommen();
            String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
            return new Span(formattedEinkommen);
        })).setHeader("Einkommen").setSortable(true);

        // Add Miete column with formatting
        addMietvertragColumn(Mietvertrag::getMiete, "Miete", true);

        // Add Kaution column with formatting
        addMietvertragColumn(Mietvertrag::getKaution, "Kaution", true);

        // Add Anzahl Bewohner column with formatting
        addMietvertragColumn(Mietvertrag::getAnzahlBewohner, "Anzahl<br>Bewohner", false);

        // Add Mietbeginn
        addMietvertragColumn(Mietvertrag::getMietbeginn, "Mietbeginn", false, true);

        // Add Mietende
        addMietvertragColumn(Mietvertrag::getMietende, "Mietende", false, true, true);

        // Spalte für die vollständige Adresse hinzufügen
        addMietvertragColumn(mietvertrag -> mietvertrag.getWohnung().getFormattedAddress(), "Mietobjekt", false);

        // Automatically resize columns and allow sorting
        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
        grid.asSingleSelect().addValueChangeListener(e -> editMieter(e.getValue()));
    }

    /**
     * Adds a column to the grid for displaying a specific Mietvertrag attribute for each Mieter.
     *
     * @param valueProvider A function that retrieves the attribute value from a Mietvertrag.
     * @param headerHtml    The HTML header for the column.
     * @param isCurrency    Whether the value is a currency and should be formatted as such.
     * @param isDate        Whether the value is a date and should be formatted as such.
     */
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency, boolean isDate, boolean isMietende) {
        grid.addColumn(new ComponentRenderer<>(mieter -> {
                    // Holt alle Mietverträge des Mieters
                    List<Mietvertrag> mietvertraege = mietvertragService.findByMieter(mieter.getMieter_id());

                    // Erstellt den Inhalt für die Zelle, indem die Werte der Mietverträge verarbeitet und formatiert werden
                    String content = mietvertraege.isEmpty() ? "" : mietvertraege.stream()
                            .map(mietvertrag -> {
                                // Holt den Wert aus dem Mietvertrag basierend auf dem angegebenen ValueProvider
                                T value = valueProvider.apply(mietvertrag);

                                // Formatiert den Wert basierend auf den Parametern isCurrency, isDate und isMietende
                                if (isCurrency) {
                                    // Formatiert den Wert als Währung
                                    return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(value);
                                } else if (isDate && value instanceof LocalDate) {
                                    // Formatiert das Datum im deutschen Format
                                    return ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY));
                                } else if (isMietende && value == null && mietvertrag.getMietbeginn() != null) {
                                    // Zeigt "Unbefristet" an, wenn Mietende null und Mietbeginn gesetzt ist
                                    return "Unbefristet";
                                } else {
                                    // Wandelt den Wert in einen String um, wenn er nicht null ist
                                    return value != null ? value.toString() : "";
                                }
                            })
                            // Verbindet die formatierten Werte mit Zeilenumbrüchen
                            .collect(Collectors.joining("<br>"));

                    // Erstellt ein Span-Element und setzt den HTML-Inhalt
                    Span span = new Span();
                    span.getElement().setProperty("innerHTML", content);
                    return span;
                }))
                // Setzt den Header der Spalte und richtet den Text in der Mitte aus
                .setHeader(new Html("<div>" + headerHtml + "</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);
    }

    /**
     * Overloaded method to add a column to the grid for displaying a specific Mietvertrag attribute for each Mieter.
     *
     * @param valueProvider A function that retrieves the attribute value from a Mietvertrag.
     * @param headerHtml    The HTML header for the column.
     * @param isCurrency    Indicates if the value should be formatted as currency.
     * @param isDate        Indicates if the value should be formatted as a date.
     */
    @SuppressWarnings("unused")
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency, boolean isDate) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, isDate, false);
    }

    /**
     * Overloaded method to add a column to the grid for displaying a specific Mietvertrag attribute for each Mieter.
     *
     * @param valueProvider A function that retrieves the attribute value from a Mietvertrag.
     * @param headerHtml    The HTML header for the column.
     * @param isCurrency    Indicates if the value should be formatted as currency.
     */
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, false, false);
    }

    // Mieter bearbeiten funktion
    private void editMieter(Mieter mieter) {
        if (mieter == null) {
            closeEditor();
        } else {
            form.setMieter(mieter);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    // Erstellung des Suchfelds und des Buttons
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

    // Mieter hinzufügen
    private void addMieter() {
        grid.asSingleSelect().clear();
        Mieter neuerMieter = new Mieter();
        form.setMieter(neuerMieter); // Call setMieter with a new Mieter object
        form.loeschen.setVisible(false); // Ensure the delete button is hidden
        editMieter(neuerMieter);
    }
}