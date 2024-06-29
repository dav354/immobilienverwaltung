package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@DataJpaTest
public class ZaehlerstandRepositoryTest {

    @Autowired
    private ZaehlerstandRepository zaehlerstandRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    /**
     * Testet das Speichern und Finden eines Zaehlerstands.
     */
    @Test
    public void testSaveAndFindZaehlerstand() {
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "9473", "Teststraße", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
        zaehlerstandRepository.save(zaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getAblesedatum()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(found.getAblesewert()).isEqualTo(1234.56);
    }

    /**
     * Testet das Finden von Zaehlerständen anhand einer Wohnung.
     */
    @Test
    public void testFindByWohnung() {
        Wohnung wohnung = new Wohnung("teststr", "11", "34321", "83423", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand1 = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
        Zaehlerstand zaehlerstand2 = new Zaehlerstand(wohnung, LocalDate.of(2023, 2, 1), 5678.90, "test");
        zaehlerstandRepository.save(zaehlerstand1);
        zaehlerstandRepository.save(zaehlerstand2);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);

        assertThat(zaehlerstaende).hasSize(2);
        assertThat(zaehlerstaende).extracting(Zaehlerstand::getAblesewert).containsExactlyInAnyOrder(1234.56, 5678.90);
    }

    /**
     * Testet das Löschen eines Zaehlerstands.
     */
    @Test
    public void testDeleteZaehlerstand() {
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "91829", "Teststraße", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
        zaehlerstandRepository.save(zaehlerstand);

        zaehlerstandRepository.delete(zaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNull();
    }

    /**
     * Testet das Aktualisieren eines Zaehlerstands.
     */
    @Test
    public void testUpdateZaehlerstand() {
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "9473", "Teststraße", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
        zaehlerstandRepository.save(zaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNotNull();

        found.setAblesewert(4321.65);
        zaehlerstandRepository.save(found);

        Zaehlerstand updated = zaehlerstandRepository.findById(found.getZaehlerstandId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getAblesewert()).isEqualTo(4321.65);
    }

    /**
     * Testet das Finden von Zaehlerständen anhand einer Wohnung, wenn keine Einträge vorhanden sind.
     */
    @Test
    public void testFindByWohnung_NoEntries() {
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "34321", "Teststraße", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);

        assertThat(zaehlerstaende).isEmpty();
    }
}