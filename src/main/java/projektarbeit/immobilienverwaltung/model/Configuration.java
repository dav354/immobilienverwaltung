package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Entity-Klasse zur Darstellung einer Konfigurationseinstellung.
 * Diese Klasse enthält die Schlüssel-Wert-Paare für Konfigurationseinstellungen.
 */
@Entity
public class Configuration {

    @Id
    private String configKey;

    private String configValue;

    /**
     * Ruft den Schlüssel der Konfigurationseinstellung ab.
     *
     * @return der Schlüssel der Konfigurationseinstellung.
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Setzt den Schlüssel der Konfigurationseinstellung.
     *
     * @param configKey der Schlüssel der Konfigurationseinstellung.
     */
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    /**
     * Ruft den Wert der Konfigurationseinstellung ab.
     *
     * @return der Wert der Konfigurationseinstellung.
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * Setzt den Wert der Konfigurationseinstellung.
     *
     * @param configValue der Wert der Konfigurationseinstellung.
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}