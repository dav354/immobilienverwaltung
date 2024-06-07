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
import projektarbeit.immobilienverwaltung.service.MieterService;

import java.util.ArrayList;
import java.util.List;

/**
 * Assigns Mieter (tenants) to Wohnungen (apartments) in demo mode.
 * This class runs as a command line runner when the application starts.
 */
@Component
@Order(10)
public class AssignMieterToWohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;
    private final MieterService mieterService;

    /**
     * Constructor for AssignMieterToWohnungDemo.
     *
     * @param demoModeConfig    The demo mode configuration.
     * @param mieterRepository  The repository for managing Mieter entities.
     * @param wohnungRepository The repository for managing Wohnung entities.
     * @param mieterService     The service for managing Mieter-related operations.
     */
    public AssignMieterToWohnungDemo(DemoModeConfig demoModeConfig,
                                     MieterRepository mieterRepository,
                                     WohnungRepository wohnungRepository,
                                     MieterService mieterService) {
        this.demoModeConfig = demoModeConfig;
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.mieterService = mieterService;
    }

    /**
     * Runs the assignment of Mieter to Wohnungen if demo mode is enabled.
     *
     * @param args Command line arguments.
     * @throws Exception if an error occurs during the operation.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()){
            assignMieterToWohnungDemo();
        } else {
            logger.info("Demo mode is OFF. Skip Mieter to Wohnung assignment.");
        }
    }

    /**
     * Assigns Mieter to Wohnungen in demo mode.
     * This method checks if there are enough Mieter and Wohnungen available before assigning.
     */
    public void assignMieterToWohnungDemo() {
        // Check if there are any Mieter and Wohnungen in the database
        if (mieterRepository.count() > 0 && wohnungRepository.count() > 0) {
            logger.info("Assigning Mieter data to Wohnung...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Load existing Mieter
            List<Mieter> mieter = mieterRepository.findAll();

            // Check if there are at least 3 Wohnungen and 2 Mieter
            if (wohnungen.size() >= 3 && mieter.size() >= 2) {
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

                // Assign Wohnungen to Mieter
                List<Wohnung> wohnungsListe1 = new ArrayList<>();
                wohnungsListe1.add(wohnung1);
                wohnungsListe1.add(wohnung2);
                wohnungsListe1.add(wohnung3);

                List<Wohnung> wohnungsListe2 = new ArrayList<>();
                wohnungsListe2.add(wohnung4);

                List<Wohnung> wohnungsListe3 = new ArrayList<>();
                wohnungsListe3.add(wohnung5);

                List<Wohnung> wohnungsListe4 = new ArrayList<>();
                wohnungsListe4.add(wohnung6);

                // Save the updated entities
                mieterService.saveWohnungToMieter(mieter1, wohnungsListe1);
                mieterService.saveWohnungToMieter(mieter2, wohnungsListe2);
                mieterService.saveWohnungToMieter(mieter3, wohnungsListe3);
                mieterService.saveWohnungToMieter(mieter4, wohnungsListe4);

                logger.info("Mieter data assigned to Wohnungen.");
            } else {
                logger.warn("Not enough Wohnungen or Mieter found, skipping assignment.");
            }
        }
    }
}