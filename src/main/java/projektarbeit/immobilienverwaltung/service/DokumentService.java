package projektarbeit.immobilienverwaltung.service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static projektarbeit.immobilienverwaltung.ui.components.TableUtils.createCustomHeader;

/**
 * Service-Klasse zur Verwaltung von Dokument-Entitäten.
 */
@Service
public class DokumentService {

    private static final Logger logger = LoggerFactory.getLogger(DokumentService.class);
    private final DokumentRepository dokumentRepository;
    @Value("${document.storage.path}")
    private String storagePath;

    private Path rootLocation;

    /**
     * Konstruktor für DokumentService mit dem angegebenen DokumentRepository.
     *
     * @param dokumentRepository das Repository für Dokument-Entitäten
     */
    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    /**
     * Initialisiert den Speicherort für Dokumente.
     */
    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(storagePath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    /**
     * Ruft alle Dokument-Entitäten ab.
     *
     * @return eine Liste aller Dokument-Entitäten
     */
    public List<Dokument> findAllDokumente() {
        return dokumentRepository.findAll();
    }

    /**
     * Löscht alle Dokument-Entitäten, die mit der angegebenen Wohnung verknüpft sind.
     * Wenn ein Dokument keinen zugehörigen Mieter hat, wird es gelöscht. Andernfalls wird die Referenz zur Wohnung auf null gesetzt und das Dokument wird aktualisiert.
     *
     * @param wohnung die Wohnung-Entität, deren zugehörige Dokument-Entitäten gelöscht oder aktualisiert werden sollen
     */
    @Transactional
    public void deleteDokumenteByWohnung(Wohnung wohnung) {
        if (wohnung == null) throw new NullPointerException("Wohnung ist null");
        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);
        for (Dokument dokument : dokumente) {
            dokument.setWohnung(null);
            if (dokument.getMieter() == null) {
                deleteFile(dokument.getDateipfad());
                dokumentRepository.delete(dokument);
            } else {
                dokumentRepository.save(dokument);
            }
        }
    }

    /**
     * Löscht alle Dokument-Entitäten, die mit dem angegebenen Mieter verknüpft sind.
     *
     * @param mieter der Mieter, dessen zugehörige Dokument-Entitäten gelöscht werden sollen
     */
    @Transactional
    public void deleteDokumenteByMieter(Mieter mieter) {
        if (mieter == null) throw new NullPointerException("Mieter ist null");
        List<Dokument> dokumente = dokumentRepository.findByMieter(mieter);
        for (Dokument dokument : dokumente) {
            dokument.setMieter(null);
            if (dokument.getWohnung() == null) {
                deleteFile(dokument.getDateipfad());
                dokumentRepository.delete(dokument);
            } else {
                dokumentRepository.save(dokument);
            }
        }
    }

