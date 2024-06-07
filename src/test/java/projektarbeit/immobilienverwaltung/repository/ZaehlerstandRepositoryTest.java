package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

@DataJpaTest
public class ZaehlerstandRepositoryTest {

    @Autowired
    private ZaehlerstandRepository zaehlerstandRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Test
    public void testSaveAndFindZaehlerstand() {
        // Create and save a Wohnung
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "9473", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Create and save a Zaehlerstand
        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        zaehlerstandRepository.save(zaehlerstand);

        // Retrieve the Zaehlerstand
        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);

        // Assert the results
        assertThat(found).isNotNull();
        assertThat(found.getAblesedatum()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(found.getAblesewert()).isEqualTo(1234.56);
    }

    @Test
    public void testFindByWohnung() {
        // Create and save a Wohnung
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "34321", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Create and save Zaehlerstaende
        Zaehlerstand zaehlerstand1 = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        Zaehlerstand zaehlerstand2 = new Zaehlerstand(wohnung, LocalDate.of(2023, 2, 1), 5678.90);
        zaehlerstandRepository.save(zaehlerstand1);
        zaehlerstandRepository.save(zaehlerstand2);

        // Retrieve Zaehlerstaende by Wohnung
        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);

        // Assert the results
        assertThat(zaehlerstaende).hasSize(2);
        assertThat(zaehlerstaende).extracting(Zaehlerstand::getAblesewert).containsExactlyInAnyOrder(1234.56, 5678.90);
    }

    @Test
    public void testDeleteZaehlerstand() {
        // Create and save a Wohnung
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "91829", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Create and save a Zaehlerstand
        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        zaehlerstandRepository.save(zaehlerstand);

        // Delete the Zaehlerstand
        zaehlerstandRepository.delete(zaehlerstand);

        // Verify deletion
        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNull();
    }
}