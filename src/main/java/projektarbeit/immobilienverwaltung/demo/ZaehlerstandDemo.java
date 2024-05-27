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

@Component
@Order(3)
public class ZaehlerstandDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ZaehlerstandDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final WohnungRepository wohnungRepository;

    public ZaehlerstandDemo(DemoModeConfig demoModeConfig,
                            ZaehlerstandRepository zaehlerstandRepository,
                            WohnungRepository wohnungRepository) {
        this.demoModeConfig = demoModeConfig;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.wohnungRepository = wohnungRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadZaehlerstandData();
        } else {
            logger.info("Demo mode is OFF. Skipping loading of Zaehlerstand data.");
        }
    }

    private void loadZaehlerstandData() {
        if (zaehlerstandRepository.count() == 0) { // Only load if no data exists
            // Load existing Wohnungen
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Create Zaehlerstand
            if (!wohnungen.isEmpty()) {
                Zaehlerstand zaehlerstand1 = new Zaehlerstand(wohnungen.get(0), LocalDate.of(2023, 1, 1), 100.0);
                zaehlerstandRepository.save(zaehlerstand1);

                Zaehlerstand zaehlerstand2 = new Zaehlerstand(wohnungen.get(1), LocalDate.of(2023, 6, 1), 200.0);
                zaehlerstandRepository.save(zaehlerstand2);

                Zaehlerstand zaehlerstand3 = new Zaehlerstand(wohnungen.get(2), LocalDate.of(2023, 11, 1), 300.0);
                zaehlerstandRepository.save(zaehlerstand3);

                Zaehlerstand zaehlerstand4 = new Zaehlerstand(wohnungen.get(3), LocalDate.of(2023, 12, 1), 400.0);
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