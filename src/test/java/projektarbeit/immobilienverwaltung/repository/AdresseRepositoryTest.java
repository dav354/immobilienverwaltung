package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

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

    @Test
    public void testUpdateAdresse() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        adr.setStrasse("UpdatedStrasse");
        adresseRepository.save(adr);

        Adresse updated = adresseRepository.findById(adr.getAdresse_id()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getStrasse()).isEqualTo("UpdatedStrasse");
    }

    @Test
    public void testDeleteAdresse() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        adresseRepository.delete(adr);
        Adresse deleted = adresseRepository.findById(adr.getAdresse_id()).orElse(null);
        assertThat(deleted).isNull();
    }

    @Test
    public void testCountByPostleitzahl() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr1 = new Adresse(plz, "Teststrasse", "11");
        Adresse adr2 = new Adresse(plz, "Teststrasse", "12");
        adresseRepository.save(adr1);
        adresseRepository.save(adr2);

        long count = adresseRepository.countByPostleitzahl(plz);
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testCountByPostleitzahl_NoMatches() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        long count = adresseRepository.countByPostleitzahl(plz);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testSaveMultipleAdressen() {
        Postleitzahl plz1 = new Postleitzahl("07111", "Stuttgart", DE);
        Postleitzahl plz2 = new Postleitzahl("07112", "Stuttgart", DE);
        postleitzahlRepository.save(plz1);
        postleitzahlRepository.save(plz2);

        Adresse adr1 = new Adresse(plz1, "TeststrasseA", "11");
        Adresse adr2 = new Adresse(plz2, "TeststrasseB", "12");
        adresseRepository.save(adr1);
        adresseRepository.save(adr2);

        Adresse found1 = adresseRepository.findById(adr1.getAdresse_id()).orElse(null);
        Adresse found2 = adresseRepository.findById(adr2.getAdresse_id()).orElse(null);
        assertThat(found1).isNotNull();
        assertThat(found2).isNotNull();
    }
}