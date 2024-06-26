package projektarbeit.immobilienverwaltung.ui.views.mieter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
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
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.service.DokumentService;
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

/**
 * Diese Klasse repräsentiert die Ansicht zur Verwaltung von Mietern.
 * Sie zeigt eine Liste der Mieter in einer Grid-Komponente an und ermöglicht deren Bearbeitung.
 */
@SuppressWarnings("SpellCheckingInspection")
@PermitAll
@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
public class MieterListView extends VerticalLayout {

    private final MieterService mieterService;
    private final MietvertragService mietvertragService;
    private final WohnungService wohnungService;
    private final DokumentService dokumentService;
    private final MieterRepository mieterRepository;

    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    MieterForm form;
    HorizontalLayout toolbar;

    public MieterListView(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungService, DokumentService dokumentService, MieterRepository mieterRepository) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;
        this.mieterRepository = mieterRepository;


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

    /**
     * Schließt den Editor für die Mieterform und zeigt die Liste an.
     * Wird aufgerufen, wenn die Bearbeitung eines Mieters abgeschlossen ist.
     */
    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        grid.setVisible(true);
        toolbar.setVisible(true);
        removeClassName("editing");
    }

    /**
     * Aktualisiert die angezeigte Liste der Mieter in der Grid-Komponente basierend auf dem aktuellen Filtertext.
     * Verwendet den Mieter-Service, um alle Mieter zu laden, die dem Filtertext entsprechen.
     */
    private void updateList() {
        grid.setItems(mieterService.findAllMieter(filterText.getValue()));
    }

    /**
     * Erstellt den Hauptinhalt der Mieterlistenansicht, der aus der Grid-Komponente und dem Mieter-Formular besteht.
     *
     * @return Eine HorizontalLayout-Komponente mit der Grid und dem Mieter-Formular.
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    /**
     * Konfiguriert das Mieter-Formular für die Bearbeitung von Mieterdaten.
     * Setzt die Breite auf 100% und setzt es initial auf unsichtbar.
     * Fügt Listener für Speichern, Löschen und Schließen hinzu, um entsprechende Aktionen auszuführen.
     */
    private void configureForm() {
        form = new MieterForm(mieterService, mietvertragService, wohnungService, dokumentService);
        form.setWidth("100%");
        form.setVisible(false);

        form.addListener(MieterForm.SaveEvent.class, event -> {
            saveMieter(event);
            closeEditor();
        });
        form.addListener(MieterForm.DeleteEvent.class, event -> {
            deleteMieter(event);
            closeEditor();
        });
        form.addListener(MieterForm.CloseEvent.class, event -> closeEditor());
    }

    /**
     * Speichert den Mieter, der im SaveEvent übergeben wird, über den Mieter-Service.
     * Aktualisiert anschließend die angezeigte Liste der Mieter und schließt den Editor.
     *
     * @param event Das SaveEvent, das den zu speichernden Mieter enthält.
     */
    private void saveMieter(MieterForm.SaveEvent event) {
        mieterService.saveMieter(event.getContact());
        updateList();
        closeEditor();
    }

    /**
     * Löscht den Mieter, der im DeleteEvent übergeben wird, über den Mieter-Service.
     * Aktualisiert anschließend die angezeigte Liste der Mieter und schließt den Editor.
     *
     * @param event Das DeleteEvent, das den zu löschenden Mieter enthält.
     */
    private void deleteMieter(MieterForm.DeleteEvent event) {
        mieterService.deleteMieter(event.getContact());
        updateList();
        closeEditor();
    }

    /**
     * Konfiguriert die Grid-Komponente für die Anzeige der Mieterdaten.
     * Setzt Klassenattribute, Spalten, und Spaltenrenderer für spezifische Mieter- und Mietvertragsdaten.
     * Fügt Listener hinzu, um Mieter für die Bearbeitung auszuwählen.
     */
    private void configureGrid() {
        grid.addClassNames("mieter-grid");
        grid.setSizeFull();

        // Standard-Mieterdaten-Spalten
        grid.setColumns("name", "vorname", "telefonnummer", "email");

        // Spalte für das Einkommen mit Währungsformatierung
        grid.addColumn(new ComponentRenderer<>(item -> {
            Double einkommen = item.getEinkommen();
            String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
            return new Span(formattedEinkommen);
        })).setHeader("Einkommen").setSortable(true);

        // Zusätzliche Mietvertrags-Spalten hinzufügen
        addMietvertragColumn(Mietvertrag::getMiete, "Miete", true);
        addMietvertragColumn(Mietvertrag::getKaution, "Kaution", true);
        addMietvertragColumn(Mietvertrag::getAnzahlBewohner, "Anzahl<br>Bewohner", false);
        addMietvertragColumn(Mietvertrag::getMietbeginn);
        addMietvertragColumn(Mietvertrag::getMietende, "Mietende", false, true, true);
        addMietvertragColumn(mietvertrag -> mietvertrag.getWohnung().getFormattedAddress(), "Mietobjekt", false);

        // Automatische Breitenanpassung und Sortierbarkeit für alle Spalten
        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        // Listener für die Auswahl eines Mieters zum Bearbeiten hinzufügen
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate("mieter-details/" + event.getValue().getMieter_id());
            }
        });
    }

    /**
     * Fügt eine neue Spalte zur Grid-Komponente hinzu, die Mietvertragsdaten darstellt.
     * Die Spalte wird basierend auf dem ValueProvider für den angegebenen Mieter erstellt.
     * Die Darstellung der Spalte kann je nach Parametern formatiert werden: Währung, Datum und besondere Fälle wie "Unbefristet".
     *
     * @param valueProvider Der ValueProvider, der den Wert aus einem Mietvertrag extrahiert.
     * @param headerHtml    Der Header der Spalte als HTML-String.
     * @param isCurrency    Gibt an, ob der Wert als Währung formatiert werden soll.
     * @param isDate        Gibt an, ob der Wert ein Datum ist und entsprechend formatiert werden soll.
     * @param isMietende    Gibt an, ob der Wert das Mietende darstellt und besondere Behandlung erfordert.
     *                      Wenn true und der Wert ist null, wird "Unbefristet" angezeigt.
     *                      Dieser Parameter wird ignoriert, wenn isDate false ist.
     */
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency, boolean isDate, boolean isMietende) {
        grid.addColumn(new ComponentRenderer<>(mieter -> {
                    List<Mietvertrag> mietvertraege = mietvertragService.findByMieter(mieter.getMieter_id());

                    String content = mietvertraege.isEmpty() ? "" : mietvertraege.stream()
                            .map(mietvertrag -> {
                                T value = valueProvider.apply(mietvertrag);

                                if (isCurrency) {
                                    return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(value);
                                } else if (isDate && value instanceof LocalDate) {
                                    return ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY));
                                } else if (isMietende && value == null && mietvertrag.getMietbeginn() != null) {
                                    return "Unbefristet";
                                } else {
                                    return value != null ? value.toString() : "";
                                }
                            })
                            .collect(Collectors.joining("<br>"));

                    Span span = new Span();
                    span.getElement().setProperty("innerHTML", content);
                    return span;
                })).setHeader(new Html("<div>" + headerHtml + "</div>"))
                .setTextAlign(ColumnTextAlign.CENTER);
    }

    /**
     * Fügt eine neue Spalte zur Grid-Komponente hinzu, die Mietvertragsdaten darstellt.
     * Die Spalte wird basierend auf dem ValueProvider für den angegebenen Mieter erstellt.
     * Die Darstellung der Spalte kann je nach Parametern formatiert werden: Währung und Datum.
     *
     * @param valueProvider Der ValueProvider, der den Wert aus einem Mietvertrag extrahiert.
     */
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider) {
        addMietvertragColumn(valueProvider, "Mietbeginn", false, true, false);
    }

    /**
     * Fügt eine neue Spalte zur Grid-Komponente hinzu, die Mietvertragsdaten darstellt.
     * Die Spalte wird basierend auf dem ValueProvider für den angegebenen Mieter erstellt.
     * Die Darstellung der Spalte kann je nach Parametern formatiert werden: Währung.
     *
     * @param valueProvider Der ValueProvider, der den Wert aus einem Mietvertrag extrahiert.
     * @param headerHtml    Der Header der Spalte als HTML-String.
     * @param isCurrency    Gibt an, ob der Wert als Währung formatiert werden soll.
     */
    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, false, false);
    }

    /**
     * Erstellt und gibt die Toolbar-Komponente zurück, die Filter- und Hinzufügen-Buttons enthält.
     *
     * @return Die horizontal angeordnete Toolbar-Komponente.
     */
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Mieter hinzufügen");
        addContactButton.addClickListener(e -> addMieter());

        toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    /**
     * Öffnet den Editor für das Hinzufügen eines neuen Mieters.
     * Setzt die Grid-Auswahl zurück und zeigt den Editor an.
     */
    private void addMieter() {
        grid.asSingleSelect().clear();

        form.loeschen.setVisible(false);
        form.setVisible(true);
        grid.setVisible(false);
        toolbar.setVisible(false);
        Mieter neuerMieter = new Mieter();
        form.setMieter(neuerMieter);
        addClassName("editing");
    }

}