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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;
import java.util.List;

/**
 * Initialisiert die Zaehlerstand-Daten (Zählerstände) zu Demozwecken.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet, falls der Demo-Modus aktiviert ist.
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
     * Konstruktor für ZaehlerstandDemo.
     *
     * @param demoModeConfig       Die Konfiguration des Demo-Modus.
     * @param zaehlerstandRepository Das Repository zur Verwaltung von Zaehlerstand-Entitäten.
     * @param wohnungRepository    Das Repository zur Verwaltung von Wohnungs-Entitäten.
     */
    public ZaehlerstandDemo(DemoModeConfig demoModeConfig,
                            ZaehlerstandRepository zaehlerstandRepository,
                            WohnungRepository wohnungRepository) {
        this.demoModeConfig = demoModeConfig;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.wohnungRepository = wohnungRepository;
    }

    /**
     * Führt die Initialisierung der Zaehlerstand-Daten aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadZaehlerstandData();
        } else {
            logger.info("Demo-Modus ist AUS. Laden der Zaehlerstand-Daten wird übersprungen.");
        }
    }

    /**
     * Lädt Zaehlerstand-Daten zu Demozwecken.
     * Diese Methode prüft, ob keine bestehenden Zaehlerstand-Einträge vorhanden sind, bevor die Daten geladen werden.
     */
    private void loadZaehlerstandData() {
        if (zaehlerstandRepository.count() == 0) { // Nur laden, wenn keine Daten existieren
            logger.info("Laden der Zaehlerstand-Daten...");

            // Bestehende Wohnungen laden
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Zaehlerstand-Einträge erstellen, falls Wohnungen verfügbar sind
            if (!wohnungen.isEmpty()) {
                Random random = new Random();

                for (Wohnung wohnung : wohnungen) {
                    double stromValue = 10000 * random.nextDouble();
                    double gasValue = 1000 * random.nextDouble();
                    double wasserValue = 10000 * random.nextDouble();

                    stromValue = roundToTwoDecimalPlaces(stromValue);
                    gasValue = roundToTwoDecimalPlaces(gasValue);
                    wasserValue = roundToTwoDecimalPlaces(wasserValue);

                    Zaehlerstand strom = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), stromValue, "Strom");
                    zaehlerstandRepository.save(strom);

                    Zaehlerstand gas = new Zaehlerstand(wohnung, LocalDate.of(2023, 6, 1), gasValue, "Gas");
                    zaehlerstandRepository.save(gas);

                    Zaehlerstand wasser = new Zaehlerstand(wohnung, LocalDate.of(2023, 11, 1), wasserValue, "Wasser");
                    zaehlerstandRepository.save(wasser);
                }
                logger.info("Zaehlerstand-Daten geladen.");
            } else {
                logger.warn("Keine Wohnungen gefunden, Initialisierung der Zaehlerstand-Daten wird übersprungen.");
            }
        } else {
            logger.info("Zaehlerstand-Daten existieren bereits, Initialisierung wird übersprungen.");
        }
    }

    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}