    /**
     * Findet und gibt eine Liste von Dokument-Entitäten zurück, die mit einer bestimmten Wohnung verknüpft sind.
     *
     * @param wohnung Die Wohnung-Entität, für die die zugehörigen Dokumente gefunden werden sollen.
     * @return Eine Liste von Dokument-Entitäten, die mit der angegebenen Wohnung verknüpft sind.
     */
    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }

    /**
     * Findet und gibt eine Liste von Dokument-Entitäten zurück, die mit einem bestimmten Mieter verknüpft sind.
     *
     * @param mieter Die Mieter-Entität, für die die zugehörigen Dokumente gefunden werden sollen.
     * @return Eine Liste von Dokument-Entitäten, die mit dem angegebenen Mieter verknüpft sind.
     */
    public List<Dokument> findDokumenteByMieter(Mieter mieter) {
        return dokumentRepository.findByMieter(mieter);
    }

    /**
     * Zeigt ein Dokument in einem Dialog an.
     *
     * @param dokument Das anzuzeigende Dokument.
     */
    public void viewDokument(Dokument dokument) {
        String mimeType = dokument.getMimeType();
        StreamResource resource = new StreamResource(dokument.getDokumententyp(),
                () -> {
                    try {
                        return Files.newInputStream(Paths.get(dokument.getDateipfad()));
                    } catch (IOException e) {
                        logger.error("Fehler beim Öffnen der Datei", e);
                        return null;
                    }
                });

        resource.setContentType(mimeType);
        resource.setCacheTime(0);

        if ("application/pdf".equals(mimeType)) {
            Anchor anchor = new Anchor(resource, "PDF öffnen");
            anchor.setTarget("_blank");
            anchor.getElement().setAttribute("style", "display: none;");
            com.vaadin.flow.component.UI.getCurrent().getElement().appendChild(anchor.getElement());
            anchor.getElement().callJsFunction("click");
        } else if (mimeType.startsWith("image/")) {
            // Bild anzeigen
            resource.setCacheTime(0);
            Dialog dialog = new Dialog();
            Image image = new Image(resource, "Dokument");
            image.setWidth("100%");
            image.setHeight("100%");
            dialog.add(image);
            dialog.open();
        } else {
            // Für andere Dateitypen zum Download anbieten
            downloadDokument(dokument);
        }
    }

    /**
     * Bietet ein Dokument zum Download an.
     *
     * @param dokument Das herunterzuladende Dokument.
     */
    public void downloadDokument(Dokument dokument) {
        StreamResource resource = new StreamResource(dokument.getDokumententyp(),
                () -> {
                    try {
                        return Files.newInputStream(Paths.get(dokument.getDateipfad()));
                    } catch (IOException e) {
                        logger.error("Fehler beim Öffnen der Datei", e);
                        return null;
                    }
                });

        Anchor downloadLink = new Anchor(resource, "Download");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.getStyle().set("display", "none"); // Verstecke den Link

        // Fügen Sie den Link zum UI hinzu und lösen Sie den Klick aus
        com.vaadin.flow.component.UI.getCurrent().getElement().appendChild(downloadLink.getElement());
        downloadLink.getElement().executeJs("this.click();");
    }

    /**
     * Löscht ein Dokument und aktualisiert das Dokument-Grid.
     *
     * @param dokument             Das zu löschende Dokument.
     * @param dokumentGrid         Das Dokument-Grid, das aktualisiert werden soll.
     * @param entity               Das Objekt (Wohnung oder Mieter), zu dem das Dokument gehört.
     * @param tableRowHeight       Die Höhe der Tabellenzeile.
     * @param configurationService Der Konfigurationsservice.
     */
    public void deleteDokument(Dokument dokument, Grid<Dokument> dokumentGrid, Object entity, int tableRowHeight, ConfigurationService configurationService, Runnable refreshView) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Löschen",
                "Möchten Sie dieses Dokument wirklich löschen?",
                () -> {
                    deleteFile(dokument.getDateipfad());
                    dokumentRepository.delete(dokument);
                    refreshDokumentGrid(dokumentGrid, entity, tableRowHeight);
                    refreshView.run();  // Aktualisiere die Ansicht nach dem Löschen
                    NotificationPopup.showSuccessNotification("Dokument erfolgreich gelöscht.");
                },
                configurationService
        );
        confirmationDialog.open();
    }

    /**
     * Löscht die Datei an dem angegebenen Pfad.
     *
     * @param filePath Der Pfad der zu löschenden Datei.
     */
    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Fehler beim Löschen der Datei", e);
        }
    }

    /**
     * Speichert eine hochgeladene Datei und erstellt ein entsprechendes Dokument.
     *
     * @param file          Die hochgeladene Datei.
     * @param wohnung       Die Wohnung, zu der das Dokument gehört.
     * @param mieter        Der Mieter, zu dem das Dokument gehört.
     * @param dokumententyp Der Typ des Dokuments.
     */
    public void saveFile(MultipartFile file, Wohnung wohnung, Mieter mieter, String dokumententyp) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + "." + fileExtension;
        Path destinationFile = rootLocation.resolve(
                        Paths.get(newFileName))
                .normalize().toAbsolutePath();

        try {
            Files.copy(file.getInputStream(), destinationFile);

            // Automatisch den MIME-Typ setzen
            String mimeType = Files.probeContentType(destinationFile);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Standard-MIME-Typ, falls nicht ermittelt werden kann
            }

            Dokument dokument = new Dokument(wohnung, mieter, dokumententyp, destinationFile.toString());
            dokument.setMimeType(mimeType); // MIME-Typ setzen
            dokumentRepository.save(dokument);
        } catch (IOException e) {
            logger.error("Fehler beim Speichern der Datei", e);
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    /**
     * Gibt die Dateierweiterung einer Datei zurück.
     *
     * @param filename Der Name der Datei.
     * @return Die Dateierweiterung.
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    /**
     * Behandelt das Hochladen von Dateien.
     *
     * @param fileName    Der Name der hochgeladenen Datei.
     * @param inputStream Der InputStream der hochgeladenen Datei.
     * @param mimeType    Der MIME-Typ der hochgeladenen Datei.
     * @param entity      Das Objekt (Wohnung oder Mieter), zu dem das Dokument gehört.
     * @param refreshView Die Methode zum Aktualisieren der Ansicht.
     */
    public void handleFileUpload(String fileName, InputStream inputStream, String mimeType, Object entity, Runnable refreshView) {
        try {
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return fileName;
                }

                @Override
                public String getOriginalFilename() {
                    return fileName;
                }

                @Override
                public String getContentType() {
                    return mimeType;
                }

                @Override
                public boolean isEmpty() {
                    return inputStream == null;
                }

                @Override
                public long getSize() {
                    try {
                        return inputStream.available();
                    } catch (IOException e) {
                        return 0;
                    }
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return inputStream.readAllBytes();
                }

                @Override
                public InputStream getInputStream() {
                    return inputStream;
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {
                    Files.copy(inputStream, dest.toPath());
                }
            };

            if (entity instanceof Wohnung) {
                saveFile(multipartFile, (Wohnung) entity, null, fileName);
            } else if (entity instanceof Mieter) {
                saveFile(multipartFile, null, (Mieter) entity, fileName);
            }

            refreshView.run();
            NotificationPopup.showSuccessNotification("Dokument erfolgreich hochgeladen.");
        } catch (Exception e) {
            logger.error("Fehler beim Hochladen der Datei", e);
            NotificationPopup.showErrorNotification("Fehler beim Hochladen des Dokuments: " + e.getMessage());
        }
    }

    /**
     * Aktualisiert das Dokument-Grid.
     *
     * @param dokumentGrid   Das Dokument-Grid, das aktualisiert werden soll.
     * @param entity         Das Objekt (Wohnung oder Mieter), zu dem die Dokumente gehören.
     * @param tableRowHeight Die Höhe der Tabellenzeile.
     */
    private void refreshDokumentGrid(Grid<Dokument> dokumentGrid, Object entity, int tableRowHeight) {
        List<Dokument> updatedDokumente = null;

        if (entity instanceof Wohnung) {
            updatedDokumente = findDokumenteByWohnung((Wohnung) entity);
        } else if (entity instanceof Mieter) {
            updatedDokumente = findDokumenteByMieter((Mieter) entity);
        }

        if (updatedDokumente != null) {
            TableUtils.configureGrid(dokumentGrid, updatedDokumente, tableRowHeight);
        }
    }

    /**
     * Konfiguriert das Dokument-Grid zur Anzeige der Dokumente.
     *
     * @param dokumentGrid         Das Dokument-Grid, das konfiguriert werden soll.
     * @param dokuments            Die Liste der Dokumente, die im Grid angezeigt werden sollen.
     * @param entity               Das Objekt (Wohnung oder Mieter), zu dem die Dokumente gehören.
     * @param tableRowHeight       Die Höhe der Tabellenzeile.
     * @param configurationService Der Konfigurationsservice.
     * @param refreshView          Die Methode zum Aktualisieren der Ansicht.
     */
    public void configureDokumentGrid(Grid<Dokument> dokumentGrid, List<Dokument> dokuments, Object entity, int tableRowHeight, ConfigurationService configurationService, Runnable refreshView) {
        dokumentGrid.setColumns(); // Entferne die automatische Spalteneinstellung
        dokumentGrid.addColumn(Dokument::getDokumententyp).setHeader(createCustomHeader("Dokumententyp"));

        dokumentGrid.addComponentColumn(dokument -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();

            Button viewButton = new Button(new Icon("eye"));
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            viewButton.getElement().setAttribute("title", "View");
            viewButton.addClickListener(event -> viewDokument(dokument));

            Button deleteButton = new Button(new Icon("close"));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.getElement().setAttribute("title", "Delete");
            deleteButton.addClickListener(event -> deleteDokument(dokument, dokumentGrid, entity, tableRowHeight, configurationService, refreshView));

            Button downloadButton = new Button(new Icon("download"));
            downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            downloadButton.getElement().setAttribute("title", "Download");
            downloadButton.addClickListener(event -> downloadDokument(dokument));

            actionsLayout.add(viewButton, deleteButton, downloadButton);
            return actionsLayout;
        }).setHeader(createCustomHeader("Actions")).setFlexGrow(0).setAutoWidth(true);

        TableUtils.configureGrid(dokumentGrid, dokuments, tableRowHeight);
    }


    /**
     * Gibt den Speicherort für Dokumente zurück.
     *
     * @return der Speicherort für Dokumente
     */
    public Path getRootLocation() {
        return rootLocation;
    }

    /**
     * Speichert eine Demodatei und erstellt ein entsprechendes Dokument.
     *
     * @param sourcePath    Der Pfad zur Quelldatei.
     * @param wohnung       Die Wohnung, zu der das Dokument gehört.
     * @param mieter        Der Mieter, zu dem das Dokument gehört.
     * @param dokumententyp Der Typ des Dokuments.
     * @param iteration     Die Iterationsnummer zur Unterscheidung der Dateien.
     * @throws IOException falls ein Fehler beim Kopieren der Datei auftritt.
     */
    public void saveFileAsDemo(Path sourcePath, Wohnung wohnung, Mieter mieter, String dokumententyp, int iteration, String dateiendung) throws IOException {
        String fileExtension = getFileExtension(sourcePath.getFileName().toString());
        String newFileName = UUID.randomUUID() + "_demo_data_" + iteration + "." + fileExtension;
        String anzeigeName = dokumententyp + iteration + "." + dateiendung;
        Path destinationFile = rootLocation.resolve(Paths.get(newFileName)).normalize().toAbsolutePath();

        Files.copy(sourcePath, destinationFile, StandardCopyOption.REPLACE_EXISTING);

        // Automatisch den MIME-Typ setzen
        String mimeType = Files.probeContentType(destinationFile);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Standard-MIME-Typ, falls nicht ermittelt werden kann
        }

        Dokument dokument = new Dokument(wohnung, mieter, anzeigeName, destinationFile.toString());
        dokument.setMimeType(mimeType); // MIME-Typ setzen
        dokumentRepository.save(dokument);
    }
}