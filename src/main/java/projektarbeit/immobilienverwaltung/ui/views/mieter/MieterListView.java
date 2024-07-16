package projektarbeit.immobilienverwaltung.ui.views.mieter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import projektarbeit.immobilienverwaltung.service.*;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "mieter", layout = MainLayout.class)
@PageTitle("Mieter")
@UIScope
public class MieterListView extends VerticalLayout {

    private final MieterService mieterService;
    private final MietvertragService mietvertragService;
    private final WohnungService wohnungService;
    private final DokumentService dokumentService;
    private final ConfigurationService configurationService;

    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    Accordion filter = new Accordion();
    Checkbox name = new Checkbox("Name");
    Checkbox vorname = new Checkbox("Vorname");
    Checkbox telefonnummer = new Checkbox("Telefonnummer");
    Checkbox email = new Checkbox("Email");
    Checkbox einkommen = new Checkbox("Einkommen");
    Checkbox miete = new Checkbox("Miete");
    Checkbox kaution = new Checkbox("Kaution");
    Checkbox anzahlBewohner = new Checkbox("Anzahl Bewohner");
    Checkbox mietbeginn = new Checkbox("Mietbeginn");
    Checkbox mietende = new Checkbox("Mietende");
    Checkbox mietobjekt = new Checkbox("Mietobjekt");

    MieterForm form;
    HorizontalLayout toolbar;

    public MieterListView(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungService, DokumentService dokumentService, ConfigurationService configurationService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;
        this.configurationService = configurationService;

        addClassName("mieter-list");
        setSizeFull();

        configureGrid();
        configureForm();

        HorizontalLayout header = new HorizontalLayout(new H1("Mieter Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);

        add(header, getToolbar(), createFilterAccordion(), getContent());

        updateList();
        updateGridColumns();
        closeEditor();
    }

    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        grid.setVisible(true);
        toolbar.setVisible(true);
        removeClassName("editing");
    }

    private void updateList() {
        List<Mieter> mieterList = mieterService.findAllMieter(filterText.getValue());
        TableUtils.configureGrid(grid, mieterList, 50);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new MieterForm(mieterService, mietvertragService, wohnungService, dokumentService, configurationService);
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

    private void saveMieter(MieterForm.SaveEvent event) {
        mieterService.saveMieter(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteMieter(MieterForm.DeleteEvent event) {
        mieterService.deleteMieter(event.getContact());
        updateList();
        closeEditor();
    }

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
        grid.asSingleSelect().addValueChangeListener(e -> editMieter(e.getValue()));
    }

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

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider) {
        addMietvertragColumn(valueProvider, "Mietbeginn", false, true, false);
    }

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, false, false);
    }

    private void editMieter(Mieter mieter) {
        if (mieter == null) {
            closeEditor();
        } else {
            form.setMieter(mieter);
            form.setVisible(true);
            grid.setVisible(false);
            toolbar.setVisible(false);
            form.loeschen.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addMieterButton = new Button("Mieter hinzufügen");
        addMieterButton.setPrefixComponent(VaadinIcon.PLUS.create());
        addMieterButton.addClickListener(e -> addMieter());

        toolbar = new HorizontalLayout(filterText, addMieterButton);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        toolbar.setFlexGrow(1, filterText);
        return toolbar;
    }

    private Accordion createFilterAccordion() {
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout col1 = new VerticalLayout(telefonnummer, email);
        VerticalLayout col2 = new VerticalLayout(einkommen, miete);
        VerticalLayout col3 = new VerticalLayout(kaution, anzahlBewohner);
        VerticalLayout col4 = new VerticalLayout(mietbeginn, mietende);
        VerticalLayout col5 = new VerticalLayout(mietobjekt);

        name.setValue(true);
        vorname.setValue(true);
        telefonnummer.setValue(true);
        email.setValue(true);
        mietobjekt.setValue(true);

        layout.add(col1, col2, col3, col4, col5);

        name.addValueChangeListener(event -> updateGridColumns());
        vorname.addValueChangeListener(event -> updateGridColumns());
        telefonnummer.addValueChangeListener(event -> updateGridColumns());
        email.addValueChangeListener(event -> updateGridColumns());
        einkommen.addValueChangeListener(event -> updateGridColumns());
        miete.addValueChangeListener(event -> updateGridColumns());
        kaution.addValueChangeListener(event -> updateGridColumns());
        anzahlBewohner.addValueChangeListener(event -> updateGridColumns());
        mietbeginn.addValueChangeListener(event -> updateGridColumns());
        mietende.addValueChangeListener(event -> updateGridColumns());
        mietobjekt.addValueChangeListener(event -> updateGridColumns());

        filter.add("Tabellen Spalten auswählen", layout);
        return filter;
    }

    private void updateGridColumns() {
        grid.removeAllColumns();

        grid.addColumn("name").setHeader("Name");
        grid.addColumn("vorname").setHeader("Vorname");
        if (telefonnummer.getValue()) grid.addColumn("telefonnummer").setHeader("Telefonnummer");
        if (email.getValue()) grid.addColumn("email").setHeader("Email");

        if (einkommen.getValue()) {
            grid.addColumn(new ComponentRenderer<>(item -> {
                Double einkommen = item.getEinkommen();
                String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
                return new Span(formattedEinkommen);
            })).setHeader("Einkommen").setSortable(true);
        }

        if (miete.getValue()) addMietvertragColumn(Mietvertrag::getMiete, "Miete", true);
        if (kaution.getValue()) addMietvertragColumn(Mietvertrag::getKaution, "Kaution", true);
        if (anzahlBewohner.getValue())
            addMietvertragColumn(Mietvertrag::getAnzahlBewohner, "Anzahl<br>Bewohner", false);
        if (mietbeginn.getValue()) addMietvertragColumn(Mietvertrag::getMietbeginn);
        if (mietende.getValue()) addMietvertragColumn(Mietvertrag::getMietende, "Mietende", false, true, true);
        if (mietobjekt.getValue())
            addMietvertragColumn(mietvertrag -> mietvertrag.getWohnung().getFormattedAddress(), "Mietobjekt", false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
    }

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