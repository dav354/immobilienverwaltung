package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DokumentRepositoryTest {

    @Autowired
    private DokumentRepository dokumentRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private MieterRepository mieterRepository;

    private Wohnung wohnung;
    private Mieter mieter;

    @BeforeEach
    public void setUp() {
        wohnung = new Wohnung();
        wohnung.setStrasse("Teststraße");
        wohnung.setHausnummer("11");
        wohnung.setPostleitzahl("07111");
        wohnung.setStadt("Stuttgart");
        wohnung.setLand(DE);
        wohnung.setGesamtQuadratmeter(200);
        wohnung.setBaujahr(1900);
        wohnung.setAnzahlSchlafzimmer(2);
        wohnung.setAnzahlBaeder(2);
        wohnung.setHatBalkon(true);
        wohnung.setHatGarten(true);
        wohnung.setHatTerrasse(true);
        wohnung.setHatKlimaanlage(true);
        wohnung = wohnungRepository.save(wohnung);

        mieter = new Mieter();
        mieter.setVorname("Max");
        mieter.setName("Mustermann");
        mieter.setTelefonnummer("071178532");
        mieter.setEmail("max@mustermann.de");
        mieter.setEinkommen(3500.0);
        mieter = mieterRepository.save(mieter);
    }

    @Test
    public void testFindByWohnung() {
        Dokument dokument1 = new Dokument(wohnung, null, "Typ1", "/pfad/zu/datei1.pdf");
        Dokument dokument2 = new Dokument(wohnung, null, "Typ2", "/pfad/zu/datei2.pdf");
        dokumentRepository.save(dokument1);
        dokumentRepository.save(dokument2);

        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);

        assertThat(dokumente).hasSize(2);
        assertThat(dokumente).extracting(Dokument::getDokumententyp).containsExactlyInAnyOrder("Typ1", "Typ2");
    }

    @Test
    public void testSaveAndFindDokument() {
        Dokument dokument = new Dokument(wohnung, null, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getDokumententyp()).isEqualTo("Rechnung");
        assertThat(found.getDateipfad()).isEqualTo("/pfad/zu/datei.pdf");
    }

    @Test
    public void testFindByMieter() {
        Dokument dokument1 = new Dokument(wohnung, mieter, "Mietvertrag", "/pfad/zu/mietvertrag.pdf");
        Dokument dokument2 = new Dokument(wohnung, mieter, "Rechnung", "/pfad/zu/rechnung.pdf");
        dokumentRepository.save(dokument1);
        dokumentRepository.save(dokument2);

        List<Dokument> dokumente = dokumentRepository.findByMieter(mieter);

        assertThat(dokumente).hasSize(2);
        assertThat(dokumente).extracting(Dokument::getDokumententyp).containsExactlyInAnyOrder("Mietvertrag", "Rechnung");
    }

    @Test
    public void testDeleteDokument() {
        Dokument dokument = new Dokument(wohnung, null, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        dokumentRepository.delete(dokument);

        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testUpdateDokument() {
        Dokument dokument = new Dokument(wohnung, mieter, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        dokument.setDokumententyp("UpdatedTyp");
        dokumentRepository.save(dokument);

        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getDokumententyp()).isEqualTo("UpdatedTyp");
    }
}