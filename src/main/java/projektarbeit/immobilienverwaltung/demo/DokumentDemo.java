package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.service.DokumentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * Initialisiert die Dokumentdaten zu Demozwecken.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet, falls der Demo-Modus aktiviert ist.
 */
@Component
@Order(5)
public class DokumentDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DokumentDemo.class);
    private static final String DEMO_PDF_PATH = "DEMO/demo_pdf_document.pdf";
    private static final String DEMO_SVG_PATH = "DEMO/genericbuilding2.svg";
    private final DemoModeConfig demoModeConfig;
    private final DokumentRepository dokumentRepository;
    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final DokumentService dokumentService;

    /**
     * Konstruktor für DokumentDemo.
     *
     * @param demoModeConfig     Die Konfiguration des Demo-Modus.
     * @param dokumentRepository Das Repository zur Verwaltung von Dokument-Entitäten.
     * @param wohnungRepository  Das Repository zur Verwaltung von Wohnungs-Entitäten.
     * @param mieterRepository   Das Repository zur Verwaltung von Mieter-Entitäten.
     * @param dokumentService    Der Dienst zur Verwaltung von Dokumenten.
     */
    public DokumentDemo(DemoModeConfig demoModeConfig,
                        DokumentRepository dokumentRepository,
                        WohnungRepository wohnungRepository,
                        MieterRepository mieterRepository,
                        DokumentService dokumentService) {
        this.demoModeConfig = demoModeConfig;
        this.dokumentRepository = dokumentRepository;
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
        this.dokumentService = dokumentService;
    }

    /**
     * Führt die Initialisierung der Dokumentdaten aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadDokumentData();
        } else {
            logger.info("Demo-Modus ist AUS. Laden der Dokumentdaten wird übersprungen.");
        }
    }

    /**
     * Lädt Dokumentdaten zu Demozwecken.
     * Diese Methode prüft, ob keine bestehenden Dokumenteinträge vorhanden sind, bevor die Daten geladen werden.
     */
    private void loadDokumentData() {
        if (dokumentRepository.count() == 0) { // Nur laden, wenn keine Daten existieren
            logger.info("Laden der Dokumentdaten...");

            // Bestehende Wohnungen und Mieter laden
            List<Wohnung> wohnungen = wohnungRepository.findAll();
            List<Mieter> mieterList = mieterRepository.findAll();

            // Dokumenteinträge für jeden Mieter erstellen
            if (!wohnungen.isEmpty() && !mieterList.isEmpty()) {
                for (Mieter mieter : mieterList) {
                    addRandomDocumentsForEntity(mieter, null);
                }

                for (Wohnung wohnung : wohnungen) {
                    addRandomDocumentsForEntity(null, wohnung);
                }

                logger.info("Dokumentdaten geladen.");
            } else {
                logger.warn("Keine Wohnungen oder Mieter gefunden, Initialisierung der Dokumentdaten wird übersprungen.");
            }
        } else {
            logger.info("Dokumentdaten existieren bereits, Initialisierung wird übersprungen.");
        }
    }

    /**
     * Fügt zufällig 1 bis 3 Dokumente für eine gegebene Wohnung oder Mieter hinzu.
     *
     * @param mieter  Der Mieter, zu dem die Dokumente hinzugefügt werden.
     * @param wohnung Die Wohnung, zu der die Dokumente hinzugefügt werden.
     */
    private void addRandomDocumentsForEntity(Mieter mieter, Wohnung wohnung) {
        String[] demoFiles = {DEMO_PDF_PATH, DEMO_SVG_PATH};
        Random random = new Random();
        int numOfDocuments = random.nextInt(5) + 1;

        for (int i = 0; i < numOfDocuments; i++) {
            String filePath = demoFiles[random.nextInt(demoFiles.length)];
            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();
            String dokumententyp = fileName.substring(0, fileName.lastIndexOf('.'));
            String dateiendung = fileName.substring(fileName.lastIndexOf('.') + 1);

            try {
                if (Files.exists(path)) {
                    dokumentService.saveFileAsDemo(path, wohnung, mieter, dokumententyp, i, dateiendung);
                } else {
                    logger.warn("Datei {} existiert nicht, wird übersprungen.", filePath);
                }
            } catch (IOException e) {
                logger.error("Fehler beim Kopieren der Datei", e);
            }
        }
    }
}