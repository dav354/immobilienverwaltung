package projektarbeit.immobilienverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse der Immobilienverwaltungsanwendung.
 * Diese Klasse konfiguriert und startet die Spring Boot-Anwendung.
 */
@SpringBootApplication
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