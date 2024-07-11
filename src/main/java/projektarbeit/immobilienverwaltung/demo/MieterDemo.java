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
import java.time.LocalDate;
import java.util.List;

/**
 * Initialisiert die Mieterdaten zu Demozwecken.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet, falls der Demo-Modus aktiviert ist.
 */
@Component
@Order(2)
public class MieterDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MieterDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;

    /**
     * Konstruktor für MieterDemo.
     *
     * @param demoModeConfig    Die Konfiguration des Demo-Modus.
     * @param mieterRepository  Das Repository zur Verwaltung von Mieter-Entitäten.
     * @param wohnungRepository Das Repository zur Verwaltung von Wohnungs-Entitäten.
     */
    public MieterDemo(DemoModeConfig demoModeConfig,
                      MieterRepository mieterRepository,
                      WohnungRepository wohnungRepository) {
        this.demoModeConfig = demoModeConfig;
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
    }

    /**
     * Führt die Initialisierung der Mieterdaten aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadMieterData();
        } else {
            logger.info("Demo-Modus ist AUS. Laden der Mieterdaten wird übersprungen.");
        }
    }

    /**
     * Lädt Mieterdaten zu Demozwecken.
     * Diese Methode prüft, ob keine bestehenden Mietereinträge vorhanden sind, bevor die Daten geladen werden.
     */
    public void loadMieterData() {
        if (mieterRepository.count() == 0) { // Nur laden, wenn keine Daten existieren
            logger.info("Laden der Mieterdaten...");

            // Bestehende Wohnungen laden
            List<Wohnung> wohnungen = wohnungRepository.findAll();

            // Mietereinträge erstellen, falls Wohnungen verfügbar sind
            if (!wohnungen.isEmpty()) {
                Mieter mieter1 = new Mieter("Doe", "John", "0123456789", "john@doe.com", 3000);
                mieterRepository.save(mieter1);

                Mieter mieter2 = new Mieter("Doe", "Jane", "9876543210","jane@doe.com", 4000);
                mieterRepository.save(mieter2);

                Mieter mieter3 = new Mieter("Mustermann", "Max", "1234567890","max.mustermann@web.de", 2500);
                mieterRepository.save(mieter3);

                Mieter mieter4 = new Mieter("Mustermann", "Erika", "0987654321", "erika.musterfrau@gmx.de",2800);
                mieterRepository.save(mieter4);

                Mieter mieter5 = new Mieter("Schmidt", "Peter", "0123456788", "peter.schmitt83@gmail.com", 3200);
                mieterRepository.save(mieter5);

                Mieter mieter6 = new Mieter("Schneider", "Laura", "9876543211","laura.schneider03@icloud.com", 4200);
                mieterRepository.save(mieter6);

                Mieter mieter7 = new Mieter("Matt", "John", "0123456789","jomat@tuta.de", 3000);
                mieterRepository.save(mieter7);

                Mieter mieter8 = new Mieter("Matt", "Jane", "9876543210", "jamat@tuta.de",4000);
                mieterRepository.save(mieter8);

                Mieter mieter9 = new Mieter("Musterfrau", "Max", "1234567890","MaxMusterfrau@gmail.com", 2500);
                mieterRepository.save(mieter9);

                Mieter mieter10 = new Mieter("Musterfrau", "Erika", "0987654321", "erika@musterfrau.de", 2800);
                mieterRepository.save(mieter10);

                Mieter mieter11 = new Mieter("Becker", "Lukas", "0123456788", "becker.lukas@gmail.com", 3200);
                mieterRepository.save(mieter11);

                Mieter mieter12 = new Mieter("Schneider", "Moritz", "9876543211","moritz.schneider@mailbox.org", 4200);
                mieterRepository.save(mieter12);

                logger.info("Mieterdaten geladen.");
            } else {
                logger.warn("Keine Wohnungen gefunden, Initialisierung der Mieterdaten wird übersprungen.");
            }
        } else {
            logger.info("Mieterdaten existieren bereits, Initialisierung wird übersprungen.");
        }
    }
}