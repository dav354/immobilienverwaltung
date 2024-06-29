package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@DataJpaTest
public class MieterRepositoryTest {

    @Autowired
    private MieterRepository mieterRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    /**
     * Testet das Speichern und Finden eines Mieters.
     */
    @Test
    public void testSaveAndFindMieter() {
        // Erstellen und Speichern einer Wohnung
        Wohnung wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Erstellen und Speichern eines Mieters
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        // Abrufen des Mieters
        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);

        // Überprüfen der Ergebnisse
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mustermann");
        assertThat(found.getVorname()).isEqualTo("Max");
        assertThat(found.getTelefonnummer()).isEqualTo("491234567890");
        assertThat(found.getEmail()).isEqualTo("max@mustermann.de");
        assertThat(found.getEinkommen()).isEqualTo(3000);
    }

    /**
     * Testet das Suchen von Mietern anhand eines Suchbegriffs.
     */
    @Test
    public void testSearchMieter() {
        // Erstellen und Speichern von Mietern
        Mieter mieter1 = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        Mieter mieter2 = new Mieter("Meier", "Anna", "491234567891", "anna@meier.de", 3200);
        mieterRepository.save(mieter1);
        mieterRepository.save(mieter2);

        // Suchen von Mietern mit dem Suchbegriff "Mustermann"
        List<Mieter> results = mieterRepository.search("Mustermann");

        // Überprüfen der Ergebnisse
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Mustermann");
        assertThat(results.get(0).getVorname()).isEqualTo("Max");
    }

    /**
     * Testet, ob ein Mieter mit einer bestimmten E-Mail-Adresse existiert.
     */
    @Test
    public void testExistsByEmail() {
        // Erstellen und Speichern eines Mieters
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        // Überprüfen, ob der Mieter mit der angegebenen E-Mail-Adresse existiert
        boolean exists = mieterRepository.existsByEmail("max@mustermann.de");

        // Überprüfen der Ergebnisse
        assertThat(exists).isTrue();
    }

    /**
     * Testet das Löschen eines Mieters.
     */
    @Test
    public void testDeleteMieter() {
        // Erstellen und Speichern eines Mieters
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        // Löschen des Mieters
        mieterRepository.delete(mieter);

        // Überprüfen, ob der Mieter gelöscht wurde
        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        assertThat(found).isNull();
    }
}