package projektarbeit.immobilienverwaltung.ui.views.dokumente;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.DokumentService;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterDetailsView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungDetailsView;

import java.util.List;

import static projektarbeit.immobilienverwaltung.ui.components.TableUtils.createCustomHeader;

/**
 * Die DokumenteListView ist die Ansicht, die alle Dokumente in einer Tabelle darstellt.
 * Sie ermöglicht das Filtern und Durchsuchen der Dokumente nach Mietern oder Wohnungen.
 */
@PermitAll
@Route(value = "dokumente", layout = MainLayout.class)
@PageTitle("Dokumente")
@UIScope
public class DokumenteListView extends VerticalLayout {

    private final WohnungService wohnungService;
    private final ConfigurationService configurationService;
    private final DokumentService dokumentService;
    private final MieterService mieterService;

    private final ComboBox<String> filterTypeComboBox = new ComboBox<>("Filter by");
    private final ComboBox<Mieter> mieterComboBox = new ComboBox<>("Mieter");
    private final ComboBox<Wohnung> wohnungComboBox = new ComboBox<>("Wohnung");
    private final ComboBox<?> dummy = new ComboBox<>();
    private final TextField searchField = new TextField("Search");
    private final Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class, false);

    /**
     * Konstruktor für DokumenteListView.
     * Initialisiert die Dienste, die Benutzeroberfläche und die Grid-Konfiguration.
     *
     * @param wohnungService       der Dienst für Wohnungsoperationen
     * @param dokumentService      der Dienst für Dokumentenoperationen
     * @param configurationService der Dienst für Konfigurationseinstellungen
     * @param mieterService        der Dienst für Mieteroperationen
     */
    public DokumenteListView(WohnungService wohnungService, DokumentService dokumentService, ConfigurationService configurationService, MieterService mieterService) {
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;
        this.configurationService = configurationService;
        this.mieterService = mieterService;

        HorizontalLayout header = new HorizontalLayout(new H1("Dokumente Übersicht"));
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(header.getComponentAt(0));

        Html helpText = new Html("<span>Für detailliertere Informationen auf den Mieter oder Wohnung klicken.<br>Zum Hinzufügen neuer Dokumente muss zuerst der Mieter oder die Wohnung aufgerufen werden.</span>");
        HorizontalLayout help = new HorizontalLayout(helpText);
        help.setWidthFull();
        help.setAlignItems(Alignment.START);

        setSizeFull();
        dokumentGrid.setSizeFull();

        configureGrid();
        updateDokumenteGrid();

        add(header, help, createFilterContent(), dokumentGrid);
    }

    /**
     * Erstellt den Filterinhalt für die Ansicht.
     *
     * @return das konfigurierte Filterinhalt-Layout
     */
    private HorizontalLayout createFilterContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setAlignItems(Alignment.END);

        // Setzt den Wertänderungsmodus auf EAGER, um das Grid sofort zu aktualisieren, während der Benutzer tippt
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(event -> updateDokumenteGrid());
        searchField.setClearButtonVisible(true);

        filterTypeComboBox.setItems("Mieter", "Wohnung");
        filterTypeComboBox.addClassName("dark-combo-box");
        filterTypeComboBox.addValueChangeListener(event -> {
            dummy.setVisible(false);
            boolean isMieter = "Mieter".equals(event.getValue());
            mieterComboBox.setVisible(isMieter);
            wohnungComboBox.setVisible(!isMieter);
            updateDokumenteGrid();
        });

        mieterComboBox.setItems(mieterService.findAllMieter());
        mieterComboBox.addClassName("dark-combo-box");
        mieterComboBox.setVisible(false);
        mieterComboBox.setClearButtonVisible(true);
        mieterComboBox.setWidthFull();
        mieterComboBox.setItemLabelGenerator(Mieter::getFullName);
        mieterComboBox.addValueChangeListener(event -> updateDokumenteGrid());

        wohnungComboBox.setItems(wohnungService.findAllWohnungen());
        wohnungComboBox.addClassName("dark-combo-box");
        wohnungComboBox.setVisible(false);
        wohnungComboBox.setClearButtonVisible(true);
        wohnungComboBox.setWidthFull();
        wohnungComboBox.setItemLabelGenerator(Wohnung::getFormattedAddress);
        wohnungComboBox.addValueChangeListener(event -> updateDokumenteGrid());

        dummy.setVisible(true);
        dummy.setClearButtonVisible(true);
        dummy.setWidthFull();
        dummy.setReadOnly(true);

        layout.expand(mieterComboBox, wohnungComboBox, dummy);
        layout.add(searchField, filterTypeComboBox, dummy, mieterComboBox, wohnungComboBox);
        return layout;
    }

    /**
     * Aktualisiert das Dokumenten-Grid mit den gefilterten und/oder gesuchten Dokumenten.
     */
    private void updateDokumenteGrid() {
        String searchTerm = searchField.getValue().trim();
        List<Dokument> dokumente;

        String filterType = filterTypeComboBox.getValue();
        Mieter selectedMieter = mieterComboBox.getValue();
        Wohnung selectedWohnung = wohnungComboBox.getValue();

        if ("Mieter".equals(filterType) && selectedMieter != null) {
            dokumente = dokumentService.findDokumenteByMieter(selectedMieter);
        } else if ("Wohnung".equals(filterType) && selectedWohnung != null) {
            dokumente = dokumentService.findDokumenteByWohnung(selectedWohnung);
        } else {
            dokumente = dokumentService.findAllDokumente();
        }

        // Filtert nach Suchbegriff, falls nötig
        if (!searchTerm.isEmpty()) {
            dokumente = dokumente.stream()
                    .filter(d -> d.getDokumententyp().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            d.getDateipfad().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            (d.getMieter() != null && d.getMieter().getFullName().toLowerCase().contains(searchTerm.toLowerCase())) ||
                            (d.getWohnung() != null && d.getWohnung().getFormattedAddress().toLowerCase().contains(searchTerm.toLowerCase())))
                    .toList();
        }

        int rowHeight = 53;
        TableUtils.configureGrid(dokumentGrid, dokumente, rowHeight);
    }

    /**
     * Konfiguriert das Grid zur Anzeige der Dokumente.
     * Richtet die Spalten und die Aktionsschaltflächen ein.
     */
    private void configureGrid() {
        dokumentGrid.removeAllColumns();

        dokumentGrid.addColumn(Dokument::getDokumententyp)
                .setHeader(createCustomHeader("Dokumenttyp"))
                .setSortable(true)
                .setAutoWidth(true);

        dokumentGrid.addColumn(new ComponentRenderer<>(dokument -> {
            if (dokument.getMieter() != null) {
                RouterLink link = new RouterLink(dokument.getMieter().getFullName(), MieterDetailsView.class, dokument.getMieter().getMieter_id());
                link.getElement().setAttribute("href", link.getHref() + "?previousView=dokumente");
                return link;
            } else {
                return new Div(new Text("Kein Mieter"));
            }
        })).setHeader(createCustomHeader("Mietername")).setSortable(true).setAutoWidth(true);

        dokumentGrid.addColumn(new ComponentRenderer<>(dokument -> {
            if (dokument.getWohnung() != null) {
                String formattedAddress = addLineBreaks(dokument.getWohnung());
                RouterLink link = new RouterLink("", WohnungDetailsView.class, dokument.getWohnung().getWohnung_id());
                link.getElement().setProperty("innerHTML", formattedAddress);
                link.getElement().setAttribute("href", link.getHref() + "?previousView=dokumente");
                return link;
            }
            return new Div(new Text("Keine Wohnung"));
        })).setHeader(createCustomHeader("Wohnung")).setSortable(true).setAutoWidth(true);

        dokumentGrid.addColumn(new ComponentRenderer<>(dokument -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");
            viewButton.addClickListener(event -> dokumentService.viewDokument(dokument));

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");
            deleteButton.addClickListener(event -> {
                dokumentService.deleteDokument(dokument, dokumentGrid, dokument.getWohnung(), 53, configurationService, this::refreshView);
            });

            Button downloadButton = new Button(new Icon("download"));
            downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            downloadButton.getElement().setAttribute("title", "Download");
            downloadButton.addClickListener(event -> dokumentService.downloadDokument(dokument));

            actionsLayout.add(viewButton, deleteButton, downloadButton);
            return actionsLayout;
        })).setHeader(createCustomHeader("Aktionen")).setFlexGrow(0).setAutoWidth(true);

        dokumentGrid.setItems(dokumentService.findAllDokumente());
        dokumentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    /**
     * Fügt an geeigneten Stellen in der Adresse Umbrüche ein.
     *
     * @param wohnung Die Wohnung, deren Adresse formatiert werden soll.
     * @return Die formatierte Adresse mit Umbrüchen.
     */
    private String addLineBreaks(Wohnung wohnung) {
        StringBuilder sb = new StringBuilder();
        sb.append(wohnung.getLand().name()).append(" ");
        sb.append(wohnung.getPostleitzahl()).append(" ");
        sb.append(wohnung.getStadt()).append(",<br>");
        sb.append(wohnung.getStrasse()).append(" ");
        sb.append(wohnung.getHausnummer());
        if (wohnung.getStockwerk() != null && wohnung.getWohnungsnummer() != null) {
            sb.append(",<br>(").append(wohnung.getStockwerk()).append("ter Stock, ");
            sb.append(wohnung.getWohnungsnummer()).append("te Wohnung)");
        }
        return sb.toString();
    }

    public void refreshView() {
        updateDokumenteGrid();
    }
}