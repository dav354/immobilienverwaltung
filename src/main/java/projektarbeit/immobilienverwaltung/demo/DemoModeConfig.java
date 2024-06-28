package projektarbeit.immobilienverwaltung.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.UserRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

/**
 * Konfigurationsklasse zur Verwaltung des Demo-Modus.
 * Diese Klasse liest die Einstellung des Demo-Modus aus den Anwendungseigenschaften und bietet eine Methode zur Überprüfung, ob der Demo-Modus aktiviert ist.
 */
@Component
public class DemoModeConfig {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeConfig.class);

    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;
    private final UserRepository userRepository;

    /**
     * Konstruktor für DemoModeConfig.
     *
     * @param mieterRepository  Das Repository zur Verwaltung von Mieter-Entitäten.
     * @param wohnungRepository Das Repository zur Verwaltung von Wohnungs-Entitäten.
     * @param userRepository    Das Repository zur Verwaltung von Benutzer-Entitäten.
     */
    public DemoModeConfig(MieterRepository mieterRepository,
                          WohnungRepository wohnungRepository,
                          UserRepository userRepository) {
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.userRepository = userRepository;
    }

    /**
     * Die Einstellung des Demo-Modus, gelesen aus den Anwendungseigenschaften.
     * Der Standardwert ist "FALSE", wenn die Eigenschaft nicht gesetzt ist.
     */
    @Value("${demo.mode:FALSE}")
    private String demoMode;

    /**
     * Überprüft, ob der Demo-Modus aktiviert ist.
     * Der Demo-Modus wird nur aktiviert, wenn keine Mieter und Wohnungen in der Datenbank vorhanden sind,
     * aber Benutzer existieren.
     *
     * @return true, wenn der Demo-Modus auf "TRUE" gesetzt ist und die Datenbank leer ist, sonst false.
     */
    public boolean isDemoMode() {
        // Überprüfen, ob keine Mieter und Wohnungen existieren, aber Benutzer vorhanden sind
        if (mieterRepository.count() == 0 && wohnungRepository.count() == 0 && userRepository.count() != 0) {
            logger.info("DEMO Mode aktiv. DEMO Daten werden geladen");
            return "TRUE".equalsIgnoreCase(demoMode);
        }
        // Wenn Daten existieren, wird das Laden der Demo-Daten übersprungen
        logger.warn("DEMO Mode aktiv, aber es existieren bereits Daten. Überspringe laden der Demo Daten.");
        return "FALSE".equalsIgnoreCase(demoMode);
    }
}