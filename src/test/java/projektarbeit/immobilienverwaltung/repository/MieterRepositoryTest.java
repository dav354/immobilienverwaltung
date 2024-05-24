package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
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

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindMieter() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        Mieter mieter = new Mieter("Mustermann", "Max", "01234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        mieterRepository.save(mieter);

        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mustermann");
        assertThat(found.getMietbeginn()).isEqualTo(LocalDate.of(2022, 1, 1));
        assertThat(found.getMietende()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    public void testFindAllWithWohnungen() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        Mieter mieter = new Mieter("Mustermann", "Max", "01234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        mieter.getWohnung().add(w);
        mieterRepository.save(mieter);

        List<Mieter> mieters = mieterRepository.findAllWithWohnungen();
        assertThat(mieters).isNotEmpty();
        assertThat(mieters.get(0).getWohnung()).contains(w);
    }

    @Test
    public void testSearchMieters() {
        Mieter mieter1 = new Mieter("Mustermann", "Max", "01234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        Mieter mieter2 = new Mieter("Musterfrau", "Anna", "09876543210", 3500, LocalDate.of(2023, 1, 1), LocalDate.of(2025, 1, 1), 700, 2);
        mieterRepository.save(mieter1);
        mieterRepository.save(mieter2);

        List<Mieter> foundMieters = mieterRepository.search("Anna");
        assertThat(foundMieters).hasSize(1);
        assertThat(foundMieters.get(0).getName()).isEqualTo("Musterfrau");
    }

    @Test
    public void testSaveAndFindMultipleMieters() {
        Mieter mieter1 = new Mieter("Mustermann", "Max", "01234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        Mieter mieter2 = new Mieter("Musterfrau", "Anna", "09876543210", 3500, LocalDate.of(2023, 1, 1), LocalDate.of(2025, 1, 1), 700, 2);
        mieterRepository.save(mieter1);
        mieterRepository.save(mieter2);

        List<Mieter> foundMieters = mieterRepository.findAll();
        assertThat(foundMieters).hasSize(2);
    }

    @Test
    public void testUpdateMieter() {
        Mieter mieter = new Mieter("Mustermann", "Max", "01234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        mieterRepository.save(mieter);

        mieter.setName("Mustermax");
        mieterRepository.save(mieter);

        Mieter updated = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Mustermax");
    }
}