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
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.service.*;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.layout.MainLayout;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.views.dialog.MieterEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.VertragEditDialog;
import projektarbeit.immobilienverwaltung.ui.views.dialog.VertragHinzufuegenDialog;
import projektarbeit.immobilienverwaltung.ui.views.dokumente.DokumenteListView;
import projektarbeit.immobilienverwaltung.ui.views.wohnung.WohnungListView;

import java.util.List;
//TODO Mieter Edit darkmode, Mieter Löschen, Mietvertrag Hinzufügen
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
    private String previousView;
    Mieter currentMieter;
    List<Mietvertrag> mietvertrage;

    public MieterDetailsView(MietvertragService mietvertragService, MieterService mieterService, DokumentService dokumentService, WohnungService wohnungsService, ConfigurationService configurationService) {
        this.mietvertragService = mietvertragService;
        this.mieterService = mieterService;
        this.dokumentService = dokumentService;
        this.wohnungsService = wohnungsService;
        this.configurationService = configurationService;
    }

    @Override
    public void setParameter(BeforeEvent event, Long mieterId) {
        currentMieter = mieterService.findById(mieterId);
        mietvertrage = mietvertragService.findByMieter(mieterId);
        previousView = event.getLocation().getQueryParameters().getParameters().getOrDefault("previousView", List.of("")).getFirst();

        if (currentMieter != null) {
            setupView();
        }
    }

    private void setupView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().add(
                createMieterLayout(),
                createSeparator(),
                createMietVertragInfos()
        );
    }

    private HorizontalLayout createMieterHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        H1 mieterHeading = new H1("Mieter");
        layout.add(mieterHeading, createMieterEditButton(), createMieterLoeschenButton(), createSchliessenButton());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private Button createMieterEditButton() {
        Button mieterEditButton = new Button("Edit");
        mieterEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        mieterEditButton.addClickListener(event -> {
            MieterEditDialog editDialog = new MieterEditDialog(mieterService, currentMieter, this::refreshView, configurationService);
            editDialog.open();
        });
        return mieterEditButton;
    }

    private Button createMieterLoeschenButton() {
        Button mieterLoeschenButton = new Button("Löschen");
        mieterLoeschenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        mieterLoeschenButton.addClickListener(event -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    "Möchten Sie diesen Mieter wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                    () -> {
                        mieterService.deleteMieter(currentMieter);
                        NotificationPopup.showSuccessNotification("Mieter erfolgreich gelöscht.");
                        UI.getCurrent().navigate(WohnungListView.class);
                    },
                    configurationService
            );
            confirmationDialog.open();
        });
        return mieterLoeschenButton;
    }

    private Button createSchliessenButton() {
        Button schliessenButton = new Button("Schließen");
        schliessenButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        schliessenButton.addClickListener(event -> {
            if ("dokumente".equals(previousView)) {
                UI.getCurrent().navigate(DokumenteListView.class);
            } else if ("wohnungen".equals(previousView)){
                UI.getCurrent().navigate(WohnungListView.class);
            } else {
                UI.getCurrent().navigate(MieterListView.class);
            }
        });
        return schliessenButton;
    }

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

    private FormLayout createMieterInfos() {
        FormLayout layout = new FormLayout();
        layout.setWidth("450px");
        layout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(createMieterHeader());
        horizontalLayout.setWidthFull();
        layout.add(horizontalLayout);

        addDetailToFormLayout(layout, "Name", currentMieter.getName());
        addDetailToFormLayout(layout, "Vorname", currentMieter.getVorname());
        addDetailToFormLayout(layout, "Telefonnummer", currentMieter.getTelefonnummer());
        addDetailToFormLayout(layout, "Email", currentMieter.getEmail());
        addDetailToFormLayout(layout, "Einkommen", String.valueOf(currentMieter.getEinkommen()));

        return layout;
    }

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

    private HorizontalLayout createMietervertragHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        H1 mietervertragHeading = new H1("Mietvertrag");
        layout.add(mietervertragHeading, createVertragHinzufuegenButton());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    private Button createVertragHinzufuegenButton() {
        Button vertragEditButton = new Button("Hinzufügen");
        vertragEditButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        vertragEditButton.addClickListener(event -> {
            VertragHinzufuegenDialog editDialog = new VertragHinzufuegenDialog(currentMieter, mietvertragService, this::refreshView, configurationService, wohnungsService);
            editDialog.open();
        });
        return vertragEditButton;
    }

    private Grid <Mietvertrag> createMietvertragsGrid(){

        // Grid für Mietvertragsdaten erstellen
        Grid<Mietvertrag> mietvertragGrid = new Grid<>();
        mietvertragGrid.setItems(mietvertrage);// Methode, um die Mietverträge zu erhalten

        // Spalten im Grid definieren
        mietvertragGrid.addColumn(Mietvertrag::getWohnung)
                .setHeader("Wohnung");
        mietvertragGrid.addColumn(Mietvertrag::getMietbeginn)
                .setHeader("Mietbeginn")
                .setRenderer(new LocalDateRenderer<>(Mietvertrag::getMietbeginn, "dd.MM.yyyy"));
        mietvertragGrid.addColumn(Mietvertrag::getMietende)
                .setHeader("Mietende")
                .setRenderer(new LocalDateRenderer<>(Mietvertrag::getMietende, "dd.MM.yyyy"));
        mietvertragGrid.addColumn(Mietvertrag::getKaution)
                .setHeader("Kaution");
        mietvertragGrid.addColumn(Mietvertrag::getMiete)
                .setHeader("Miete");
        mietvertragGrid.addColumn(Mietvertrag::getAnzahlBewohner)
                .setHeader("Anzahl Bewohner").setTextAlign(ColumnTextAlign.CENTER);

        TableUtils.configureGrid(mietvertragGrid, mietvertrage, tableRowHeight);

        mietvertragGrid.addComponentColumn(mietvertrag -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");

            viewButton.addClickListener(event -> {
                VertragEditDialog editDialog = new VertragEditDialog(mietvertragService, mietvertrag, this::refreshView, configurationService, wohnungsService);
                editDialog.open();
            });


            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");

            deleteButton.addClickListener(event -> {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        "Möchten Sie diesen Mietvertrag wirklich löschen? Es werden auch alle Dokumente mit gelöscht.",
                        () -> {
                            mietvertragService.deleteMietvertrag(mietvertrag);
                            mietvertrage.remove(mietvertrag); // Entferne den Mietvertrag aus der Liste
                            mietvertragGrid.setItems(mietvertrage); // Aktualisiere die Grid-Daten
                            NotificationPopup.showSuccessNotification("Mietvertrag erfolgreich gelöscht.");
                        },
                        configurationService
                );
                confirmationDialog.open();
            });

            actionsLayout.add(viewButton, deleteButton);
            return actionsLayout;
        }).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);

        mietvertragGrid.setWidthFull();

        return mietvertragGrid;
    }

    private VerticalLayout createMietVertragInfos() {
        // Vertikales Layout für Titel und Grid erstellen
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(createMietervertragHeader(), createMietvertragsGrid());
        verticalLayout.setHeight("400px");
        verticalLayout.setWidth("1085px");

        return verticalLayout;
    }

    private Component createSeparator() {
        Div separator = new Div();
        separator.getStyle().set("border-top", "1px solid var(--lumo-contrast-20pct)");
        separator.setWidthFull();
        separator.getStyle().set("margin", "20px 0");
        return separator;
    }

    HorizontalLayout createDokumenteLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        HorizontalLayout dokumentHeaderLayout = new HorizontalLayout();
        H2 dokumenteHeading = new H2("Dokumente");
        Button addDokumentButton = new Button("Add Dokument");
        addDokumentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dokumentHeaderLayout.add(dokumenteHeading, addDokumentButton);
        dokumentHeaderLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dokumentHeaderLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        dokumentHeaderLayout.setWidth("50%");

        VerticalLayout verticalLayout = new VerticalLayout(dokumentHeaderLayout, createDokumentGrid());
        layout.add(verticalLayout);

        return layout;
    }

    private Grid<Dokument> createDokumentGrid() {
        Grid<Dokument> dokumentGrid = new Grid<>(Dokument.class);
        dokumentGrid.setColumns("dokumententyp");
        dokumentGrid.setWidth("500px");

        dokumentGrid.addComponentColumn(dokument -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");

            Button downloadButton = new Button(new Icon("download"));
            downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            downloadButton.getElement().setAttribute("title", "Download");

            actionsLayout.add(viewButton, deleteButton, downloadButton);
            return actionsLayout;
        }).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);

        List<Dokument> dokuments = dokumentService.findDokumenteByMieter(currentMieter);
        TableUtils.configureGrid(dokumentGrid, dokuments, tableRowHeight);

        return dokumentGrid;
    }

    /**
     * Aktualisiert die Ansicht.
     */
    public void refreshView() {
        currentMieter = mieterService.findById(currentMieter.getMieter_id());
        if (currentMieter != null) {
            getContent().removeAll();
            setupView();
        } else {
            NotificationPopup.showWarningNotification("Mieter nicht gefunden.");
        }
    }
}