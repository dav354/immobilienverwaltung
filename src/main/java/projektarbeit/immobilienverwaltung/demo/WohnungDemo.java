package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.service.WohnungService;
import projektarbeit.immobilienverwaltung.model.Land;

@Component
@Order(1)
public class WohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WohnungDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final WohnungService wohnungService;

    @Autowired
    public WohnungDemo(DemoModeConfig demoModeConfig, WohnungService wohnungService) {
        this.demoModeConfig = demoModeConfig;
        this.wohnungService = wohnungService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadWohnungData();
        } else {
            logger.info("Demo mode is OFF. Skipping loading of Wohnung data.");
        }
    }

    private void loadWohnungData() {
        if (wohnungService.findAllWohnungen().isEmpty()) { // Only load if no data exists
            logger.info("Loading Wohnung data...");

            // Create Wohnungen with their corresponding attributes directly
            Wohnung w1 = new Wohnung("Teststrasse", "11", "07111", "Stuttgart", Land.DE, 200, 1900, 2, 2, true, true, true, true);
            wohnungService.save(w1);

            Wohnung w2 = new Wohnung("Alphastreet", "420a", "338474", "Würzburg", Land.DE, 50, 2000, 1, 1, false, false, false, false);
            wohnungService.save(w2);

            Wohnung w3 = new Wohnung("Beispielweg", "22", "12345", "Bern", Land.CH, 120, 1985, 1, 3, true, false, true, false);
            wohnungService.save(w3);

            Wohnung w4 = new Wohnung("Musterallee", "33b", "54321", "Hamburg", Land.DE, 85, 1995, 2, 2, false, true, false, true);
            wohnungService.save(w4);

            Wohnung w5 = new Wohnung("Betastreet", "1111", "9876", "Oberhausen", Land.DE, 222, 1967, 2, 2, true, true, true, true);
            wohnungService.save(w5);

            Wohnung w6 = new Wohnung("Lolgasse", "69", "77777", "Berlin", Land.DE, 35, 1943, 1, 1, false, false, false, false);
            wohnungService.save(w6);

            Wohnung w7 = new Wohnung("strasseb", "333", "88888", "Zürich", Land.CH, 44, 1877, 1, 3, true, false, true, false);
            wohnungService.save(w7);

            Wohnung w8 = new Wohnung("Königsstraße", "1", "54555", "Altona", Land.DE, 55, 2008, 2, 2, false, true, false, true);
            wohnungService.save(w8);

            Wohnung w9 = new Wohnung("Königsstraße", "2", "54555", "Altona", Land.DE, 55, 2008, 2, 2, false, true, false, true);
            wohnungService.save(w9);

            Wohnung w10 = new Wohnung("Königsstraße", "1", "54555", "Altona", Land.DE, 55, 2008, 2, 2, false, true, false, true);
            wohnungService.save(w10);

            logger.info("Wohnung data loaded.");
        } else {
            logger.info("Wohnung data already exists, skipping initialization.");
        }
    }
}