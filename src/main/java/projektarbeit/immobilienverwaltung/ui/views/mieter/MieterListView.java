package projektarbeit.immobilienverwaltung.ui.views.mieter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import projektarbeit.immobilienverwaltung.ui.views.dialog.MieterEditDialog;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Die MieterListView zeigt eine Liste der Mieter an.
 * Sie ermöglicht das Filtern, Hinzufügen und Bearbeiten von Mieterdaten.
 */
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
        Html helpText = new Html("<span>Um mehr Infos zu den Mietern zu bekommen, in die entsprechende Zeile klicken.</span>");

        add(header, helpText, getToolbar(), createFilterAccordion(), getContent());

        updateList();
        updateGridColumns();
        closeEditor();
    }

    /**
     * Schließt den Editor und stellt die Sichtbarkeit der Gitter und Werkzeugleiste wieder her.
     */
    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        grid.setVisible(true);
        toolbar.setVisible(true);
        removeClassName("editing");
    }

    /**
     * Aktualisiert die Liste der Mieter basierend auf dem Filtertext.
     */
    private void updateList() {
        List<Mieter> mieterList = mieterService.findAllMieter(filterText.getValue());
        TableUtils.configureGrid(grid, mieterList, 50);
    }

    /**
     * Erstellt das Inhaltslayout, das das Gitter und das Formular enthält.
     *
     * @return das konfigurierte Inhaltslayout
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    /**
     * Konfiguriert das Formular für die Mieterbearbeitung.
     */
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

    /**
     * Speichert die Änderungen an einem Mieter.
     *
     * @param event das SaveEvent, das die Änderungen enthält
     */
    private void saveMieter(MieterForm.SaveEvent event) {
        mieterService.saveMieter(event.getContact());
        updateList();
        closeEditor();
    }

    /**
     * Löscht einen Mieter.
     *
     * @param event das DeleteEvent, das den zu löschenden Mieter enthält
     */
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
        })).setHeader(createCustomHeader("Einkommen")).setSortable(true);

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
        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                getUI().ifPresent(ui -> ui.navigate(MieterDetailsView.class, e.getValue().getMieter_id()));
            }
        });
    }

    /**
     * Fügt eine Spalte zum Gitter hinzu, die Informationen aus dem Mietvertrag anzeigt.
     *
     * @param valueProvider der Wertelieferant für die Spalte
     * @param headerHtml    die HTML-Überschrift der Spalte
     * @param isCurrency    ob der Wert als Währung formatiert werden soll
     * @param isDate        ob der Wert als Datum formatiert werden soll
     * @param isMietende    ob es sich um das Mietende handelt (für spezielle Formatierung)
     * @param <T>           der Typ des Wertes
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
                })).setHeader(new Html("<div>" + headerHtml + "</div>"));
    }

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider) {
        addMietvertragColumn(valueProvider, "Mietbeginn", false, true, false);
    }

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, false, false);
    }

    /**
     * Erstellt die Werkzeugleiste mit Filterfeld und Button zum Hinzufügen eines Mieters.
     *
     * @return die konfigurierte Werkzeugleiste
     */
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter nach Name...");
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

    /**
     * Erstellt ein Akkordeon für Filteroptionen.
     *
     * @return das konfigurierte Akkordeon
     */
    private Accordion createFilterAccordion() {
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout col1 = createVerticalLayoutWithSpacing(telefonnummer, email);
        VerticalLayout col2 = createVerticalLayoutWithSpacing(einkommen, miete);
        VerticalLayout col3 = createVerticalLayoutWithSpacing(kaution, mietobjekt);
        VerticalLayout col4 = createVerticalLayoutWithSpacing(mietbeginn, mietende);
        VerticalLayout col5 = createVerticalLayoutWithSpacing(anzahlBewohner);

        name.setValue(configurationService.getCheckboxState("nameCheckbox", true));
        vorname.setValue(configurationService.getCheckboxState("vornameCheckbox", true));
        telefonnummer.setValue(configurationService.getCheckboxState("telefonnummerCheckbox", true));
        email.setValue(configurationService.getCheckboxState("emailCheckbox", true));
        einkommen.setValue(configurationService.getCheckboxState("einkommenCheckbox", false));
        miete.setValue(configurationService.getCheckboxState("mieteCheckbox", false));
        kaution.setValue(configurationService.getCheckboxState("kautionCheckbox", false));
        anzahlBewohner.setValue(configurationService.getCheckboxState("anzahlBewohnerCheckbox", false));
        mietbeginn.setValue(configurationService.getCheckboxState("mietbeginnCheckbox", false));
        mietende.setValue(configurationService.getCheckboxState("mietendeCheckbox", false));
        mietobjekt.setValue(configurationService.getCheckboxState("mietobjektCheckbox", true));

        layout.add(col1, col2, col3, col4, col5);

        name.addValueChangeListener(event -> {
            configurationService.setCheckboxState("nameCheckbox", name.getValue());
            updateGridColumns();
        });
        vorname.addValueChangeListener(event -> {
            configurationService.setCheckboxState("vornameCheckbox", vorname.getValue());
            updateGridColumns();
        });
        telefonnummer.addValueChangeListener(event -> {
            configurationService.setCheckboxState("telefonnummerCheckbox", telefonnummer.getValue());
            updateGridColumns();
        });
        email.addValueChangeListener(event -> {
            configurationService.setCheckboxState("emailCheckbox", email.getValue());
            updateGridColumns();
        });
        einkommen.addValueChangeListener(event -> {
            configurationService.setCheckboxState("einkommenCheckbox", einkommen.getValue());
            updateGridColumns();
        });
        miete.addValueChangeListener(event -> {
            configurationService.setCheckboxState("mieteCheckbox", miete.getValue());
            updateGridColumns();
        });
        kaution.addValueChangeListener(event -> {
            configurationService.setCheckboxState("kautionCheckbox", kaution.getValue());
            updateGridColumns();
        });
        anzahlBewohner.addValueChangeListener(event -> {
            configurationService.setCheckboxState("anzahlBewohnerCheckbox", anzahlBewohner.getValue());
            updateGridColumns();
        });
        mietbeginn.addValueChangeListener(event -> {
            configurationService.setCheckboxState("mietbeginnCheckbox", mietbeginn.getValue());
            updateGridColumns();
        });
        mietende.addValueChangeListener(event -> {
            configurationService.setCheckboxState("mietendeCheckbox", mietende.getValue());
            updateGridColumns();
        });
        mietobjekt.addValueChangeListener(event -> {
            configurationService.setCheckboxState("mietobjektCheckbox", mietobjekt.getValue());
            updateGridColumns();
        });

        filter.add("Tabellen Spalten auswählen", layout);

        boolean isExpanded = configurationService.getAccordionState("mieterFilterAccordionExpanded", true);
        if (isExpanded) {
            filter.open(0);
        } else {
            filter.close();
        }

        filter.addOpenedChangeListener(event -> {
            int openedIndex = event.getOpenedIndex().orElse(-1);
            boolean isOpened = openedIndex == 0;
            configurationService.setAccordionState("mieterFilterAccordionExpanded", isOpened);
        });

        return filter;
    }

    /**
     * Aktualisiert die Spalten im Gitter basierend auf den ausgewählten Filteroptionen.
     */
    private void updateGridColumns() {
        grid.removeAllColumns();

        grid.addColumn(Mieter::getName).setHeader(createCustomHeader("Name")).setSortable(true);
        grid.addColumn(Mieter::getVorname).setHeader(createCustomHeader("Vorname")).setSortable(true);
        if (telefonnummer.getValue()) grid.addColumn(Mieter::getTelefonnummer).setHeader(createCustomHeader("Telefonnummer")).setSortable(true);
        if (email.getValue()) grid.addColumn(Mieter::getEmail).setHeader(createCustomHeader("Email")).setSortable(true);

        if (einkommen.getValue()) {
            grid.addColumn(new ComponentRenderer<>(item -> {
                Double einkommen = item.getEinkommen();
                String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
                return new Span(formattedEinkommen);
            })).setHeader(createCustomHeader("Einkommen")).setSortable(true);
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

    /**
     * Fügt einen neuen Mieter hinzu.
     */
    private void addMieter() {
        grid.asSingleSelect().clear();
        Mieter neuerMieter = new Mieter();

        MieterEditDialog dialog = new MieterEditDialog(mieterService, neuerMieter, () -> {
            updateList();
            getUI().ifPresent(ui -> ui.navigate(MieterDetailsView.class, neuerMieter.getMieter_id()));
        }, configurationService);
        dialog.open();
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
     * Erstellt ein VerticalLayout mit festgelegtem Abstand zwischen den Komponenten.
     *
     * @param components die Komponenten, die dem Layout hinzugefügt werden sollen.
     * @return das konfigurierte VerticalLayout.
     */
    private VerticalLayout createVerticalLayoutWithSpacing(Component... components) {
        VerticalLayout layout = new VerticalLayout(components);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(true);
        return layout;
    }
}