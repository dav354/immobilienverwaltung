package projektarbeit.immobilienverwaltung.ui.views.mieter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.service.*;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.components.UploadUtils;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.MieterEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.VertragHinzufuegenDialog;
import projektarbeit.immobilienverwaltung.ui.views.dokumente.DokumenteListView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungDetailsView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungListView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static projektarbeit.immobilienverwaltung.ui.components.FormUtils.addDetailToFormLayout;
import static projektarbeit.immobilienverwaltung.ui.components.TableUtils.createCustomHeader;

/**
 * Diese Klasse stellt die Detailansicht eines Mieters dar und ermöglicht das Anzeigen von Mieterinformationen,
 * das Hinzufügen und Löschen von Mietverträgen sowie das Verwalten von Dokumenten.
 * Außerdem können Mieter bearbeitet und gelöscht werden.
 */
@PermitAll
@Route(value = "mieter-details", layout = MainLayout.class)
@PageTitle("Mieter Detail")
@Uses(Icon.class)
public class MieterDetailsView extends Composite<VerticalLayout> implements HasUrlParameter<Long> {

    private final MietvertragService mietvertragService;
    private final MieterService mieterService;
    private final DokumentService dokumentService;
    private final WohnungService wohnungsService;
    private final ConfigurationService configurationService;
    private final int tableRowHeight = 54;
    Mieter currentMieter;
    List<Mietvertrag> mietvertrage;
    private String previousView;
    private Grid<Mietvertrag> mietvertragGrid;

    /**
     * Konstruktor für die MieterDetailsView.
     *
     * @param mietvertragService   Der Service für Mietverträge.
     * @param mieterService        Der Service für Mieter.
     * @param dokumentService      Der Service für Dokumente.
     * @param wohnungsService      Der Service für Wohnungen.
     * @param configurationService Der Service für die Konfiguration.
     */
    public MieterDetailsView(MietvertragService mietvertragService, MieterService mieterService, DokumentService dokumentService, WohnungService wohnungsService, ConfigurationService configurationService) {
        this.mietvertragService = mietvertragService;
        this.mieterService = mieterService;
        this.dokumentService = dokumentService;
        this.wohnungsService = wohnungsService;
        this.configurationService = configurationService;
    }

    /**
     * Setzt die Parameter für die View basierend auf der Mieter-ID.
     *
     * @param event    Das Ereignis, das den Parameter enthält.
     * @param mieterId Die ID des Mieters.
     */
    @Override
    public void setParameter(BeforeEvent event, Long mieterId) {
        currentMieter = mieterService.findById(mieterId);
        mietvertrage = mietvertragService.findByMieter(mieterId);
        previousView = event.getLocation().getQueryParameters().getParameters().getOrDefault("previousView", List.of("")).getFirst();

        if (currentMieter != null) {
            setupView();
        }
    }

