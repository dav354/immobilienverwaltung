package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.util.List;

/**
 * Initializes the Dokument data for demo purposes.
 * This class runs as a command line runner when the application starts if the demo mode is enabled.
 */
@Component
@Order(5)
public class DokumentDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DokumentDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final DokumentRepository dokumentRepository;
    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;

    /**
     * Constructor for DokumentDemo.
     *
     * @param demoModeConfig    The demo mode configuration.
     * @param dokumentRepository The repository for managing Dokument entities.
     * @param wohnungRepository  The repository for managing Wohnung entities.
     * @param mieterRepository   The repository for managing Mieter entities.
     */
    public DokumentDemo(DemoModeConfig demoModeConfig,
                        DokumentRepository dokumentRepository,
                        WohnungRepository wohnungRepository,
                        MieterRepository mieterRepository) {
        this.demoModeConfig = demoModeConfig;
        this.dokumentRepository = dokumentRepository;
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
    }

    /**
     * Runs the initialization of Dokument data if demo mode is enabled.
     *
     * @param args Command line arguments.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadDokumentData();
        } else {
            logger.info("Demo mode is OFF. Skipping loading of Dokument data.");
        }
    }

    /**
     * Loads Dokument data for demo purposes.
     * This method checks if there are no existing Dokument entries before loading the data.
     */
    private void loadDokumentData() {
        if (dokumentRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Dokument data...");

            // Load existing Wohnungen and Mieter
            List<Wohnung> wohnungen = wohnungRepository.findAll();
            List<Mieter> mieterList = mieterRepository.findAll();

            // Create Dokument entries for each Mieter
            if (!wohnungen.isEmpty() && !mieterList.isEmpty()) {
                for (int i = 0; i < mieterList.size(); i++) {
                    Mieter mieter = mieterList.get(i);
                    Wohnung wohnung = i < wohnungen.size() ? wohnungen.get(i) : null;

                    Dokument mietvertrag = new Dokument(wohnung, mieter, "Mietvertrag", "/path/to/mietvertrag" + (i + 1) + ".pdf");
                    dokumentRepository.save(mietvertrag);

                    Dokument rechnung = new Dokument(wohnung, mieter, "Rechnung", "/path/to/rechnung" + (i + 1) + ".pdf");
                    dokumentRepository.save(rechnung);

                    Dokument nebenkosten = new Dokument(wohnung, mieter, "Nebenkostenabrechnung Wasser", "/path/to/nebenkosten" + (i + 1) + ".pdf");
                    dokumentRepository.save(nebenkosten);

                    Dokument nebenkosten2 = new Dokument(wohnung, mieter, "Nebenkostenabrechnung Strom", "/path/to/nebenkosten" + (i + 1) + ".pdf");
                    dokumentRepository.save(nebenkosten2);

                    Dokument perso = new Dokument(null, mieter, "Personalausweis", "/path/to/eigentumsnachweis" + (i + 1) + ".pdf");
                    dokumentRepository.save(perso);
                }

                for (int i = 0; i < wohnungen.size(); i++) {
                    Mieter mieter = mieterList.get(i);
                    Wohnung wohnung = wohnungen.get(i);

                    Dokument grundriss = new Dokument(wohnung, null, "Grundriss", "/path/to/eigentumsnachweis" + (i + 1) + ".pdf");
                    dokumentRepository.save(grundriss);

                    Dokument foto1 = new Dokument(wohnung, null, "Bild-Küche", "/path/to/picture028932" + (i + 1) + ".png");
                    dokumentRepository.save(foto1);

                    Dokument foto2 = new Dokument(wohnung, null, "Bild-Wohnzimmer", "/path/to/picture98423" + (i + 1) + ".png");
                    dokumentRepository.save(foto2);
                }

                logger.info("Dokument data loaded.");
            } else {
                logger.warn("No Wohnungen or Mieter found, skipping Dokument data initialization.");
            }
        } else {
            logger.info("Dokument data already exists, skipping initialization.");
        }
    }
}