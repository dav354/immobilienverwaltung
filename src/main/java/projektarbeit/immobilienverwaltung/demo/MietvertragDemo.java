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
import projektarbeit.immobilienverwaltung.service.MietvertragService;

import java.time.LocalDate;
import java.util.List;

/**
 * Assigns Mietvertrag (rental contracts) to Mieter (tenants) and Wohnungen (apartments) in demo mode.
 * This class runs as a command line runner when the application starts.
 */
@Component
@Order(10)
public class MietvertragDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MietvertragDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;
    private final MietvertragService mietvertragService;

    /**
     * Constructor for MietvertragDemo.
     *
     * @param demoModeConfig    The demo mode configuration.
     * @param mieterRepository  The repository for managing Mieter entities.
     * @param wohnungRepository The repository for managing Wohnung entities.
     * @param mietvertragService The service for managing Mietvertrag-related operations.
     */
    public MietvertragDemo(DemoModeConfig demoModeConfig,
                           MieterRepository mieterRepository,
                           WohnungRepository wohnungRepository,
                           MietvertragService mietvertragService) {
        this.demoModeConfig = demoModeConfig;
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.mietvertragService = mietvertragService;
    }

    /**
     * Runs the assignment of Mietvertrag to Mieter and Wohnungen if demo mode is enabled.
     *
     * @param args Command line arguments.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            assignMietvertragDemo();
        } else {
            logger.info("Demo mode is OFF. Skip Mietvertrag assignment.");
        }
    }

    /**
     * Assigns Mietvertrag to Mieter and Wohnungen in demo mode.
     * This method checks if there are enough Mieter and Wohnungen available before assigning.
     */
    public void assignMietvertragDemo() {
        // Check if there are any Mieter and Wohnungen in the database
        if (mieterRepository.count() > 0 && wohnungRepository.count() > 0) {
            logger.info("Assigning Mietvertrag data to Mieter and Wohnungen...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Load existing Mieter
            List<Mieter> mieter = mieterRepository.findAll();

            // Check if there are at least 3 Wohnungen and 2 Mieter
            if (wohnungen.size() >= 6 && mieter.size() >= 4) {
                Mieter mieter1 = mieter.get(0);
                Mieter mieter2 = mieter.get(1);
                Mieter mieter3 = mieter.get(2);
                Mieter mieter4 = mieter.get(3);

                Wohnung wohnung1 = wohnungen.get(0);
                Wohnung wohnung2 = wohnungen.get(1);
                Wohnung wohnung3 = wohnungen.get(2);
                Wohnung wohnung4 = wohnungen.get(3);
                Wohnung wohnung5 = wohnungen.get(4);
                Wohnung wohnung6 = wohnungen.get(5);

                // Assign Mietvertrag to Mieter and Wohnungen
                mietvertragService.createAndSaveMietvertrag(mieter1, wohnung1, LocalDate.now().minusMonths(12), LocalDate.now().plusMonths(12), 1000.00, 500.00, 3);
                mietvertragService.createAndSaveMietvertrag(mieter2, wohnung2, LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(6), 500.00, 250.00, 1);
                mietvertragService.createAndSaveMietvertrag(mieter3, wohnung3, LocalDate.now().minusMonths(24), LocalDate.now().plusMonths(24), 1500.00, 750.00, 2);
                mietvertragService.createAndSaveMietvertrag(mieter4, wohnung4, LocalDate.now().minusMonths(18), LocalDate.now().plusMonths(18), 1200.00, 600.00, 2);
                mietvertragService.createAndSaveMietvertrag(mieter1, wohnung5, LocalDate.now().minusMonths(12), null, 800.00, 400.00, 1);
                mietvertragService.createAndSaveMietvertrag(mieter2, wohnung6, LocalDate.now().minusMonths(6), null, 900.00, 450.00, 2);

                logger.info("Mietvertrag data assigned to Mieter and Wohnungen.");
            } else {
                logger.warn("Not enough Wohnungen or Mieter found, skipping assignment.");
            }
        }
    }
}