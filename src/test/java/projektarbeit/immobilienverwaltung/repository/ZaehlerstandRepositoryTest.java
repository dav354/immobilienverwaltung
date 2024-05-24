package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
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

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindZaehlerstand() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung wohnung = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        zaehlerstandRepository.save(zaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getAblesedatum()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(found.getAblesewert()).isEqualTo(1234.56);
    }

    @Test
    public void testFindByWohnung() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung wohnung = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand1 = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        Zaehlerstand zaehlerstand2 = new Zaehlerstand(wohnung, LocalDate.of(2023, 2, 1), 5678.90);
        zaehlerstandRepository.save(zaehlerstand1);
        zaehlerstandRepository.save(zaehlerstand2);

        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);
        assertThat(zaehlerstaende).hasSize(2);
        assertThat(zaehlerstaende).extracting(Zaehlerstand::getAblesewert).containsExactlyInAnyOrder(1234.56, 5678.90);
    }

    @Test
    public void testDeleteZaehlerstand() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung wohnung = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2023, 1, 1), 1234.56);
        zaehlerstandRepository.save(zaehlerstand);

        zaehlerstandRepository.delete(zaehlerstand);

        Zaehlerstand found = zaehlerstandRepository.findById(zaehlerstand.getZaehlerstandId()).orElse(null);
        assertThat(found).isNull();
    }
}