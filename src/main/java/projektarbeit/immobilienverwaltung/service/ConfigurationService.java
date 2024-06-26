package projektarbeit.immobilienverwaltung.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Configuration;
import projektarbeit.immobilienverwaltung.repository.ConfigurationRepository;

import java.util.Optional;

/**
 * Service-Klasse zur Verwaltung von Konfigurationseinstellungen.
 * Diese Klasse bietet Methoden zum Speichern und Abrufen von Konfigurationswerten aus der Datenbank.
 */
@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    /**
     * Konstruktor für ConfigurationService.
     *
     * @param configurationRepository Das Repository zur Verwaltung von Configuration-Entitäten.
     */
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    /**
     * Findet eine Konfigurationseinstellung anhand des Schlüssels.
     *
     * @param key Der Schlüssel der Konfigurationseinstellung.
     * @return Ein Optional, das die gefundene Konfigurationseinstellung enthält, oder ein leeres Optional, wenn keine Einstellung mit dem Schlüssel gefunden wurde.
     */
    @Transactional
    public Optional<Configuration> findByKey(String key) {
        return configurationRepository.findById(key);
    }

    /**
     * Speichert eine Konfigurationseinstellung in der Datenbank.
     *
     * @param config Die zu speichernde Konfigurationseinstellung.
     */
    @Transactional
    public void save(@Valid Configuration config) {
        configurationRepository.save(config);
    }

    /**
     * Setzt den Dark Mode-Status in der Datenbank.
     *
     * @param isDarkMode Der neue Dark Mode-Status.
     */
    @Transactional
    public void setDarkMode(boolean isDarkMode) {
        Configuration config = configurationRepository.findById("darkMode")
                .orElse(new Configuration());
        config.setConfigKey("darkMode");
        config.setConfigValue(Boolean.toString(isDarkMode));
        configurationRepository.save(config);
    }

    /**
     * Prüft, ob der Dark Mode aktiviert ist.
     *
     * @return true, wenn der Dark Mode aktiviert ist, sonst false.
     */
    @Transactional
    public boolean isDarkMode() {
        return configurationRepository.findById("darkMode")
                .map(config -> Boolean.parseBoolean(config.getConfigValue()))
                .orElse(true);
    }


}