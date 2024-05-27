package projektarbeit.immobilienverwaltung.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DemoModeConfig {

    @Value("${demo.mode:FALSE}") // Standardwert ist "OFF"
    private String demoMode;

    public boolean isDemoMode() {
        return "TRUE".equalsIgnoreCase(demoMode);
    }
}