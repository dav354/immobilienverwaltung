package projektarbeit.immobilienverwaltung.demo;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(2)
public class MieterDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MieterDemo.class);

    @Autowired
    private MieterRepository mieterRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Override
    public void run(String... args) throws Exception {
        loadMieterData();
    }

    private void loadMieterData() {
        if (mieterRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Mieter data...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Create Mieter
            if (!wohnungen.isEmpty()) {
                Mieter mieter1 = new Mieter("Doe", "John", "0123456789", 3000, 1000, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), 1000, 2);
                mieterRepository.save(mieter1);

                Mieter mieter2 = new Mieter( "Doe", "Jane", "9876543210", 4000, 1200, LocalDate.of(2023, 6, 1), LocalDate.of(2024, 6, 1), 1200, 3);
                mieterRepository.save(mieter2);

                Mieter mieter3 = new Mieter( "Mustermann", "Max", "1234567890", 2500, 800, LocalDate.of(2022, 5, 1), LocalDate.of(2023, 5, 1), 900, 1);
                mieterRepository.save(mieter3);

                Mieter mieter4 = new Mieter("Mustermann", "Erika", "0987654321", 2800, 900, LocalDate.of(2023, 3, 1), LocalDate.of(2024, 3, 1), 1000, 2);
                mieterRepository.save(mieter4);

                Mieter mieter5 = new Mieter( "Schmidt", "Peter", "0123456788", 3200, 1100, LocalDate.of(2022, 2, 1), LocalDate.of(2023, 2, 1), 1100, 2);
                mieterRepository.save(mieter5);

                Mieter mieter6 = new Mieter("Schneider", "Laura", "9876543211", 4200, 1300, LocalDate.of(2023, 7, 1), LocalDate.of(2024, 7, 1), 1300, 3);
                mieterRepository.save(mieter6);

                logger.info("Mieter data loaded.");
            } else {
                logger.warn("No Wohnungen found, skipping Mieter data initialization.");
            }
        } else {
            logger.info("Mieter data already exists, skipping initialization.");
        }
    }
}