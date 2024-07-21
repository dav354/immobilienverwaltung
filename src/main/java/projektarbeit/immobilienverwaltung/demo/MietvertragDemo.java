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
 * Weist Mietverträge (Mietvertrag) den Mietern (Mieter) und Wohnungen (Wohnungen) im Demo-Modus zu.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet.
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
     * Konstruktor für MietvertragDemo.
     *
     * @param demoModeConfig     Die Konfiguration des Demo-Modus.
     * @param mieterRepository   Das Repository zur Verwaltung von Mieter-Entitäten.
     * @param wohnungRepository  Das Repository zur Verwaltung von Wohnungs-Entitäten.
     * @param mietvertragService Der Service zur Verwaltung mietvertragbezogener Operationen.
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
     * Führt die Zuweisung von Mietverträgen an Mieter und Wohnungen aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            assignMietvertragDemo();
        } else {
            logger.info("Demo-Modus ist AUS. Zuweisung der Mietverträge wird übersprungen.");
        }
    }

    /**
     * Weist im Demo-Modus Mietverträge Mietern und Wohnungen zu.
     * Diese Methode prüft, ob genügend Mieter und Wohnungen vorhanden sind, bevor die Zuweisung erfolgt.
     */
    public void assignMietvertragDemo() {
        // Überprüfen, ob Mieter und Wohnungen in der Datenbank vorhanden sind
        if (mieterRepository.count() > 0 && wohnungRepository.count() > 0) {
            logger.info("Zuweisung von Mietvertragsdaten an Mieter und Wohnungen...");

            // Bestehende Wohnungen laden
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Bestehende Mieter laden
            List<Mieter> mieter = mieterRepository.findAll();

            // Überprüfen, ob mindestens 3 Wohnungen und 2 Mieter vorhanden sind
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

                // Mietverträge Mietern und Wohnungen zuweisen
                mietvertragService.createAndSaveMietvertrag(mieter1, wohnung1, LocalDate.now().minusMonths(12), LocalDate.now().plusMonths(12), 1000.00, 500.00, 3);
                mietvertragService.createAndSaveMietvertrag(mieter2, wohnung2, LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(6), 500.00, 250.00, 1);
                mietvertragService.createAndSaveMietvertrag(mieter3, wohnung3, LocalDate.now().minusMonths(24), LocalDate.now().plusMonths(24), 1500.00, 750.00, 2);
                mietvertragService.createAndSaveMietvertrag(mieter4, wohnung4, LocalDate.now().minusMonths(18), LocalDate.now().plusMonths(18), 1200.00, 600.00, 2);
                mietvertragService.createAndSaveMietvertrag(mieter1, wohnung5, LocalDate.now().minusMonths(12), null, 800.00, 400.00, 1);
                mietvertragService.createAndSaveMietvertrag(mieter2, wohnung6, LocalDate.now().minusMonths(6), null, 900.00, 450.00, 2);

                logger.info("Mietvertragsdaten wurden Mietern und Wohnungen zugewiesen.");
            } else {
                logger.warn("Nicht genügend Wohnungen oder Mieter gefunden, Zuweisung wird übersprungen.");
            }
        }
    }
}