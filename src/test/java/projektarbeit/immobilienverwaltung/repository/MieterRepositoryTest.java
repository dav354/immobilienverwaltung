package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

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

        Mieter mieter = new Mieter("Mustermann", "Max", "+491234567890", 3000, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1), 500, 1);
        mieterRepository.save(mieter);

        Mieter found = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mustermann");
        assertThat(found.getMietbeginn()).isEqualTo(LocalDate.of(2022, 1, 1));
        assertThat(found.getMietende()).isEqualTo(LocalDate.of(2024, 1, 1));
    }
}