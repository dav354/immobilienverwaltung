package projektarbeit.immobilienverwaltung.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import projektarbeit.immobilienverwaltung.model.Dokument;
import com.vaadin.flow.component.html.HtmlObject;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.ui.components.NotificationPopup;
import projektarbeit.immobilienverwaltung.ui.components.TableUtils;
import projektarbeit.immobilienverwaltung.ui.views.dialog.ConfirmationDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    @Value("${document.storage.path}")
    private String storagePath;

    private Path rootLocation;

    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(storagePath);
        try {
            Files.createDirectories(rootLocation); // Erstellen Sie das Verzeichnis, falls es nicht existiert
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public List<Dokument> findAllDokumente() {
        return dokumentRepository.findAll();
    }

    @Transactional
    public void deleteDokumenteByWohnung(Wohnung wohnung) {
        if (wohnung == null) throw new NullPointerException("Wohnung is null");
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

    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }

    public List<Dokument> findDokumenteByMieter(Mieter mieter) {
        return dokumentRepository.findByMieter(mieter);
    }

    public void viewDokument(Dokument dokument) {
        Dialog dialog = new Dialog();
        dialog.setWidth("80%");
        dialog.setHeight("80%");

        String mimeType = dokument.getMimeType();
        StreamResource resource = new StreamResource(dokument.getDokumententyp(),
                () -> {
                    try {
                        return Files.newInputStream(Paths.get(dokument.getDateipfad()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });

        if ("application/pdf".equals(mimeType)) {
            // PDF anzeigen
            resource.setContentType("application/pdf");
            resource.setCacheTime(0);

            Anchor anchor = new Anchor(resource, "");
            anchor.setHref(resource);
            String resourceUrl = anchor.getHref();

            IFrame pdfFrame = new IFrame(resourceUrl);
            pdfFrame.setSizeFull();
            dialog.add(pdfFrame);
        } else if (mimeType.startsWith("image/")) {
            // Bild anzeigen
            resource.setCacheTime(0);

            Image image = new Image(resource, "Dokument");
            image.setWidth("100%");
            image.setHeight("100%");
            dialog.add(image);
        } else {
            // Für andere Dateitypen zum Download anbieten
            downloadDokument(dokument);
            return; // Keine Notwendigkeit, den Dialog zu öffnen
        }

        dialog.open();
    }

    public void downloadDokument(Dokument dokument) {
        StreamResource resource = new StreamResource(dokument.getDokumententyp(),
                () -> {
                    try {
                        return Files.newInputStream(Paths.get(dokument.getDateipfad()));
                    } catch (IOException e) {
                        e.printStackTrace();
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

    public void deleteDokument(Dokument dokument, Grid<Dokument> dokumentGrid, Wohnung currentWohnung, int tableRowHeight, ConfigurationService configurationService) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                "Möchten Sie dieses Dokument wirklich löschen?",
                () -> {
                    deleteFile(dokument.getDateipfad());
                    dokumentRepository.delete(dokument);
                    List<Dokument> updatedDokuments = findDokumenteByWohnung(currentWohnung);
                    TableUtils.configureGrid(dokumentGrid, updatedDokuments, tableRowHeight);
                    NotificationPopup.showSuccessNotification("Dokument erfolgreich gelöscht.");
                },
                configurationService
        );
        confirmationDialog.open();
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dokument saveFile(MultipartFile file, Wohnung wohnung, Mieter mieter, String dokumententyp) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
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
            return dokumentRepository.save(dokument);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}