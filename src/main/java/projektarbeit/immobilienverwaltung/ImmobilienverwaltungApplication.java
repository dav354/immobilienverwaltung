package projektarbeit.immobilienverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hauptklasse der Immobilienverwaltungsanwendung.
 * Diese Klasse konfiguriert und startet die Spring Boot-Anwendung.
 */
@SpringBootApplication
@EntityScan(basePackages = "projektarbeit.immobilienverwaltung.model")
@EnableJpaRepositories(basePackages = "projektarbeit.immobilienverwaltung.repository")
public class ImmobilienverwaltungApplication {

    /**
     * Der Haupteinstiegspunkt der Spring Boot-Anwendung.
     *
     * @param args die Befehlszeilenargumente.
     */
    public static void main(String[] args) {
        SpringApplication.run(ImmobilienverwaltungApplication.class, args);
    }
}
