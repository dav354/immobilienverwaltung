package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@DataJpaTest
public class WohnungRepositoryTest {

    @Autowired
    private WohnungRepository wohnungRepository;

    /**
     * Testet die Suche nach Wohnungen anhand eines Suchbegriffs.
     */
    @Test
    public void testSearchWohnungen() {
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("AndereStrasse", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> result = wohnungRepository.search("Teststraße");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStrasse()).isEqualTo("Teststraße");

        result = wohnungRepository.search("AndereStrasse");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStrasse()).isEqualTo("AndereStrasse");

        result = wohnungRepository.search("11");
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Wohnung::getHausnummer).containsOnly("11");
    }

    /**
     * Testet die Suche nach Wohnungen, wenn keine Übereinstimmungen gefunden werden.
     */
    @Test
    public void testSearchWohnungen_NoMatches() {
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("AndereStrasse", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> result = wohnungRepository.search("NonExistingStrasse");
        assertThat(result).isEmpty();
    }

    /**
     * Testet das Speichern und Finden einer Wohnung.
     */
    @Test
    public void testSaveAndFindWohnung() {
        Wohnung wohnung = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(wohnung);

        Wohnung found = wohnungRepository.findById(wohnung.getWohnung_id()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getStrasse()).isEqualTo("Teststraße");
        assertThat(found.getHausnummer()).isEqualTo("11");
    }

    /**
     * Testet das Aktualisieren einer Wohnung.
     */
    @Test
    public void testUpdateWohnung() {
        Wohnung wohnung = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(wohnung);

        Wohnung found = wohnungRepository.findById(wohnung.getWohnung_id()).orElse(null);
        assertThat(found).isNotNull();

        found.setStrasse("Neue Straße");
        wohnungRepository.save(found);

        Wohnung updated = wohnungRepository.findById(found.getWohnung_id()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getStrasse()).isEqualTo("Neue Straße");
    }

    /**
     * Testet das Löschen einer Wohnung.
     */
    @Test
    public void testDeleteWohnung() {
        Wohnung wohnung = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(wohnung);

        wohnungRepository.delete(wohnung);

        Wohnung found = wohnungRepository.findById(wohnung.getWohnung_id()).orElse(null);
        assertThat(found).isNull();
    }
}