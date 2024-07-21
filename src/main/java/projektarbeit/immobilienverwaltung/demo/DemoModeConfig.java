package projektarbeit.immobilienverwaltung.demo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;

/**
 * Konfigurationsklasse zur Verwaltung des Demo-Modus.
 * Diese Klasse liest die Einstellung des Demo-Modus aus den Anwendungseigenschaften und bietet eine Methode zur Überprüfung, ob der Demo-Modus aktiviert ist.
 */
@Component
public class DemoModeConfig {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeConfig.class);

    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;
    private final ConfigurationService configurationService;

    private boolean isDemoMode = false;

    /**
     * Die Einstellung des Demo-Modus, gelesen aus den Anwendungseigenschaften.
     */
    @Value("${demo.mode:false}")
    private String demoMode;

    /**
     * Konstruktor für DemoModeConfig.
     *
     * @param mieterRepository     Das Repository zur Verwaltung von Mieter-Entitäten.
     * @param wohnungRepository    Das Repository zur Verwaltung von Wohnungs-Entitäten.
     * @param configurationService Der Service zur Verwaltung von Konfigurationseinstellungen.
     */
    public DemoModeConfig(MieterRepository mieterRepository,
                          WohnungRepository wohnungRepository,
                          ConfigurationService configurationService) {
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.configurationService = configurationService;
    }

    /**
     * Diese Methode wird nach der Konstruktion des Beans aufgerufen und überprüft und setzt den Demo-Modus-Status.
     */
    @PostConstruct
    private void checkAndSetDemoMode() {
        // Konvertiere den demoMode-String in einen boolean-Wert
        if ("true".equalsIgnoreCase(demoMode)) {
            isDemoMode = true;
        } else if ("false".equalsIgnoreCase(demoMode)) {
            isDemoMode = false;
        } else {
            logger.error("Ungültiger Wert für demo.mode: {}. Erwartet 'true' oder 'false'.", demoMode);
            System.exit(1);
        }

        if (isDemoMode) {
            if (configurationService.isDemoMode()) {
                isDemoMode = true;
            } else {
                if (mieterRepository.count() == 0 && wohnungRepository.count() == 0) {
                    logger.info("DEMO Mode aktiv. DEMO Daten werden geladen");
                    isDemoMode = true;
                    configurationService.setDemoMode(true);
                } else {
                    logger.warn("DEMO Mode aktiv, aber es existieren bereits Daten. Überspringe laden der Demo Daten.");
                    isDemoMode = false;
                    configurationService.setDemoMode(false);
                }
            }
        }
    }

    /**
     * Überprüft, ob der Demo-Modus aktiviert ist.
     *
     * @return true, wenn der Demo-Modus aktiviert ist, sonst false.
     */
    public boolean isDemoMode() {
        return isDemoMode;
    }
}