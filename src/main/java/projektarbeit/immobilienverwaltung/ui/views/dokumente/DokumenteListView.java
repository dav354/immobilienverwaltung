package projektarbeit.immobilienverwaltung.ui.views.dokumente;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
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
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungDetailsView;

import java.util.List;

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

        configureGrid();
        updateDokumenteGrid();

        add(header, createFilterContent(), dokumentGrid);
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
        filterTypeComboBox.addValueChangeListener(event -> {
            dummy.setVisible(false);
            boolean isMieter = "Mieter".equals(event.getValue());
            mieterComboBox.setVisible(isMieter);
            wohnungComboBox.setVisible(!isMieter);
            updateDokumenteGrid();
        });

        mieterComboBox.setItems(mieterService.findAllMieter());
        mieterComboBox.setVisible(false);
        mieterComboBox.setClearButtonVisible(true);
        mieterComboBox.setWidthFull();
        mieterComboBox.setItemLabelGenerator(Mieter::getFullName);
        mieterComboBox.addValueChangeListener(event -> updateDokumenteGrid());

        wohnungComboBox.setItems(wohnungService.findAllWohnungen());
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
                .setHeader("Dokumenttyp")
                .setSortable(true)
                .setAutoWidth(true);
        dokumentGrid.addColumn(dokument -> dokument.getMieter() != null ? dokument.getMieter().getFullName() : "")
                .setHeader("Mietername")
                .setSortable(true)
                .setAutoWidth(true);
        dokumentGrid.addComponentColumn(dokument -> {
            if (dokument.getWohnung() != null) {
                RouterLink link = new RouterLink(dokument.getWohnung().getFormattedAddress(), WohnungDetailsView.class, dokument.getWohnung().getWohnung_id());
                link.getElement().setAttribute("href", link.getHref() + "?previousView=dokumente");
                return link;
            }
            return null;
        }).setHeader("Wohnung").setSortable(true).setAutoWidth(true);

        dokumentGrid.addComponentColumn(dokument -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");
            viewButton.addClickListener(event -> {
                // Handle view action
            });

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");
            deleteButton.addClickListener(event -> {
                // Handle delete action
            });

            Button downloadButton = new Button(new Icon("download"));
            downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            downloadButton.getElement().setAttribute("title", "Download");
            downloadButton.addClickListener(event -> {
                // Handle download action
            });

            actionsLayout.add(viewButton, deleteButton, downloadButton);
            return actionsLayout;
        }).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);

        dokumentGrid.setItems(dokumentService.findAllDokumente());
        dokumentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
}