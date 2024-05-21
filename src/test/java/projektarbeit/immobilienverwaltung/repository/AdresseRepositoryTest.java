package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

@DataJpaTest
public class AdresseRepositoryTest {

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindAdresse() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Adresse found = adresseRepository.findById(adr.getAdresse_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getStrasse()).isEqualTo("Teststrasse");
    }
}