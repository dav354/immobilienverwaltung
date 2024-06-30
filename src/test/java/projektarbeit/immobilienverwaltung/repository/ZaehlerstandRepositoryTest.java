package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ZaehlerstandRepositoryTest {

    @Autowired
    private ZaehlerstandRepository zaehlerstandRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    private Wohnung testWohnung;

    private Zaehlerstand testZaehlerstand;

    @BeforeEach
    public void setUp() {
        testWohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 1, true, true, true, true, null, null);
        testWohnung = wohnungRepository.save(testWohnung);

        testZaehlerstand = new Zaehlerstand(testWohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
    }

    @Test
    public void testSaveAndFindZaehlerstand() {
        zaehlerstandRepository.save(testZaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(testZaehlerstand.getZaehlerstandId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getAblesedatum()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(found.getAblesewert()).isEqualTo(1234.56);
    }

    @Test
    public void testFindByWohnung() {
        Zaehlerstand zaehlerstand1 = new Zaehlerstand(testWohnung, LocalDate.of(2023, 1, 1), 1234.56, "test");
        Zaehlerstand zaehlerstand2 = new Zaehlerstand(testWohnung, LocalDate.of(2023, 2, 1), 5678.90, "test");
        zaehlerstandRepository.save(zaehlerstand1);
        zaehlerstandRepository.save(zaehlerstand2);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(testWohnung);

        assertThat(zaehlerstaende).hasSize(2);
        assertThat(zaehlerstaende).extracting(Zaehlerstand::getAblesewert).containsExactlyInAnyOrder(1234.56, 5678.90);
    }

    @Test
    public void testDeleteZaehlerstand() {
        zaehlerstandRepository.save(testZaehlerstand);

        zaehlerstandRepository.delete(testZaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(testZaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testUpdateZaehlerstand() {
        zaehlerstandRepository.save(testZaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(testZaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNotNull();

        found.setAblesewert(4321.65);
        zaehlerstandRepository.save(found);

        Zaehlerstand updated = zaehlerstandRepository.findById(found.getZaehlerstandId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getAblesewert()).isEqualTo(4321.65);
    }

    @Test
    public void testFindByWohnung_NoEntries() {
        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(testWohnung);

        assertThat(zaehlerstaende).isEmpty();
    }

    @Test
    public void testFindNonExistentZaehlerstand() {
        Zaehlerstand found = zaehlerstandRepository.findById(999L).orElse(null);
        assertThat(found).isNull();
    }
}