    /**
     * Initialisiert die View-Komponenten und fügt sie dem Layout hinzu.
     */
    private void setupView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().add(
                createMieterLayout(),
                createSeparator(),
                createMietVertragInfos()
        );
    }

    /**
     * Erstellt den Header für die Mieterinformationen.
     *
     * @return Der Header als HorizontalLayout.
     */
    private HorizontalLayout createMieterHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        H1 mieterHeading = new H1("Mieter");
        layout.add(mieterHeading, createMieterEditButton(), createMieterLoeschenButton(), createSchliessenButton());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    /**
     * Erstellt den Button zum Bearbeiten des Mieters.
     *
     * @return Der Edit-Button für den Mieter.
     */
    private Button createMieterEditButton() {
        Button mieterEditButton = new Button("Edit");
        mieterEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        mieterEditButton.addClickListener(event -> {
            MieterEditDialog editDialog = new MieterEditDialog(mieterService, currentMieter, this::refreshView, configurationService);
            editDialog.open();
        });
        return mieterEditButton;
    }

    /**
     * Erstellt den Button zum Löschen des Mieters.
     *
     * @return Der Löschen-Button für den Mieter.
     */
    private Button createMieterLoeschenButton() {
        Button mieterLoeschenButton = new Button("Löschen");
        mieterLoeschenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        mieterLoeschenButton.addClickListener(event -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    "Löschen",
                    "Möchten Sie diesen Mieter wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                    () -> {
                        mieterService.deleteMieter(currentMieter);
                        NotificationPopup.showSuccessNotification("Mieter erfolgreich gelöscht.");
                        UI.getCurrent().navigate(MieterListView.class);
                    },
                    configurationService
            );
            confirmationDialog.open();
        });
        return mieterLoeschenButton;
    }

    /**
     * Erstellt den Button zum Schließen der View.
     *
     * @return Der Schließen-Button.
     */
    private Button createSchliessenButton() {
        Button schliessenButton = new Button("Schließen");
        schliessenButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        schliessenButton.addClickListener(event -> {
            switch (previousView) {
                case "dokumente" -> UI.getCurrent().navigate(DokumenteListView.class);
                case "wohnungen" -> UI.getCurrent().navigate(WohnungListView.class);
                case "wohnungen-details" -> {
                    String wohnungIdParam = getUI().orElseThrow().getInternals().getActiveViewLocation().getQueryParameters().getParameters().get("wohnungId").getFirst();
                    if (wohnungIdParam != null) {
                        Long wohnungId = Long.valueOf(wohnungIdParam);
                        UI.getCurrent().navigate(WohnungDetailsView.class, wohnungId);
                    } else {
                        UI.getCurrent().navigate(WohnungListView.class);
                    }
                }
                case null, default -> UI.getCurrent().navigate(MieterListView.class);
            }
        });
        return schliessenButton;
    }

    /**
     * Erstellt das Layout für die Mieterinformationen.
     *
     * @return Das HorizontalLayout für die Mieterinformationen.
     */
    private HorizontalLayout createMieterLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setHeight("400px");

        VerticalLayout leftLayout = new VerticalLayout(createMieterInfos());
        leftLayout.setWidth("450px");

        VerticalLayout rightLayout = new VerticalLayout(createDokumenteLayout());
        leftLayout.setWidth("800px");

        layout.add(leftLayout, rightLayout);
        return layout;
    }

    /**
     * Erstellt das FormLayout für die Anzeige der Mieterdetails.
     *
     * @return Das FormLayout mit den Mieterdetails.
     */
    private FormLayout createMieterInfos() {
        FormLayout layout = new FormLayout();
        layout.setWidth("450px");
        layout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        Div spacer = new Div();
        spacer.getStyle().set("height", "20px"); // Feste Höhe für den Abstand
        layout.add(spacer, 2);

        layout.add(createMieterHeader());

        addDetailToFormLayout(layout, "Name", currentMieter.getName());
        addDetailToFormLayout(layout, "Vorname", currentMieter.getVorname());
        addDetailToFormLayout(layout, "Telefonnummer", currentMieter.getTelefonnummer());
        addDetailToFormLayout(layout, "Email", currentMieter.getEmail());
        addDetailToFormLayout(layout, "Einkommen", String.valueOf(currentMieter.getEinkommen()));

        return layout;
    }

    /**
     * Erstellt den Header für die Mietverträge.
     *
     * @return Der Header als HorizontalLayout.
     */
    private HorizontalLayout createMietervertragHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        H1 mietervertragHeading = new H1("Mietvertrag");
        layout.add(mietervertragHeading, createVertragHinzufuegenButton());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    /**
     * Erstellt den Button zum Hinzufügen eines Mietvertrags.
     *
     * @return Der Button zum Hinzufügen eines Mietvertrags.
     */
    private Button createVertragHinzufuegenButton() {
        Button vertragEditButton = new Button("Hinzufügen");
        vertragEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        vertragEditButton.addClickListener(event -> {
            VertragHinzufuegenDialog editDialog = new VertragHinzufuegenDialog(currentMieter, mietvertragService, this::refreshView, configurationService, wohnungsService);
            editDialog.open();
            mietvertragGrid.setItems(mietvertragService.findByMieter(currentMieter.getMieter_id()));
        });
        return vertragEditButton;
    }

    /**
     * Erstellt das Grid für die Mietverträge.
     *
     * @return Das Grid mit den Mietverträgen.
     */
    private Grid<Mietvertrag> createMietvertragsGrid() {

        // Grid für Mietvertragsdaten erstellen
        mietvertragGrid = new Grid<>();
        mietvertragGrid.setItems(mietvertrage);

        // Spalten im Grid definieren
        mietvertragGrid.addComponentColumn(mietvertrag -> {
            RouterLink link = new RouterLink(mietvertrag.getWohnung().getSmallFormattedAddress(), WohnungDetailsView.class, mietvertrag.getWohnung().getWohnung_id());
            link.getElement().setAttribute("href", link.getHref() + "?previousView=mieter-details" + currentMieter.getMieter_id());
            return link;
        }).setHeader(createCustomHeader("Wohnung"));
        mietvertragGrid.addColumn(Mietvertrag::getMietbeginn)
                .setHeader(createCustomHeader("Mietbeginn"))
                .setRenderer(new LocalDateRenderer<>(Mietvertrag::getMietbeginn, "dd.MM.yyyy"));
        mietvertragGrid.addColumn(mietvertrag -> {
            LocalDate mietende = mietvertrag.getMietende();
            return mietende == null ? "Unbefristet" : mietende.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));})
                .setHeader(createCustomHeader("Mietende"));
        mietvertragGrid.addColumn(Mietvertrag::getKaution)
                .setHeader(createCustomHeader("Kaution"));
        mietvertragGrid.addColumn(Mietvertrag::getMiete)
                .setHeader(createCustomHeader("Miete"));
        mietvertragGrid.addColumn(Mietvertrag::getAnzahlBewohner)
                .setHeader(createCustomHeader("Anzahl Bewohner")).setTextAlign(ColumnTextAlign.CENTER);

        TableUtils.configureGrid(mietvertragGrid, mietvertrage, tableRowHeight);

        mietvertragGrid.addComponentColumn(mietvertrag -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");

            deleteButton.addClickListener(event -> {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        "Löschen",
                        "Möchten Sie diesen Mietvertrag wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                        () -> {
                            mietvertragService.deleteMietvertrag(mietvertrag);
                            NotificationPopup.showSuccessNotification("Mietvertrag erfolgreich gelöscht.");
                            mietvertragGrid.setItems(mietvertragService.findByMieter(currentMieter.getMieter_id()));
                            UI.getCurrent().access(this::refreshView);
                        },
                        configurationService
                );
                confirmationDialog.open();
            });

            actionsLayout.add(deleteButton);
            return actionsLayout;
        }).setHeader(createCustomHeader("Actions")).setFlexGrow(0).setAutoWidth(true);

        mietvertragGrid.setWidthFull();

        return mietvertragGrid;
    }

    /**
     * Erstellt das Layout für die Mietvertragsinformationen.
     *
     * @return Das VerticalLayout für die Mietvertragsinformationen.
     */
    private VerticalLayout createMietVertragInfos() {
        // Vertikales Layout für Titel und Grid erstellen
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(createMietervertragHeader(), createMietvertragsGrid());
        verticalLayout.setSizeFull(); // Layout nimmt die volle verfügbare Größe ein
        verticalLayout.setFlexGrow(1, createMietvertragsGrid());

        return verticalLayout;
    }

    /**
     * Erstellt einen Separator als Div-Element.
     *
     * @return Das Div-Element als Separator.
     */
    private Component createSeparator() {
        Div separator = new Div();
        separator.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        separator.setWidthFull();
        separator.getStyle().set("margin", "20px 0");
        return separator;
    }

    /**
     * Erstellt das Layout für die Dokumente.
     *
     * @return Das HorizontalLayout für die Dokumente.
     */
    HorizontalLayout createDokumenteLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull(); // Layout nimmt die volle verfügbare Größe ein

        HorizontalLayout dokumentHeaderLayout = new HorizontalLayout();
        H2 dokumenteHeading = new H2("Dokumente");

        HorizontalLayout uploadLayout = UploadUtils.createUploadButton("Add Dokument", dokumentService, currentMieter, this::refreshView);

        dokumentHeaderLayout.add(dokumenteHeading, uploadLayout);
        dokumentHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dokumentHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        dokumentHeaderLayout.setWidth("100%"); // Header layout soll die gesamte Breite einnehmen

        VerticalLayout verticalLayout = new VerticalLayout(dokumentHeaderLayout, createDokumentGrid());
        verticalLayout.setSizeFull(); // VerticalLayout nimmt die volle verfügbare Größe ein
        verticalLayout.setFlexGrow(1, createDokumentGrid()); // Grid expandiert, um den verbleibenden Platz einzunehmen

        layout.add(verticalLayout);
        layout.setFlexGrow(1, verticalLayout); // VerticalLayout soll sich ausdehnen, um den Platz einzunehmen

        return layout;
    }

    /**
     * Erstellt das Grid für die Dokumente.
     *
     * @return Das Grid mit den Dokumenten.
     */
    private Grid<Dokument> createDokumentGrid() {
        Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);
        List<Dokument> dokuments = dokumentService.findDokumenteByMieter(currentMieter);
        dokumentService.configureDokumentGrid(dokumentGrid, dokuments, currentMieter, tableRowHeight, configurationService, this::refreshView);
        dokumentGrid.setSizeFull(); // Grid nimmt die volle verfügbare Größe ein
        return dokumentGrid;
    }

    /**
     * Aktualisiert die Ansicht.
     */
    public void refreshView() {
        currentMieter = mieterService.findById(currentMieter.getMieter_id());
        if (currentMieter != null) {
            mietvertrage = mietvertragService.findByMieter(currentMieter.getMieter_id());
            mietvertragGrid.setItems(mietvertrage);
            getContent().removeAll();
            setupView();
        } else {
            NotificationPopup.showWarningNotification("Mieter nicht gefunden.");
        }
    }
}