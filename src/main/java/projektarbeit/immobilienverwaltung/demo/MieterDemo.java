package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Initializes the Mieter data for demo purposes.
 * This class runs as a command line runner when the application starts if the demo mode is enabled.
 */
@Component
@Order(2)
public class MieterDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MieterDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;

    /**
     * Constructor for MieterDemo.
     *
     * @param demoModeConfig    The demo mode configuration.
     * @param mieterRepository  The repository for managing Mieter entities.
     * @param wohnungRepository The repository for managing Wohnung entities.
     */
    public MieterDemo(DemoModeConfig demoModeConfig,
                      MieterRepository mieterRepository,
                      WohnungRepository wohnungRepository) {
        this.demoModeConfig = demoModeConfig;
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
    }

    /**
     * Runs the initialization of Mieter data if demo mode is enabled.
     *
     * @param args Command line arguments.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadMieterData();
        } else {
            logger.info("Demo mode is OFF. Skipping loading of Mieter data.");
        }
    }

    /**
     * Loads Mieter data for demo purposes.
     * This method checks if there are no existing Mieter entries before loading the data.
     */
    public void loadMieterData() {
        if (mieterRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Mieter data...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Create Mieter entries if there are Wohnungen available
            if (!wohnungen.isEmpty()) {
                Mieter mieter1 = new Mieter("Doe", "John", "0123456789", "john@doe.com", 3000);
                mieterRepository.save(mieter1);

                Mieter mieter2 = new Mieter("Doe", "Jane", "9876543210","jane@doe.com", 4000);
                mieterRepository.save(mieter2);

                Mieter mieter3 = new Mieter("Mustermann", "Max", "1234567890","max.mustermann@web.de", 2500);
                mieterRepository.save(mieter3);

                Mieter mieter4 = new Mieter("Mustermann", "Erika", "0987654321", "erika.musterfrau@gmx.de",2800);
                mieterRepository.save(mieter4);

                Mieter mieter5 = new Mieter("Schmidt", "Peter", "0123456788", "peter.schmitt83@gmail.com", 3200);
                mieterRepository.save(mieter5);

                Mieter mieter6 = new Mieter("Schneider", "Laura", "9876543211","laura.schneider03@icloud.com", 4200);
                mieterRepository.save(mieter6);

                Mieter mieter7 = new Mieter("Matt", "John", "0123456789","jomat@tuta.de", 3000);
                mieterRepository.save(mieter7);

                Mieter mieter8 = new Mieter("Matt", "Jane", "9876543210", "jamat@tuta.de",4000);
                mieterRepository.save(mieter8);

                Mieter mieter9 = new Mieter("Musterfrau", "Max", "1234567890","MaxMusterfrau@gmail.com", 2500);
                mieterRepository.save(mieter9);

                Mieter mieter10 = new Mieter("Musterfrau", "Erika", "0987654321", "erika@musterfrau.de", 2800);
                mieterRepository.save(mieter10);

                Mieter mieter11 = new Mieter("Becker", "Lukas", "0123456788", "becker.lukas@gmail.com", 3200);
                mieterRepository.save(mieter11);

                Mieter mieter12 = new Mieter("Schneider", "Moritz", "9876543211","moritz.schneider@mailbox.org", 4200);
                mieterRepository.save(mieter12);

                logger.info("Mieter data loaded.");
            } else {
                logger.warn("No Wohnungen found, skipping Mieter data initialization.");
            }
        } else {
            logger.info("Mieter data already exists, skipping initialization.");
        }
    }
}