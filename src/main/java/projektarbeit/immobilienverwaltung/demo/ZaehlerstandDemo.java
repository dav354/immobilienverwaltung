package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Initializes the Zaehlerstand (meter reading) data for demo purposes.
 * This class runs as a command line runner when the application starts if the demo mode is enabled.
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@Order(3)
public class ZaehlerstandDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ZaehlerstandDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final WohnungRepository wohnungRepository;

    /**
     * Constructor for ZaehlerstandDemo.
     *
     * @param demoModeConfig       The demo mode configuration.
     * @param zaehlerstandRepository The repository for managing Zaehlerstand entities.
     * @param wohnungRepository    The repository for managing Wohnung entities.
     */
    public ZaehlerstandDemo(DemoModeConfig demoModeConfig,
                            ZaehlerstandRepository zaehlerstandRepository,
                            WohnungRepository wohnungRepository) {
        this.demoModeConfig = demoModeConfig;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.wohnungRepository = wohnungRepository;
    }

    /**
     * Runs the initialization of Zaehlerstand data if demo mode is enabled.
     *
     * @param args Command line arguments.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadZaehlerstandData();
        } else {
            logger.info("Demo mode is OFF. Skipping loading of Zaehlerstand data.");
        }
    }

    /**
     * Loads Zaehlerstand data for demo purposes.
     * This method checks if there are no existing Zaehlerstand entries before loading the data.
     */
    private void loadZaehlerstandData() {
        if (zaehlerstandRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Zaehlerstand data...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Create Zaehlerstand entries if there are Wohnungen available
            if (!wohnungen.isEmpty()) {
                Zaehlerstand zaehlerstand1 = new Zaehlerstand(wohnungen.get(0), LocalDate.of(2023, 1, 1), 100.0, "Strom");
                zaehlerstandRepository.save(zaehlerstand1);

                Zaehlerstand zaehlerstand2 = new Zaehlerstand(wohnungen.get(1), LocalDate.of(2023, 6, 1), 200.0, "Wasser");
                zaehlerstandRepository.save(zaehlerstand2);

                Zaehlerstand zaehlerstand3 = new Zaehlerstand(wohnungen.get(2), LocalDate.of(2023, 11, 1), 300.0, "Gas");
                zaehlerstandRepository.save(zaehlerstand3);

                Zaehlerstand zaehlerstand4 = new Zaehlerstand(wohnungen.get(3), LocalDate.of(2023, 12, 1), 400.0, "Strom");
                zaehlerstandRepository.save(zaehlerstand4);

                logger.info("Zaehlerstand data loaded.");
            } else {
                logger.warn("No Wohnungen found, skipping Zaehlerstand data initialization.");
            }
        } else {
            logger.info("Zaehlerstand data already exists, skipping initialization.");
        }
    }
}