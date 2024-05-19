package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.PostleitzahlRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import static projektarbeit.immobilienverwaltung.model.Land.*;

@Component
@Order(1)
public class WohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MieterDemo.class);

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Override
    public void run(String... args) throws Exception {
        loadWohnungData();
    }

    private void loadWohnungData() {
        if (wohnungRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Wohnung data...");

            // Create Postleitzahl
            Postleitzahl plz1 = new Postleitzahl("07111", "Stuttgart", DE);
            postleitzahlRepository.save(plz1);

            Postleitzahl plz2 = new Postleitzahl("338474", "Würzburg", DE);
            postleitzahlRepository.save(plz2);

            Postleitzahl plz3 = new Postleitzahl("12345", "Bern", CH);
            postleitzahlRepository.save(plz3);

            Postleitzahl plz4 = new Postleitzahl("54321", "Hamburg", DE);
            postleitzahlRepository.save(plz4);

            // Create Adresse
            Adresse adr1 = new Adresse(plz1, "Teststrasse", "11");
            adresseRepository.save(adr1);

            Adresse adr2 = new Adresse(plz2, "Alphastreet", "420a");
            adresseRepository.save(adr2);

            Adresse adr3 = new Adresse(plz3, "Beispielweg", "22");
            adresseRepository.save(adr3);

            Adresse adr4 = new Adresse(plz4, "Musterallee", "33b");
            adresseRepository.save(adr4);

            // Create Wohnung
            Wohnung w1 = new Wohnung(adr1, 200, 1900, 2, 2, true, true, true, true);
            wohnungRepository.save(w1);

            Wohnung w2 = new Wohnung(adr2, 50, 2000, 1, 1, false, false, false, false);
            wohnungRepository.save(w2);

            Wohnung w3 = new Wohnung(adr3, 120, 1985, 1, 3, true, false, true, false);
            wohnungRepository.save(w3);

            Wohnung w4 = new Wohnung(adr4, 85, 1995, 2, 2, false, true, false, true);
            wohnungRepository.save(w4);

            logger.info("Wohnung data loaded.");
        } else {
            logger.info("Wohnung data already exists, skipping initialization.");
        }
    }
}