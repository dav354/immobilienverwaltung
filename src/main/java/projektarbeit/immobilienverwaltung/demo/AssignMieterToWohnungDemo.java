package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.util.List;

@Component
@Order(10)
public class AssignMieterToWohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);

    @Autowired
    private MieterRepository mieterRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Override
    public void run(String... args) throws Exception {
        assignMieterToWohnungDemo();
    }

    @Transactional
    public void assignMieterToWohnungDemo() {
        if (mieterRepository.count() > 0 && wohnungRepository.count() > 0) { // Only load if data exists
            logger.info("Assigning Mieter data to Wohnung...");

            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Load wxisting Mieter
            List<Mieter> mieter = mieterRepository.findAll();

            if (wohnungen.size() >= 3 && mieter.size() >= 2) {
                Mieter mieter1 = mieter.get(0);
                Mieter mieter2 = mieter.get(1);

                Wohnung wohnung1 = wohnungen.get(0);
                Wohnung wohnung2 = wohnungen.get(1);
                Wohnung wohnung3 = wohnungen.get(2);

                // Assign Mieter 1 to Wohnung 1 and Wohnung 2
                mieter1.getWohnung().add(wohnung1);
                mieter1.getWohnung().add(wohnung2);
                wohnung1.setMieter(mieter1);
                wohnung2.setMieter(mieter1);

                // Assign Mieter 2 to Wohnung 3
                mieter2.getWohnung().add(wohnung3);
                wohnung3.setMieter(mieter2);

                // Save the updated entities
                mieterRepository.save(mieter1);
                mieterRepository.save(mieter2);
                wohnungRepository.save(wohnung1);
                wohnungRepository.save(wohnung2);
                wohnungRepository.save(wohnung3);

                logger.info("Mieter data assigned to Wohnungen.");
            } else {
                logger.warn("Not enough Wohnungen or Mieter found, skipping assignment.");
            }
        }
    }
}