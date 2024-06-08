package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

/*
@DataJpaTest
public class MieterRepositoryTest {

    @Autowired
    private MieterRepository mieterRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Test
    public void testSaveAndFindMieter() {
        // Create and save a Wohnung
        Wohnung w = new Wohnung("07111", "Stuttgart", "01111", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(w);

        // Create and save a Mieter
        Mieter mieter = new Mieter("Mustermann", "Max", "491234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        mieterRepository.save(mieter);

        // Associate Mieter with Wohnung
        w.setMieter(mieter);
        wohnungRepository.save(w);

        // Retrieve the Mieter
        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);

        // Assert the results
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mustermann");
        assertThat(found.getMietbeginn()).isEqualTo(LocalDate.of(2022, 1, 1));
        assertThat(found.getMietende()).isEqualTo(LocalDate.of(2024, 1, 1));
    }
}

 */