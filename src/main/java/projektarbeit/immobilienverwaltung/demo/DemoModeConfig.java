package projektarbeit.immobilienverwaltung.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration class to manage the demo mode setting.
 * This class reads the demo mode setting from the application properties and provides a method to check if the demo mode is enabled.
 */
@Component
public class DemoModeConfig {

    /**
     * The demo mode setting read from application properties.
     * The default value is "FALSE" if the property is not set.
     */
    @Value("${demo.mode:FALSE}")
    private String demoMode;

    /**
     * Checks if the demo mode is enabled.
     *
     * @return true if demo mode is set to "TRUE", false otherwise.
     */
    public boolean isDemoMode() {
        return "TRUE".equalsIgnoreCase(demoMode);
    }
}