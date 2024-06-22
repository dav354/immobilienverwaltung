package projektarbeit.immobilienverwaltung.ui.views.mieter;

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

    Grid<Mieter> grid = new Grid<>(Mieter.class);
    TextField filterText = new TextField();
    MieterForm form;
    HorizontalLayout toolbar;

    public MieterListView(MieterService mieterService, MietvertragService mietvertragService, WohnungService wohnungService, DokumentService dokumentService) {
        this.mieterService = mieterService;
        this.mietvertragService = mietvertragService;
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;

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

    private void closeEditor() {
        form.setMieter(null);
        form.setVisible(false);
        grid.setVisible(true);
        toolbar.setVisible(true);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(mieterService.findAllMieter(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

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

        grid.setColumns("name", "vorname", "telefonnummer", "email");

        grid.addColumn(new ComponentRenderer<>(item -> {
            Double einkommen = item.getEinkommen();
            String formattedEinkommen = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(einkommen);
            return new Span(formattedEinkommen);
        })).setHeader("Einkommen").setSortable(true);

        addMietvertragColumn(Mietvertrag::getMiete, "Miete", true);
        addMietvertragColumn(Mietvertrag::getKaution, "Kaution", true);
        addMietvertragColumn(Mietvertrag::getAnzahlBewohner, "Anzahl<br>Bewohner", false);
        addMietvertragColumn(Mietvertrag::getMietbeginn, "Mietbeginn", false, true);
        addMietvertragColumn(Mietvertrag::getMietende, "Mietende", false, true, true);
        addMietvertragColumn(mietvertrag -> mietvertrag.getWohnung().getFormattedAddress(), "Mietobjekt", false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true).setSortable(true));
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

    private <T> void addMietvertragColumn(ValueProvider<Mietvertrag, T> valueProvider, String headerHtml, boolean isCurrency, boolean isDate) {
        addMietvertragColumn(valueProvider, headerHtml, isCurrency, isDate, false);
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
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Mieter hinzufügen");
        addContactButton.addClickListener(e -> addMieter());

        toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addMieter() {
        grid.asSingleSelect().clear();
        Mieter neuerMieter = new Mieter();
        form.setMieter(neuerMieter);
        form.loeschen.setVisible(false);
        form.setVisible(true);
        grid.setVisible(false);
        toolbar.setVisible(false);
        addClassName("editing");
    }
}
