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
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.MieterEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungDetailsView;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static projektarbeit.immobilienverwaltung.ui.components.TableUtils.createCustomHeader;

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

    HorizontalLayout toolbar;

    public MieterListView(MieterService mieterService, MietvertragService mietvertragService, ConfigurationService configurationService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.configurationService = configurationService;

        addClassName("mieter-list");
        setSizeFull();

        configureGrid();

        HorizontalLayout header = new HorizontalLayout(new H1("Mieter Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        Html helpText = new Html("<span>Um mehr Infos zu den Mietern zu bekommen, in die entsprechende Zeile klicken.</span>");

        add(header, helpText, getToolbar(), createFilterAccordion(), getContent());

        updateList();
        updateGridColumns();
    }

    private void updateList() {
        List<Mieter> mieterList = mieterService.findAllMieter(filterText.getValue());
        TableUtils.configureGrid(grid, mieterList, 50);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassNames("mieter-grid");
        grid.setSizeFull();

        grid.setColumns("name", "vorname", "telefonnummer", "email");

        grid.addColumn(new ComponentRenderer<>(item -> {
            Double einkommen = item.getEinkommen();
            String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
            return new Span(formattedEinkommen);
        })).setHeader(createCustomHeader("Einkommen")).setSortable(true);

        addMietvertragColumn(Mietvertrag::getMiete, "Miete", true);
        addMietvertragColumn(Mietvertrag::getKaution, "Kaution", true);
        addMietvertragColumn(Mietvertrag::getAnzahlBewohner, "Anzahl<br>Bewohner", false);
        addMietvertragColumn(Mietvertrag::getMietbeginn);
        addMietvertragColumn(Mietvertrag::getMietende, "Mietende", false, true, true);
        addMietvertragColumn(mietvertrag -> mietvertrag.getWohnung() != null ? mietvertrag.getWohnung().getFormattedAddress() : "Keine Wohnung", "Mietobjekt", false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                getUI().ifPresent(ui -> ui.navigate(MieterDetailsView.class, e.getValue().getMieter_id()));
            }
        });
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
                            return value != null ? value.toString() : "Keine Wohnung";
                        }
                    })
                    .collect(Collectors.joining("<br>"));

            Span span = new Span();
            span.getElement().setProperty("innerHTML", content);
            return span;
        })).setHeader(createCustomHeader(headerHtml));
    }

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider) {
        addMietvertragColumn(valueProvider, "Mietbeginn", false, true, false);
    }

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, false, false);
    }

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

    private void updateGridColumns() {
        grid.removeAllColumns();

        grid.addColumn(Mieter::getName).setHeader(createCustomHeader("Name")).setSortable(true);
        grid.addColumn(Mieter::getVorname).setHeader(createCustomHeader("Vorname")).setSortable(true);
        if (telefonnummer.getValue())
            grid.addColumn(Mieter::getTelefonnummer).setHeader(createCustomHeader("Telefonnummer")).setSortable(true);
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

        grid.addColumn(new ComponentRenderer<>(mieter -> {
            List<Mietvertrag> mietvertraege = mietvertragService.findByMieter(mieter.getMieter_id());

            String content = mietvertraege.isEmpty() ? "Keine Wohnung" : mietvertraege.stream()
                    .map(mietvertrag -> {
                        Wohnung wohnung = mietvertrag.getWohnung();
                        if (wohnung != null) {
                            RouterLink link = new RouterLink(wohnung.getFormattedAddress(), WohnungDetailsView.class, wohnung.getWohnung_id());
                            link.getElement().setAttribute("href", link.getHref() + "?previousView=mieter-list");
                            return link.getElement().getOuterHTML();
                        } else {
                            return "Keine Wohnung";
                        }
                    })
                    .collect(Collectors.joining("<br>"));

            Span span = new Span();
            span.getElement().setProperty("innerHTML", content);
            return span;
        })).setHeader(createCustomHeader("Mietobjekt")).setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
    }

    private void addMieter() {
        grid.asSingleSelect().clear();
        Mieter neuerMieter = new Mieter();

        MieterEditDialog dialog = new MieterEditDialog(mieterService, neuerMieter, () -> {
            updateList();
            getUI().ifPresent(ui -> ui.navigate(MieterDetailsView.class, neuerMieter.getMieter_id()));
        }, configurationService);
        dialog.open();
    }

    private VerticalLayout createVerticalLayoutWithSpacing(Component... components) {
        VerticalLayout layout = new VerticalLayout(components);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(true);
        return layout;
    }
}
