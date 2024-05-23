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
import projektarbeit.immobilienverwaltung.service.MieterService;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(10)
public class AssignMieterToWohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);

    @Autowired
    private MieterRepository mieterRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private MieterService mieterService;

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
                Mieter mieter3 = mieter.get(2);
                Mieter mieter4 = mieter.get(3);

                Wohnung wohnung1 = wohnungen.get(0);
                Wohnung wohnung2 = wohnungen.get(1);
                Wohnung wohnung3 = wohnungen.get(2);
                Wohnung wohnung4 = wohnungen.get(3);
                Wohnung wohnung5 = wohnungen.get(4);
                Wohnung wohnung6 = wohnungen.get(5);

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