package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WohnungRepositoryTest {

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindWohnung() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", "Deutschland");
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        Wohnung found = wohnungRepository.findById(w.getWohnung_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getAdresse().getStrasse()).isEqualTo("Teststrasse");
    }
}