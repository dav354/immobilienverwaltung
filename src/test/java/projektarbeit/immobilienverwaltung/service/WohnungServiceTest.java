package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.PostleitzahlRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WohnungServiceTest {

    @Autowired
    private WohnungService wohnungService;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    @Transactional
    public void testFindAllWohnungen() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", "Deutschland");
        postleitzahlRepository.save(plz);

        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        Wohnung w = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(w);

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen();
        assertThat(wohnungen).isNotEmpty();
        assertThat(wohnungen.get(0).getAdresse().getStrasse()).isEqualTo("Teststrasse");
    }
}