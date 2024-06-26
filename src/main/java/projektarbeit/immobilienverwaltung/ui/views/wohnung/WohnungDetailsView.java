package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.service.DokumentService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.service.ZaehlerstandService;
import projektarbeit.immobilienverwaltung.ui.components.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.MapComponent;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.WohnungEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ZaehlerstandDialog;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;

import java.util.List;

/**
 * View for displaying the details of a Wohnung.
 */
@PageTitle("Wohnung Details")
@PermitAll
@Route(value = "wohnung-details", layout = MainLayout.class)
@Uses(Icon.class)
public class WohnungDetailsView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private final WohnungService wohnungService;
    private final DokumentService dokumentService;
    private final ZaehlerstandService zaehlerstandService;
    private Wohnung currentWohnung;

    /**
     * Constructor for WohnungDetailsView.
     *
     * @param wohnungService      the service for Wohnung operations
     * @param dokumentService     the service for Dokument operations
     * @param zaehlerstandService the service for Zaehlerstand operations
     */
    @Autowired
    public WohnungDetailsView(WohnungService wohnungService, DokumentService dokumentService, ZaehlerstandService zaehlerstandService) {
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;
        this.zaehlerstandService = zaehlerstandService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long wohnungId) {
        currentWohnung = wohnungService.findWohnungById(wohnungId);
        if (currentWohnung != null) {
            setupView();
        }
    }

    private void setupView() {
        HorizontalLayout layoutRow = createLayoutRow();
        FormLayout formLayout = createFormLayout();
        HorizontalLayout topLayout = createTopLayout(formLayout);
        VerticalLayout formLayout2Col = createFormLayout2Col();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        getContent().add(layoutRow, topLayout, createSeparator(), formLayout2Col);
    }

    private HorizontalLayout createLayoutRow() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        H1 wohnungHeading = new H1("Wohnung");
        Button wohnungEditButton = createWohnungEditButton();
        Button wohnungLoeschenButton = createWohnungLoeschenButton();

        layoutRow.add(wohnungHeading, wohnungEditButton, wohnungLoeschenButton);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);
        return layoutRow;
    }

    private Button createWohnungEditButton() {
        Button wohnungEditButton = new Button("edit");
        wohnungEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        wohnungEditButton.addClickListener(event -> {
            WohnungEditDialog editDialog = new WohnungEditDialog(wohnungService, currentWohnung, this::refreshView);
            editDialog.open();
        });
        return wohnungEditButton;
    }

    private Button createWohnungLoeschenButton() {
        Button wohnungLoeschenButton = new Button("Löschen");
        wohnungLoeschenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        wohnungLoeschenButton.addClickListener(event -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    "Möchten Sie diese Wohnung wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                    () -> {
                        wohnungService.delete(currentWohnung);
                        Notification.show("Wohnung erfolgreich gelöscht.");
                        UI.getCurrent().navigate(WohnungListView.class);
                    }
            );
            confirmationDialog.open();
        });
        return wohnungLoeschenButton;
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        // Add details to the form layout
        addDetailToFormLayout(formLayout, "Strasse", currentWohnung.getStrasse());
        addDetailToFormLayout(formLayout, "Hausnummer", currentWohnung.getHausnummer());
        addDetailToFormLayout(formLayout, "PLZ", currentWohnung.getPostleitzahl());
        addDetailToFormLayout(formLayout, "Stadt", currentWohnung.getStadt());
        addDetailToFormLayout(formLayout, "Land", currentWohnung.getLand().name());
        addDetailToFormLayout(formLayout, "Stockwerk", currentWohnung.getStockwerk());
        addDetailToFormLayout(formLayout, "Wohnungsnummer", currentWohnung.getWohnungsnummer());
        addDetailToFormLayout(formLayout, "GesamtQuadratmeter", String.valueOf(currentWohnung.getGesamtQuadratmeter()));
        addDetailToFormLayout(formLayout, "Baujahr", String.valueOf(currentWohnung.getBaujahr()));
        addDetailToFormLayout(formLayout, "Anzahl Bäder", String.valueOf(currentWohnung.getAnzahlBaeder()));
        addDetailToFormLayout(formLayout, "Anzahl Schlafzimmer", String.valueOf(currentWohnung.getAnzahlSchlafzimmer()));
        addDetailToFormLayout(formLayout, "Garten", currentWohnung.isHatGarten() ? "Ja" : "Nein");
        addDetailToFormLayout(formLayout, "Balkon", currentWohnung.isHatBalkon() ? "Ja" : "Nein");
        addDetailToFormLayout(formLayout, "Terrasse", currentWohnung.isHatTerrasse() ? "Ja" : "Nein");
        addDetailToFormLayout(formLayout, "Klimaanlage", currentWohnung.isHatKlimaanlage() ? "Ja" : "Nein");

        return formLayout;
    }

    private HorizontalLayout createTopLayout(FormLayout formLayout) {
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setHeight("400px"); // Höhe der Karte

        VerticalLayout leftLayout = new VerticalLayout(formLayout);
        leftLayout.setWidth("750px"); // Feste Breite für den Textbereich

        MapComponent mapComponent = new MapComponent(currentWohnung);
        mapComponent.setWidthFull(); // Die Karte soll den verbleibenden Platz einnehmen

        topLayout.add(leftLayout, mapComponent);
        topLayout.setFlexGrow(1, mapComponent); // Karte soll wachsen
        return topLayout;
    }

    private VerticalLayout createFormLayout2Col() {
        VerticalLayout formLayout2Col = new VerticalLayout();
        HorizontalLayout documentTable = createDocumentTable();
        HorizontalLayout zaehlerstandTable = createZaehlerstandTable();

        formLayout2Col.add(documentTable, zaehlerstandTable);
        return formLayout2Col;
    }

    private HorizontalLayout createDocumentTable() {
        HorizontalLayout documentTable = new HorizontalLayout();
        documentTable.setWidthFull();
        documentTable.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        HorizontalLayout dokumentHeaderLayout = createDokumentHeaderLayout();
        VerticalLayout dokumentLayout = new VerticalLayout(dokumentHeaderLayout);

        HorizontalLayout zaehlerstandHeaderLayout = createZaehlerstandHeaderLayout();
        VerticalLayout zaehlerstandLayout = new VerticalLayout(zaehlerstandHeaderLayout);

        documentTable.add(dokumentLayout, zaehlerstandLayout);
        return documentTable;
    }

    private HorizontalLayout createDokumentHeaderLayout() {
        HorizontalLayout dokumentHeaderLayout = new HorizontalLayout();
        H1 dokumenteHeading = new H1("Dokumente");
        Button addDokumentButton = new Button("Add Dokument");
        addDokumentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dokumentHeaderLayout.add(dokumenteHeading, addDokumentButton);
        dokumentHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dokumentHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        dokumentHeaderLayout.setWidthFull();
        return dokumentHeaderLayout;
    }

    private HorizontalLayout createZaehlerstandHeaderLayout() {
        HorizontalLayout zaehlerstandHeaderLayout = new HorizontalLayout();
        H1 zaehlerstaendeHeading = new H1("Zählerstände");
        Button addZaehlerstandButton = new Button("Add Zählerstand");

        addZaehlerstandButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addZaehlerstandButton.addClickListener(event -> {
            Zaehlerstand zaehlerstand = new Zaehlerstand();
            zaehlerstand.setWohnung(currentWohnung);
            ZaehlerstandDialog zaehlerstandDialog = new ZaehlerstandDialog(zaehlerstandService, zaehlerstand, this::refreshView);
            zaehlerstandDialog.open();
        });

        zaehlerstandHeaderLayout.add(zaehlerstaendeHeading, addZaehlerstandButton);
        zaehlerstandHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        zaehlerstandHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        zaehlerstandHeaderLayout.setWidthFull();
        return zaehlerstandHeaderLayout;
    }

    private HorizontalLayout createZaehlerstandTable() {
        HorizontalLayout zaehlerstandTable = new HorizontalLayout();
        zaehlerstandTable.setWidthFull();
        zaehlerstandTable.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Grid<Dokument> dokumentGrid = createDokumentGrid();
        Grid<Zaehlerstand> zaehlerstandGrid = createZaehlerstandGrid();

        zaehlerstandTable.add(dokumentGrid, zaehlerstandGrid);
        zaehlerstandTable.setFlexGrow(1, dokumentGrid, zaehlerstandGrid);
        return zaehlerstandTable;
    }

    private Grid<Dokument> createDokumentGrid() {
        Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);
        dokumentGrid.setColumns("dokumententyp");
        dokumentGrid.setWidthFull();

        dokumentGrid.addComponentColumn(dokument -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");
            viewButton.addClickListener(event -> {
                // Define the view action ToDo
            });

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");
            deleteButton.addClickListener(event -> {
                // Define the delete action ToDo
            });

            Button downloadButton = new Button(new Icon("download"));
            downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            downloadButton.getElement().setAttribute("title", "Download");
            downloadButton.addClickListener(event -> {
                // Define the download action ToDo
            });

            actionsLayout.add(viewButton, deleteButton, downloadButton);
            return actionsLayout;
        }).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);

        List<Dokument> dokuments = dokumentService.findDokumenteByWohnung(currentWohnung);
        addInvisibleBarrierIfEmpty(dokumentGrid, dokuments);

        return dokumentGrid;
    }

    /**
     * Erstellt ein Grid für die Anzeige und Verwaltung von Zählerständen.
     *
     * @return das konfigurierte Zählerstands-Grid
     */
    private Grid<Zaehlerstand> createZaehlerstandGrid() {
        Grid<Zaehlerstand> zaehlerstandGrid = new Grid<>(Zaehlerstand.class);
        zaehlerstandGrid.setColumns("name", "ablesedatum", "ablesewert");
        zaehlerstandGrid.setWidthFull();

        // Füge eine Spalte mit Lösch-Schaltflächen hinzu
        zaehlerstandGrid.addComponentColumn(zaehlerstand -> {
            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Löschen");

            // Füge einen Klick-Listener hinzu, um den Bestätigungsdialog anzuzeigen
            deleteButton.addClickListener(event -> {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        "Möchten Sie diesen Zählerstand wirklich löschen?",
                        () -> {
                            zaehlerstandService.deleteZaehlerstand(zaehlerstand);
                            // Aktualisiere die Höhe des Grids
                            List<Zaehlerstand> updatedZaehlerstaende = zaehlerstandService.findZaehlerstandByWohnung(currentWohnung);
                            TableUtils.configureGrid(zaehlerstandGrid, updatedZaehlerstaende);
                        }
                );
                confirmationDialog.open();
            });

            return deleteButton;
        }).setHeader("Aktionen").setAutoWidth(true);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandService.findZaehlerstandByWohnung(currentWohnung);
        addInvisibleBarrierIfEmpty(zaehlerstandGrid, zaehlerstaende);

        return zaehlerstandGrid;
    }

    /**
     * Konfiguriert das gegebene Grid mit den angegebenen Elementen und fügt eine unsichtbare Barriere hinzu,
     * wenn keine Elemente vorhanden sind. Dies stellt sicher, dass das Layout stabil bleibt.
     *
     * @param <T>   Der Typ der Elemente im Grid.
     * @param grid  Das zu konfigurierende Grid.
     * @param items Die Elemente, die im Grid angezeigt werden sollen.
     */
    private <T> void addInvisibleBarrierIfEmpty(Grid<T> grid, List<T> items) {
        // Konfiguriere das Grid mit den gegebenen Elementen
        TableUtils.configureGrid(grid, items);

        // Wenn keine Elemente vorhanden sind, füge eine unsichtbare Barriere hinzu
        if (items.isEmpty()) {
            Div invisibleBarrier = new Div();
            invisibleBarrier.setHeight("150px");
            invisibleBarrier.getStyle().set("visibility", "hidden");
            grid.getElement().appendChild(invisibleBarrier.getElement());
        }
    }

    /**
     * Adds a detail to the form layout.
     *
     * @param formLayout the form layout
     * @param label      the label for the detail
     * @param value      the value for the detail
     */
    private void addDetailToFormLayout(FormLayout formLayout, String label, String value) {
        if (value != null) {
            Div container = new Div();
            container.getStyle().set("display", "flex").set("justify-content", "space-between");
            Span labelSpan = new Span(label);
            Span valueSpan = new Span(value);
            labelSpan.getStyle().set("font-weight", "bold"); // Setze den Label-Text fett
            container.add(labelSpan, valueSpan);
            formLayout.add(container);
        }
    }

    /**
     * Creates a horizontal separator.
     *
     * @return the horizontal separator
     */
    private Div createSeparator() {
        Div separator = new Div();
        separator.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        separator.setWidthFull();
        separator.getStyle().set("margin", "20px 0");
        return separator;
    }

    public void refreshView() {
        // Re-fetch the current Wohnung from the service
        currentWohnung = wohnungService.findWohnungById(currentWohnung.getWohnung_id());
        if (currentWohnung != null) {
            // Clear existing content
            getContent().removeAll();
            // Re-setup the view with the updated Wohnung details
            setupView();
        } else {
            NotificationPopup.showWarningNotification("Wohnung nicht gefunden.");
        }
    }

}