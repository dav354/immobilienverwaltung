package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
@Order(5)
public class DokumentDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DokumentDemo.class);

    @Autowired
    private DokumentRepository dokumentRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private MieterRepository mieterRepository;

    @Override
    public void run(String... args) throws Exception {
        loadDokumentData();
    }

    private void loadDokumentData() {
        if (dokumentRepository.count() == 0) { // Only load if no data exists
            logger.info("Loading Dokument data...");

            // Load existing Wohnungen and Mieter
            List<Wohnung> wohnungen = wohnungRepository.findAll();
            List<Mieter> mieter = mieterRepository.findAll();

            // Create Dokument
            if (!wohnungen.isEmpty() && !mieter.isEmpty()) {
                Dokument dokument1 = new Dokument(wohnungen.get(0), mieter.get(0), "Mietvertrag", "/path/to/mietvertrag1.pdf");
                dokumentRepository.save(dokument1);

                Dokument dokument2 = new Dokument(wohnungen.get(1), mieter.get(1), "Rechnung", "/path/to/rechnung1.pdf");
                dokumentRepository.save(dokument2);

                Dokument dokument3 = new Dokument(wohnungen.get(2), null, "Eigentumsnachweis", "/path/to/eigentumsnachweis1.pdf");
                dokumentRepository.save(dokument3);

                Dokument dokument4 = new Dokument(null, mieter.get(2), "Kündigung", "/path/to/kuendigung1.pdf");
                dokumentRepository.save(dokument4);

                Dokument dokument5 = new Dokument(wohnungen.get(3), mieter.get(3), "Nebenkostenabrechnung", "/path/to/nebenkosten1.pdf");
                dokumentRepository.save(dokument5);

                Dokument dokument6 = new Dokument(wohnungen.get(3), mieter.get(3), "Rechnung", "/path/to/rechnung2.pdf");
                dokumentRepository.save(dokument6);

                Dokument dokument7 = new Dokument(wohnungen.get(3), mieter.get(3), "Rechnung", "/path/to/rechnung3.pdf");
                dokumentRepository.save(dokument7);

                Dokument dokument8 = new Dokument(wohnungen.get(3), mieter.get(3), "Rechnung", "/path/to/rechnung4.pdf");
                dokumentRepository.save(dokument8);

                Dokument dokument9 = new Dokument(wohnungen.get(3), mieter.get(3), "Rechnung", "/path/to/rechnung5.pdf");
                dokumentRepository.save(dokument9);

                logger.info("Dokument data loaded.");
            } else {
                logger.warn("No Wohnungen or Mieter found, skipping Dokument data initialization.");
            }
        } else {
            logger.info("Dokument data already exists, skipping initialization.");
        }
    }
}