package projektarbeit.immobilienverwaltung.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Mieter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MieterRepositoryTest {

    @Autowired
    private MieterRepository mieterRepository;

    @Test
    public void testSaveAndFindMieter() {
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mustermann");
        assertThat(found.getVorname()).isEqualTo("Max");
        assertThat(found.getTelefonnummer()).isEqualTo("491234567890");
        assertThat(found.getEmail()).isEqualTo("max@mustermann.de");
        assertThat(found.getEinkommen()).isEqualTo(3000);
    }

    @Test
    public void testSearchMieter() {
        Mieter mieter1 = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        Mieter mieter2 = new Mieter("Meier", "Anna", "491234567891", "anna@meier.de", 3200);
        mieterRepository.save(mieter1);
        mieterRepository.save(mieter2);

        List<Mieter> results = mieterRepository.search("Mustermann");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("Mustermann");
        assertThat(results.getFirst().getVorname()).isEqualTo("Max");
    }

    @Test
    public void testExistsByEmail() {
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        boolean exists = mieterRepository.existsByEmail("max@mustermann.de");

        assertThat(exists).isTrue();
    }

    @Test
    public void testDeleteMieter() {
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        mieterRepository.save(mieter);

        mieterRepository.delete(mieter);

        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testSearchMieterEmptyResult() {
        Mieter mieter1 = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3000);
        Mieter mieter2 = new Mieter("Meier", "Anna", "491234567891", "anna@meier.de", 3200);
        mieterRepository.save(mieter1);
        mieterRepository.save(mieter2);

        List<Mieter> results = mieterRepository.search("NonExistent");

        assertThat(results).isEmpty();
    }

    @Test
    public void testExistsByEmailFalse() {
        boolean exists = mieterRepository.existsByEmail("nonexistent@mustermann.de");

        assertThat(exists).isFalse();
    }

    @Test
    public void testSaveMieterWithNullFields() {
        Mieter mieter = new Mieter(null, "Max", "491234567890", "max@mustermann.de", 3000);
        assertThrows(ConstraintViolationException.class, () -> mieterRepository.save(mieter));
    }
}