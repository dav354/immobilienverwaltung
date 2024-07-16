package projektarbeit.immobilienverwaltung.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Configuration;
import projektarbeit.immobilienverwaltung.repository.ConfigurationRepository;

import java.util.Collections;
import java.util.List;

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
     * Setzt den Dark Mode-Status in der Datenbank.
     *
     * @param isDarkMode Der neue Dark Mode-Status.
     */
    @Transactional
    public void setDarkMode(boolean isDarkMode) {
        setBooleanValue("darkMode", isDarkMode);
    }

    /**
     * Prüft, ob der Dark Mode aktiviert ist.
     *
     * @return true, wenn der Dark Mode aktiviert ist, sonst false.
     */
    @Transactional
    public boolean isDarkMode() {
        return getBooleanValue("darkMode", true);
    }

    /**
     * Setzt den Demo-Modus-Status in der Datenbank.
     *
     * @param isDemoMode Der neue Demo-Modus-Status.
     */
    @Transactional
    public void setDemoMode(boolean isDemoMode) {
        setBooleanValue("demoMode", isDemoMode);
    }

    /**
     * Prüft, ob der Demo-Modus aktiviert ist.
     *
     * @return true, wenn der Demo-Modus aktiviert ist, sonst false.
     */
    @Transactional
    public boolean isDemoMode() {
        return getBooleanValue("demoMode", false);
    }

    /**
     * Setzt den Status der Checkbox für vermietete Wohnungen.
     *
     * @param checked Der neue Status der Checkbox für vermietete Wohnungen.
     */
    @Transactional
    public void setVermieteteChecked(boolean checked) {
        setBooleanValue("vermieteteChecked", checked);
    }

    /**
     * Gibt den Status der Checkbox für vermietete Wohnungen zurück.
     *
     * @return true, wenn die Checkbox für vermietete Wohnungen aktiviert ist, sonst false.
     */
    @Transactional
    public boolean getVermieteteChecked() {
        return getBooleanValue("vermieteteChecked", true);
    }

    /**
     * Setzt den Status der Checkbox für unvermietete Wohnungen.
     *
     * @param checked Der neue Status der Checkbox für unvermietete Wohnungen.
     */
    @Transactional
    public void setUnvermieteteChecked(boolean checked) {
        setBooleanValue("unvermieteteChecked", checked);
    }

    /**
     * Gibt den Status der Checkbox für unvermietete Wohnungen zurück.
     *
     * @return true, wenn die Checkbox für unvermietete Wohnungen aktiviert ist, sonst false.
     */
    @Transactional
    public boolean getUnvermieteteChecked() {
        return getBooleanValue("unvermieteteChecked", true);
    }

    /**
     * Speichert die IDs der expandierten Knoten.
     * Die IDs werden als kommaseparierte Zeichenkette in der Konfiguration gespeichert.
     *
     * @param expandedNodeIds Eine Liste der IDs der expandierten Knoten.
     */
    @Transactional
    public void saveExpandedNodes(List<String> expandedNodeIds) {
        Configuration config = configurationRepository.findById("expandedNodes")
                .orElse(new Configuration());
        config.setConfigKey("expandedNodes");
        config.setConfigValue(String.join(",", expandedNodeIds));
        configurationRepository.save(config);
    }

    /**
     * Ruft die IDs der expandierten Knoten ab.
     * Die IDs werden als kommaseparierte Zeichenkette in der Konfiguration gespeichert und hier in eine Liste umgewandelt.
     *
     * @return Eine Liste der IDs der expandierten Knoten. Wenn keine IDs gespeichert sind, wird eine leere Liste zurückgegeben.
     */
    @Transactional
    public List<String> getExpandedNodes() {
        return configurationRepository.findById("expandedNodes")
                .map(config -> List.of(config.getConfigValue().split(",")))
                .orElse(Collections.emptyList());
    }

    /**
     * Setzt den Status des Accordion in der Konfiguration.
     *
     * @param key       Der Schlüssel der Konfiguration.
     * @param isExpanded Der zu setzende boolesche Wert.
     */
    @Transactional
    public void setAccordionState(String key, boolean isExpanded) {
        setBooleanValue(key, isExpanded);
    }

    /**
     * Gibt den Status des Accordion aus der Konfiguration zurück.
     *
     * @param key          Der Schlüssel der Konfiguration.
     * @param defaultValue Der Standardwert, der zurückgegeben wird, wenn der Schlüssel nicht gefunden wird.
     * @return Der boolesche Wert aus der Konfiguration.
     */
    @Transactional
    public boolean getAccordionState(String key, boolean defaultValue) {
        return getBooleanValue(key, defaultValue);
    }

    /**
     * Setzt den Zustand einer Checkbox in der Konfiguration.
     *
     * @param key   Der Schlüssel der Konfiguration.
     * @param value Der zu setzende boolesche Wert für den Checkbox-Zustand.
     */
    @Transactional
    public void setCheckboxState(String key, boolean value) {
        setBooleanValue(key, value);
    }

    /**
     * Gibt den Zustand einer Checkbox aus der Konfiguration zurück.
     *
     * @param key          Der Schlüssel der Konfiguration.
     * @param defaultValue Der Standardwert, der zurückgegeben wird, wenn der Schlüssel nicht gefunden wird.
     * @return Der boolesche Wert aus der Konfiguration.
     */
    @Transactional
    public boolean getCheckboxState(String key, boolean defaultValue) {
        return getBooleanValue(key, defaultValue);
    }

    /**
     * Setzt einen booleschen Wert in der Konfiguration.
     *
     * @param key   Der Schlüssel der Konfiguration.
     * @param value Der zu setzende boolesche Wert.
     */
    @Transactional
    public void setBooleanValue(String key, boolean value) {
        Configuration config = configurationRepository.findById(key).orElse(new Configuration());
        config.setConfigKey(key);
        config.setConfigValue(Boolean.toString(value));
        configurationRepository.save(config);
    }

    /**
     * Gibt einen booleschen Wert aus der Konfiguration zurück.
     *
     * @param key          Der Schlüssel der Konfiguration.
     * @param defaultValue Der Standardwert, der zurückgegeben wird, wenn der Schlüssel nicht gefunden wird.
     * @return Der boolesche Wert aus der Konfiguration.
     */
    @Transactional
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return configurationRepository.findById(key)
                .map(config -> Boolean.parseBoolean(config.getConfigValue()))
                .orElse(defaultValue);
    }
}