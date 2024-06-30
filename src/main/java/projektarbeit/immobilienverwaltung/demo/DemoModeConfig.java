package projektarbeit.immobilienverwaltung.demo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.Configuration;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.UserRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.service.ConfigurationService;

import java.util.Optional;

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
     * @param userRepository       Das Repository zur Verwaltung von Benutzer-Entitäten.
     * @param configurationService Der Service zur Verwaltung von Konfigurationseinstellungen.
     */
    public DemoModeConfig(MieterRepository mieterRepository,
                          WohnungRepository wohnungRepository,
                          UserRepository userRepository,
                          ConfigurationService configurationService) {
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.userRepository = userRepository;
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
            Optional<Configuration> demoModeConfig = configurationService.findByKey("demo.mode.disable");

            if (demoModeConfig.isPresent()) {
                isDemoMode = Boolean.parseBoolean(demoModeConfig.get().getConfigValue());
            } else {
                if (mieterRepository.count() == 0 && wohnungRepository.count() == 0) {
                    logger.info("DEMO Mode aktiv. DEMO Daten werden geladen");
                    isDemoMode = true;
                    Configuration newConfig = new Configuration();
                    newConfig.setConfigKey("demo.mode.disable");
                    newConfig.setConfigValue("false");
                    configurationService.save(newConfig);
                } else {
                    logger.warn("DEMO Mode aktiv, aber es existieren bereits Daten. Überspringe laden der Demo Daten.");
                    isDemoMode = false;
                    Configuration newConfig = new Configuration();
                    newConfig.setConfigKey("demo.mode.disable");
                    newConfig.setConfigValue("disable");
                    configurationService.save(newConfig);
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