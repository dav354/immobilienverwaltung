package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@DataJpaTest
public class DokumentRepositoryTest {

    @Autowired
    private DokumentRepository dokumentRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testFindByWohnung() {
        // Create and save a Postleitzahl
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        // Create and save an Adresse
        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        // Create and save a Wohnung
        Wohnung wohnung = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(wohnung);

        // Create and save Dokumente
        Dokument dokument1 = new Dokument(wohnung, null, "Typ1", "/pfad/zu/datei1.pdf");
        Dokument dokument2 = new Dokument(wohnung, null, "Typ2", "/pfad/zu/datei2.pdf");
        dokumentRepository.save(dokument1);
        dokumentRepository.save(dokument2);

        // Retrieve Dokumente by Wohnung
        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);

        // Assert the results
        assertThat(dokumente).hasSize(2);
        assertThat(dokumente).extracting(Dokument::getDokumententyp).containsExactlyInAnyOrder("Typ1", "Typ2");
    }

    @Test
    public void testSaveAndFindDokument() {
        // Create and save a Postleitzahl
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        // Create and save an Adresse
        Adresse adr = new Adresse(plz, "Teststrasse", "11");
        adresseRepository.save(adr);

        // Create and save a Wohnung
        Wohnung wohnung = new Wohnung(adr, 200, 1900, 2, 2, true, true, true, true);
        wohnungRepository.save(wohnung);

        // Create and save a Dokument
        Dokument dokument = new Dokument(wohnung, null, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        // Retrieve the Dokument
        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);

        // Assert the results
        assertThat(found).isNotNull();
        assertThat(found.getDokumententyp()).isEqualTo("Rechnung");
        assertThat(found.getDateipfad()).isEqualTo("/pfad/zu/datei.pdf");
    }
}