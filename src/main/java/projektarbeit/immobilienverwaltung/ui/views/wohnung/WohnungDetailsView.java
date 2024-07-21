package projektarbeit.immobilienverwaltung.ui.views.wohnung;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
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
import projektarbeit.immobilienverwaltung.service.ConfigurationService;
import projektarbeit.immobilienverwaltung.service.DokumentService;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.service.ZaehlerstandService;
import projektarbeit.immobilienverwaltung.ui.components.UploadUtils;
import projektarbeit.immobilienverwaltung.ui.views.MainView;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.MapComponent;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.WohnungEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ZaehlerstandDialog;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.views.dokumente.DokumenteListView;
import projektarbeit.immobilienverwaltung.ui.views.mieter.MieterListView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * View zur Anzeige der Details einer Wohnung.
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
    private final ConfigurationService configurationService;
    private final int tableRowHeight = 54;
    private String previousView;

    /**
     * Konstruktor für WohnungDetailsView.
     *
     * @param wohnungService       der Service für Wohnung-Operationen
     * @param dokumentService      der Service für Dokument-Operationen
     * @param zaehlerstandService  der Service für Zählerstand-Operationen
     * @param configurationService der Service für globale Konfigurationen
     */
    @Autowired
    public WohnungDetailsView(WohnungService wohnungService, DokumentService dokumentService, ZaehlerstandService zaehlerstandService, ConfigurationService configurationService) {
        this.wohnungService = wohnungService;
        this.dokumentService = dokumentService;
        this.zaehlerstandService = zaehlerstandService;
        this.configurationService = configurationService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long wohnungId) {
        currentWohnung = wohnungService.findWohnungById(wohnungId);
        previousView = event.getLocation().getQueryParameters().getParameters().getOrDefault("previousView", List.of("")).getFirst();

        if (currentWohnung != null) {
            setupView();
        }
    }

    /**
     * Initialisiert die Ansicht.
     */
    private void setupView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().add(
                createWohnungHeader(),
                createInfoMapLayout(),
                createSeparator(),
                createDokumenteZaehlerstandLayout()
        );
    }

    /**
     * Erstellt die Kopfzeile für die Wohnung.
     *
     * @return das Layout für die Kopfzeile
     */
    private HorizontalLayout createWohnungHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        H1 wohnungHeading = new H1("Wohnung");
        layout.add(wohnungHeading, createWohnungEditButton(), createWohnungLoeschenButton(), createSchliessenButton());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    /**
     * Erstellt den Bearbeiten-Button für die Wohnung.
     *
     * @return der Bearbeiten-Button
     */
    private Button createWohnungEditButton() {
        Button wohnungEditButton = new Button("edit");
        wohnungEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        wohnungEditButton.addClickListener(event -> {
            WohnungEditDialog editDialog = new WohnungEditDialog(wohnungService, currentWohnung, this::refreshView, configurationService);
            editDialog.open();
        });
        return wohnungEditButton;
    }

    /**
     * Erstellt den Löschen-Button für die Wohnung.
     *
     * @return der Löschen-Button
     */
    private Button createWohnungLoeschenButton() {
        Button wohnungLoeschenButton = new Button("Löschen");
        wohnungLoeschenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        wohnungLoeschenButton.addClickListener(event -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    "Möchten Sie diese Wohnung wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                    () -> {
                        wohnungService.delete(currentWohnung);
                        NotificationPopup.showSuccessNotification("Wohnung erfolgreich gelöscht.");
                        UI.getCurrent().navigate(WohnungListView.class);
                    },
                    configurationService
            );
            confirmationDialog.open();
        });
        return wohnungLoeschenButton;
    }

    /**
     * Erstellt den Schließen-Button für die Wohnung.
     *
     * @return der Schließen-Button
     */
    private Button createSchliessenButton() {
        Button schliessenButton = new Button("Schließen");
        schliessenButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        schliessenButton.addClickListener(event -> {
            switch (previousView) {
                case "dokumente" -> UI.getCurrent().navigate(DokumenteListView.class);
                case "dashboard" -> UI.getCurrent().navigate(MainView.class);
                case "mieter-list" -> UI.getCurrent().navigate(MieterListView.class);
                case null, default -> UI.getCurrent().navigate(WohnungListView.class);
            }
        });
        return schliessenButton;
    }

    /**
     * Erstellt das Layout für die Informationen und die Karte.
     *
     * @return das Layout für die Informationen und die Karte
     */
    private HorizontalLayout createInfoMapLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeight("400px");

        VerticalLayout leftLayout = new VerticalLayout(createWohnungInfos());
        leftLayout.setWidth("750px");

        MapComponent mapComponent = new MapComponent(currentWohnung);
        mapComponent.setWidthFull();

        layout.add(leftLayout, mapComponent);
        layout.setFlexGrow(1, mapComponent);
        return layout;
    }

    /**
     * Erstellt das Layout für die Wohnungsinformationen.
     *
     * @return das Layout für die Wohnungsinformationen
     */
    private FormLayout createWohnungInfos() {
        FormLayout layout = new FormLayout();
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        addDetailToFormLayout(layout, "Strasse", currentWohnung.getStrasse());
        addDetailToFormLayout(layout, "Hausnummer", currentWohnung.getHausnummer());
        addDetailToFormLayout(layout, "PLZ", currentWohnung.getPostleitzahl());
        addDetailToFormLayout(layout, "Stadt", currentWohnung.getStadt());
        addDetailToFormLayout(layout, "Land", currentWohnung.getLand().getName());
        addDetailToFormLayout(layout, "Stockwerk", currentWohnung.getStockwerk());
        addDetailToFormLayout(layout, "Wohnungsnummer", currentWohnung.getWohnungsnummer());
        addDetailToFormLayout(layout, "GesamtQuadratmeter", String.valueOf(currentWohnung.getGesamtQuadratmeter()));
        addDetailToFormLayout(layout, "Baujahr", String.valueOf(currentWohnung.getBaujahr()));
        addDetailToFormLayout(layout, "Anzahl Bäder", String.valueOf(currentWohnung.getAnzahlBaeder()));
        addDetailToFormLayout(layout, "Anzahl Schlafzimmer", String.valueOf(currentWohnung.getAnzahlSchlafzimmer()));
        addDetailToFormLayout(layout, "Garten", currentWohnung.isHatGarten() ? "Ja" : "Nein");
        addDetailToFormLayout(layout, "Balkon", currentWohnung.isHatBalkon() ? "Ja" : "Nein");
        addDetailToFormLayout(layout, "Terrasse", currentWohnung.isHatTerrasse() ? "Ja" : "Nein");
        addDetailToFormLayout(layout, "Klimaanlage", currentWohnung.isHatKlimaanlage() ? "Ja" : "Nein");

        return layout;
    }

    /**
     * Erstellt das Layout für Dokumente und Zählerstände.
     *
     * @return das Layout für Dokumente und Zählerstände
     */
    private VerticalLayout createDokumenteZaehlerstandLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        // Erstelle Header und Buttons
        HorizontalLayout headers = new HorizontalLayout();
        headers.setWidthFull();
        headers.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout dokumentHeaderLayout = createDokumentHeaderLayout();
        HorizontalLayout zaehlerstandHeaderLayout = createZaehlerstandHeaderLayout();

        headers.add(dokumentHeaderLayout, zaehlerstandHeaderLayout);

        // Erstelle Grids
        HorizontalLayout grids = new HorizontalLayout();
        grids.setWidthFull();
        grids.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Grid<Dokument> dokumentGrid = createDokumentGrid();
        Grid<Zaehlerstand> zaehlerstandGrid = createZaehlerstandGrid();

        dokumentGrid.setWidth("50%");
        zaehlerstandGrid.setWidth("50%");

        grids.add(dokumentGrid, zaehlerstandGrid);
        layout.add(headers, grids);
        return layout;
    }

    /**
     * Erstellt das Header-Layout für Dokumente.
     *
     * @return das Header-Layout für Dokumente
     */
    private HorizontalLayout createDokumentHeaderLayout() {
        HorizontalLayout dokumentHeaderLayout = new HorizontalLayout();
        H1 dokumenteHeading = new H1("Dokumente");

        HorizontalLayout uploadLayout = UploadUtils.createUploadButton("Add Dokument", dokumentService, currentWohnung, this::refreshView);

        dokumentHeaderLayout.add(dokumenteHeading, uploadLayout);
        dokumentHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dokumentHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        dokumentHeaderLayout.setWidth("50%");
        return dokumentHeaderLayout;
    }

    /**
     * Erstellt das Header-Layout für Zählerstände.
     *
     * @return das Header-Layout für Zählerstände
     */
    private HorizontalLayout createZaehlerstandHeaderLayout() {
        HorizontalLayout zaehlerstandHeaderLayout = new HorizontalLayout();
        H1 zaehlerstaendeHeading = new H1("Zählerstände");
        Button addZaehlerstandButton = new Button("Add Zählerstand");

        addZaehlerstandButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addZaehlerstandButton.addClickListener(event -> {
            Zaehlerstand zaehlerstand = new Zaehlerstand();
            zaehlerstand.setWohnung(currentWohnung);
            ZaehlerstandDialog zaehlerstandDialog = new ZaehlerstandDialog(zaehlerstandService, zaehlerstand, this::refreshView, configurationService);
            zaehlerstandDialog.open();
        });

        zaehlerstandHeaderLayout.add(zaehlerstaendeHeading, addZaehlerstandButton);
        zaehlerstandHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        zaehlerstandHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        zaehlerstandHeaderLayout.setWidth("50%");
        return zaehlerstandHeaderLayout;
    }

    /**
     * Erstellt das Grid für Dokumente.
     *
     * @return das Grid für Dokumente
     */
    private Grid<Dokument> createDokumentGrid() {
        Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);
        List<Dokument> dokuments = dokumentService.findDokumenteByWohnung(currentWohnung);
        dokumentService.configureDokumentGrid(dokumentGrid, dokuments, currentWohnung, tableRowHeight, configurationService, this::refreshView);
        return dokumentGrid;
    }

    /**
     * Erstellt das Grid für Zählerstände.
     *
     * @return das Grid für Zählerstände
     */
    private Grid<Zaehlerstand> createZaehlerstandGrid() {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.GERMANY);
        Grid<Zaehlerstand> zaehlerstandGrid = new Grid<>(Zaehlerstand.class);

        zaehlerstandGrid.setColumns(); // Entferne die automatische Spalteneinstellung
        zaehlerstandGrid.addColumn(Zaehlerstand::getName)
                .setHeader(createCustomHeader("Name"))
                .setSortable(true);
        zaehlerstandGrid.addColumn(zaehlerstand -> zaehlerstand.getAblesedatum().format(germanFormatter))
                .setHeader(createCustomHeader("Ablesedatum"))
                .setSortable(true);
        zaehlerstandGrid.addColumn(Zaehlerstand::getAblesewert)
                .setHeader(createCustomHeader("Ablesewert"))
                .setSortable(true);
        zaehlerstandGrid.setWidth("50%");

        zaehlerstandGrid.addComponentColumn(zaehlerstand -> {
            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Löschen");

            deleteButton.addClickListener(event -> {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        "Möchten Sie diesen Zählerstand wirklich löschen?",
                        () -> {
                            zaehlerstandService.deleteZaehlerstand(zaehlerstand);
                            List<Zaehlerstand> updatedZaehlerstaende = zaehlerstandService.findZaehlerstandByWohnung(currentWohnung);
                            TableUtils.configureGrid(zaehlerstandGrid, updatedZaehlerstaende, tableRowHeight);
                        },
                        configurationService
                );
                confirmationDialog.open();
            });

            return deleteButton;
        }).setHeader(createCustomHeader("Aktionen")).setAutoWidth(true);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandService.findZaehlerstandByWohnung(currentWohnung);
        TableUtils.configureGrid(zaehlerstandGrid, zaehlerstaende, tableRowHeight);

        return zaehlerstandGrid;
    }

    /**
     * Fügt ein Detail zum Formularlayout hinzu.
     *
     * @param formLayout das Formularlayout
     * @param label      das Label für das Detail
     * @param value      der Wert für das Detail
     */
    private void addDetailToFormLayout(FormLayout formLayout, String label, String value) {
        if (value != null) {
            Div container = new Div();
            container.getStyle().set("display", "flex").set("justify-content", "space-between");
            Span labelSpan = new Span(label);
            Span valueSpan = new Span(value);
            labelSpan.getStyle().set("font-weight", "bold");
            container.add(labelSpan, valueSpan);
            formLayout.add(container);
        }
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
     * Erstellt einen horizontalen Separator.
     *
     * @return der horizontale Separator
     */
    private Div createSeparator() {
        Div separator = new Div();
        separator.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        separator.setWidthFull();
        separator.getStyle().set("margin", "20px 0");
        return separator;
    }

    /**
     * Aktualisiert die Ansicht.
     */
    public void refreshView() {
        currentWohnung = wohnungService.findWohnungById(currentWohnung.getWohnung_id());
        if (currentWohnung != null) {
            getContent().removeAll();
            setupView();
        } else {
            NotificationPopup.showWarningNotification("Wohnung nicht gefunden.");
        }
    }
}