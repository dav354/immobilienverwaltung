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

/**
 * Initialisiert die Wohnungsdaten zu Demozwecken.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet, falls der Demo-Modus aktiviert ist.
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@Order(1)
public class WohnungDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WohnungDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final WohnungService wohnungService;

    /**
     * Konstruktor für WohnungDemo.
     *
     * @param demoModeConfig Die Konfiguration des Demo-Modus.
     * @param wohnungService Der Service zur Verwaltung von Wohnung-Entitäten.
     */
    @Autowired
    public WohnungDemo(DemoModeConfig demoModeConfig, WohnungService wohnungService) {
        this.demoModeConfig = demoModeConfig;
        this.wohnungService = wohnungService;
    }

    /**
     * Führt die Initialisierung der Wohnungsdaten aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadWohnungData();
        } else {
            logger.info("Demo-Modus ist AUS. Laden der Wohnungsdaten wird übersprungen.");
        }
    }

    /**
     * Lädt Wohnungsdaten zu Demozwecken.
     * Diese Methode prüft, ob keine bestehenden Wohnungseinträge vorhanden sind, bevor die Daten geladen werden.
     */
    private void loadWohnungData() {
        if (wohnungService.findAllWohnungen().isEmpty()) { // Nur laden, wenn keine Daten existieren
            logger.info("Laden der Wohnungsdaten...");

            // Wohnungen mit ihren entsprechenden Attributen direkt erstellen
            Wohnung w1 = new Wohnung("Münzstraße", "12", "97070", "Würzburg", Land.DE, 200, 1900, 2, 2, true, true, true, true, null, null);
            w1.setLatitude(49.794500); w1.setLongitude(9.930800);
            wohnungService.save(w1);

            Wohnung w2 = new Wohnung("Geschwister-Scholl-Platz", "1", "80539", "München", Land.DE, 50, 2000, 1, 1, false, false, false, false, null, null);
            w2.setLatitude(48.150800); w2.setLongitude(11.580200);
            wohnungService.save(w2);

            Wohnung w3 = new Wohnung("Keplerstraße", "7", "70174", "Stuttgart", Land.DE, 120, 1985, 1, 3, true, false, true, false, null, null);
            w3.setLatitude(48.781500); w3.setLongitude(9.172300);
            wohnungService.save(w3);

            Wohnung w4 = new Wohnung("Rämistrasse", "71", "8006", "Zürich", Land.CH, 85, 1995, 2, 2, false, true, false, true, null, null);
            w4.setLatitude(47.376900); w4.setLongitude(8.541700);
            wohnungService.save(w4);

            Wohnung w5 = new Wohnung("Unter den Linden", "6", "10117", "Berlin", Land.DE, 222, 1967, 2, 2, true, true, true, true, null, null);
            w5.setLatitude(52.516300); w5.setLongitude(13.377700);
            wohnungService.save(w5);

            Wohnung w6 = new Wohnung("Adickesallee", "32", "60322", "Frankfurt am Main", Land.DE, 35, 1943, 1, 1, false, false, false, false, null, null);
            wohnungService.save(w6);

            Wohnung w7 = new Wohnung("Universitätsstraße", "2", "86159", "Augsburg", Land.DE, 44, 1877, 1, 3, true, false, true, false, null, null);
            wohnungService.save(w7);

            Wohnung w8 = new Wohnung("Augustinerstraße", "26", "97070", "Würzburg", Land.DE, 55, 2008, 2, 2, false, true, false, true, "1", "1");
            wohnungService.save(w8);

            Wohnung w9 = new Wohnung("Augustinerstraße", "26", "97070", "Würzburg", Land.DE, 55, 2008, 2, 2, false, true, false, true, "1", "2");
            wohnungService.save(w9);

            Wohnung w10 = new Wohnung("Augustinerstraße", "26", "97070", "Würzburg", Land.DE, 55, 2008, 2, 2, false, true, false, true, "2", "3");
            wohnungService.save(w10);

            Wohnung w11 = new Wohnung("Augustinerstraße", "26", "97070", "Würzburg", Land.DE, 55, 2008, 2, 2, false, true, false, true, "2", "4");
            wohnungService.save(w11);

            Wohnung w12 = new Wohnung("Augustinerstraße", "26", "97070", "Würzburg", Land.DE, 55, 2008, 2, 2, false, true, false, true, "2", "5");
            wohnungService.save(w12);

            logger.info("Wohnungsdaten geladen.");
        } else {
            logger.info("Wohnungsdaten existieren bereits, Initialisierung wird übersprungen.");
        }
    }
}