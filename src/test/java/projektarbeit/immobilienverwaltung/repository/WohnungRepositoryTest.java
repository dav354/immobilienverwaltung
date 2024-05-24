package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

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
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        Wohnung found = wohnungRepository.findById(w.getWohnung_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getAdresse().getStrasse()).isEqualTo("Teststrasse");
    }

    @Test
    public void testUpdateWohnung() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        w.setGesamtQuadratmeter(250);
        wohnungRepository.save(w);

        Wohnung updated = wohnungRepository.findById(w.getWohnung_id()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getGesamtQuadratmeter()).isEqualTo(250);
    }

    @Test
    public void testDeleteWohnung() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        wohnungRepository.delete(w);

        Wohnung deleted = wohnungRepository.findById(w.getWohnung_id()).orElse(null);
        assertThat(deleted).isNull();
    }

    @Test
    public void testFindByMieterIsNull() {
        Postleitzahl plz1 = new Postleitzahl("07111", "Stuttgart", DE);
        Postleitzahl plz2 = new Postleitzahl("07112", "Stuttgart", DE);
        postleitzahlRepository.save(plz1);
        postleitzahlRepository.save(plz2);

        Adresse adr1 = new Adresse(plz1, "Teststrasse", "11");
        Adresse adr2 = new Adresse(plz2, "AndereStrasse", "12");
        adresseRepository.save(adr1);
        adresseRepository.save(adr2);

        Wohnung w1 = new Wohnung(adr1, 200, 1900, 2, 2, true, true, true, true);
        Wohnung w2 = new Wohnung(adr2, 150, 1950, 1, 1, false, false, true, false);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> availableWohnungen = wohnungRepository.findByMieterIsNull();
        assertThat(availableWohnungen).hasSize(2);
        assertThat(availableWohnungen).extracting(Wohnung::getGesamtQuadratmeter).containsExactlyInAnyOrder(200, 150);
    }

    @Test
    public void testFindAllWohnungen() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr1 = new Adresse(plz, "Teststrasse", "11");
        Adresse adr2 = new Adresse(plz, "AndereStrasse", "12");
        adresseRepository.save(adr1);
        adresseRepository.save(adr2);

        Wohnung w1 = new Wohnung(adr1, 200, 1900, 2, 2, true, true, true, true);
        Wohnung w2 = new Wohnung(adr2, 150, 1950, 1, 1, false, false, true, false);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> wohnungen = wohnungRepository.findAll();
        assertThat(wohnungen).hasSize(2);
        assertThat(wohnungen).extracting(Wohnung::getAdresse).containsExactlyInAnyOrder(adr1, adr2);
    }
}