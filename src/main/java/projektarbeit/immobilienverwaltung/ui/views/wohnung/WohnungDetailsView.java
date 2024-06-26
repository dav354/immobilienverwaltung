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
import com.vaadin.flow.component.html.IFrame;
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
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.WohnungEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ZaehlerstandDialog;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;

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

        VerticalLayout leftLayout = new VerticalLayout(formLayout);
        leftLayout.setWidth("60%");

        IFrame mapFrame = createMapFrame();

        VerticalLayout rightLayout = new VerticalLayout(mapFrame);
        rightLayout.setWidth("40%");

        topLayout.add(leftLayout, rightLayout);
        return topLayout;
    }

    private IFrame createMapFrame() {
        IFrame mapFrame = new IFrame("https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d243647.30699931925!2d-74.0060152!3d40.7127281!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x89c250bdeb33f607%3A0x4b105c8b569d1e14!2sNew+York%2C+NY%2C+USA!5e0!3m2!1sen!2s!4v1421001339451");
        mapFrame.setWidth("100%");
        mapFrame.setHeight("400px");
        mapFrame.getElement().getStyle().set("border", "0");
        return mapFrame;
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
            ZaehlerstandDialog zaehlerstandDialog = new ZaehlerstandDialog(zaehlerstandService, zaehlerstand, this::setupView);
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

        Div verticalSeparator = createVerticalSeparator();

        zaehlerstandTable.add(dokumentGrid, verticalSeparator, zaehlerstandGrid);
        zaehlerstandTable.setFlexGrow(1, dokumentGrid, zaehlerstandGrid);
        return zaehlerstandTable;
    }

    private Grid<Dokument> createDokumentGrid() {
        Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);
        dokumentGrid.getElement().getStyle().set("min-height", "150px"); // Ensure at least 3 rows
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

        TableUtils.configureGrid(dokumentGrid, dokumentService.findDokumenteByWohnung(currentWohnung));
        return dokumentGrid;
    }

    private Grid<Zaehlerstand> createZaehlerstandGrid() {
        Grid<Zaehlerstand> zaehlerstandGrid = new Grid<>(Zaehlerstand.class);
        zaehlerstandGrid.getElement().getStyle().set("min-height", "150px"); // Ensure at least 3 rows
        zaehlerstandGrid.setColumns("name", "ablesedatum", "ablesewert");
        zaehlerstandGrid.setWidthFull();

        TableUtils.configureGrid(zaehlerstandGrid, zaehlerstandService.findZaehlerstandByWohnung(currentWohnung));
        return zaehlerstandGrid;
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

    /**
     * Creates a vertical separator.
     *
     * @return the vertical separator
     */
    private Div createVerticalSeparator() {
        Div separator = new Div();
        separator.getStyle().set("border-left", "1px solid var(--lumo-contrast-20pct)");
        separator.setHeight("100%");
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