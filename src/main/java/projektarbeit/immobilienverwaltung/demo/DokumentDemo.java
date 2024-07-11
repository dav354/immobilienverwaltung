package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Initialisiert die Dokumentdaten zu Demozwecken.
 * Diese Klasse wird als Command Line Runner ausgeführt, wenn die Anwendung startet, falls der Demo-Modus aktiviert ist.
 */
@Component
@Order(5)
public class DokumentDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DokumentDemo.class);

    private final DemoModeConfig demoModeConfig;
    private final DokumentRepository dokumentRepository;
    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;

    /**
     * Konstruktor für DokumentDemo.
     *
     * @param demoModeConfig    Die Konfiguration des Demo-Modus.
     * @param dokumentRepository Das Repository zur Verwaltung von Dokument-Entitäten.
     * @param wohnungRepository  Das Repository zur Verwaltung von Wohnungs-Entitäten.
     * @param mieterRepository   Das Repository zur Verwaltung von Mieter-Entitäten.
     */
    public DokumentDemo(DemoModeConfig demoModeConfig,
                        DokumentRepository dokumentRepository,
                        WohnungRepository wohnungRepository,
                        MieterRepository mieterRepository) {
        this.demoModeConfig = demoModeConfig;
        this.dokumentRepository = dokumentRepository;
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
    }

    /**
     * Führt die Initialisierung der Dokumentdaten aus, falls der Demo-Modus aktiviert ist.
     *
     * @param args Kommandozeilenargumente.
     * @throws Exception wenn ein Fehler während des Vorgangs auftritt.
     */
    @Override
    public void run(String... args) throws Exception {
        if (demoModeConfig.isDemoMode()) {
            loadDokumentData();
        } else {
            logger.info("Demo-Modus ist AUS. Laden der Dokumentdaten wird übersprungen.");
        }
    }

    /**
     * Lädt Dokumentdaten zu Demozwecken.
     * Diese Methode prüft, ob keine bestehenden Dokumenteinträge vorhanden sind, bevor die Daten geladen werden.
     */
    private void loadDokumentData() {
        if (dokumentRepository.count() == 0) { // Nur laden, wenn keine Daten existieren
            logger.info("Laden der Dokumentdaten...");

            // Bestehende Wohnungen und Mieter laden
            List<Wohnung> wohnungen = wohnungRepository.findAll();
            List<Mieter> mieterList = mieterRepository.findAll();

            // Dokumenteinträge für jeden Mieter erstellen
            if (!wohnungen.isEmpty() && !mieterList.isEmpty()) {
                for (int i = 0; i < mieterList.size(); i++) {
                    Mieter mieter = mieterList.get(i);
                    Wohnung wohnung = i < wohnungen.size() ? wohnungen.get(i) : null;

                    Dokument mietvertrag = new Dokument(wohnung, mieter, "Mietvertrag", "/path/to/mietvertrag" + (i + 1) + ".pdf");
                    dokumentRepository.save(mietvertrag);

                    Dokument rechnung = new Dokument(wohnung, mieter, "Rechnung", "/path/to/rechnung" + (i + 1) + ".pdf");
                    dokumentRepository.save(rechnung);

                    Dokument nebenkosten = new Dokument(wohnung, mieter, "Nebenkostenabrechnung Wasser", "/path/to/nebenkosten" + (i + 1) + ".pdf");
                    dokumentRepository.save(nebenkosten);

                    Dokument nebenkosten2 = new Dokument(wohnung, mieter, "Nebenkostenabrechnung Strom", "/path/to/nebenkosten" + (i + 1) + ".pdf");
                    dokumentRepository.save(nebenkosten2);

                    Dokument perso = new Dokument(null, mieter, "Personalausweis", "/path/to/eigentumsnachweis" + (i + 1) + ".pdf");
                    dokumentRepository.save(perso);
                }

                for (int i = 0; i < wohnungen.size(); i++) {
                    Mieter mieter = mieterList.get(i);
                    Wohnung wohnung = wohnungen.get(i);

                    Dokument grundriss = new Dokument(wohnung, null, "Grundriss", "/path/to/eigentumsnachweis" + (i + 1) + ".pdf");
                    dokumentRepository.save(grundriss);

                    Dokument foto1 = new Dokument(wohnung, null, "Bild-Küche", "/path/to/picture028932" + (i + 1) + ".png");
                    dokumentRepository.save(foto1);

                    Dokument foto2 = new Dokument(wohnung, null, "Bild-Wohnzimmer", "/path/to/picture98423" + (i + 1) + ".png");
                    dokumentRepository.save(foto2);
                }

                logger.info("Dokumentdaten geladen.");
            } else {
                logger.warn("Keine Wohnungen oder Mieter gefunden, Initialisierung der Dokumentdaten wird übersprungen.");
            }
        } else {
            logger.info("Dokumentdaten existieren bereits, Initialisierung wird übersprungen.");
        }
    }
}