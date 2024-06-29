package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
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

    /**
     * Testet das Finden von Dokumenten anhand einer Wohnung.
     */
    @Test
    public void testFindByWohnung() {
        // Erstellen und Speichern einer Wohnung
        Wohnung wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Erstellen und Speichern von Dokumenten
        Dokument dokument1 = new Dokument(wohnung, null, "Typ1", "/pfad/zu/datei1.pdf");
        Dokument dokument2 = new Dokument(wohnung, null, "Typ2", "/pfad/zu/datei2.pdf");
        dokumentRepository.save(dokument1);
        dokumentRepository.save(dokument2);

        // Abrufen der Dokumente anhand der Wohnung
        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);

        // Überprüfen der Ergebnisse
        assertThat(dokumente).hasSize(2);
        assertThat(dokumente).extracting(Dokument::getDokumententyp).containsExactlyInAnyOrder("Typ1", "Typ2");
    }

    /**
     * Testet das Speichern und Finden eines Dokuments.
     */
    @Test
    public void testSaveAndFindDokument() {
        // Erstellen und Speichern einer Wohnung
        Wohnung wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Erstellen und Speichern eines Dokuments
        Dokument dokument = new Dokument(wohnung, null, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        // Abrufen des Dokuments
        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);

        // Überprüfen der Ergebnisse
        assertThat(found).isNotNull();
        assertThat(found.getDokumententyp()).isEqualTo("Rechnung");
        assertThat(found.getDateipfad()).isEqualTo("/pfad/zu/datei.pdf");
    }

    /**
     * Testet das Finden von Dokumenten anhand eines Mieters.
     */
    @Test
    public void testFindByMieter() {
        // Erstellen und Speichern einer Wohnung und eines Mieters
        Wohnung wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max@mustermann.de", 3500);

        // Erstellen und Speichern von Dokumenten für den Mieter
        Dokument dokument1 = new Dokument(wohnung, mieter, "Mietvertrag", "/pfad/zu/mietvertrag.pdf");
        Dokument dokument2 = new Dokument(wohnung, mieter, "Rechnung", "/pfad/zu/rechnung.pdf");
        dokumentRepository.save(dokument1);
        dokumentRepository.save(dokument2);

        // Abrufen der Dokumente anhand des Mieters
        List<Dokument> dokumente = dokumentRepository.findByMieter(mieter);

        // Überprüfen der Ergebnisse
        assertThat(dokumente).hasSize(2);
        assertThat(dokumente).extracting(Dokument::getDokumententyp).containsExactlyInAnyOrder("Mietvertrag", "Rechnung");
    }

    /**
     * Testet das Löschen eines Dokuments.
     */
    @Test
    public void testDeleteDokument() {
        // Erstellen und Speichern einer Wohnung
        Wohnung wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnungRepository.save(wohnung);

        // Erstellen und Speichern eines Dokuments
        Dokument dokument = new Dokument(wohnung, null, "Rechnung", "/pfad/zu/datei.pdf");
        dokumentRepository.save(dokument);

        // Löschen des Dokuments
        dokumentRepository.delete(dokument);

        // Überprüfen, ob das Dokument gelöscht wurde
        Dokument found = dokumentRepository.findById(dokument.getDokument_id()).orElse(null);
        assertThat(found).isNull();
    }
}