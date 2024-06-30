package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class WohnungRepositoryTest {

    @Autowired
    private WohnungRepository wohnungRepository;

    private Wohnung testWohnung;

    @BeforeEach
    public void setUp() {
        testWohnung = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        testWohnung = wohnungRepository.save(testWohnung);
    }

    @Test
    public void testSearchWohnungen() {
        List<Wohnung> result = wohnungRepository.search("Teststraße");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStrasse()).isEqualTo("Teststraße");

        result = wohnungRepository.search("11");
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Wohnung::getHausnummer).containsOnly("11");
    }

    @Test
    public void testSearchWohnungen_NoMatches() {
        List<Wohnung> result = wohnungRepository.search("NonExistingStrasse");
        assertThat(result).isEmpty();
    }

    @Test
    public void testSaveAndFindWohnung() {
        Wohnung found = wohnungRepository.findById(testWohnung.getWohnung_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getStrasse()).isEqualTo("Teststraße");
        assertThat(found.getHausnummer()).isEqualTo("11");
    }

    @Test
    public void testUpdateWohnung() {
        Wohnung found = wohnungRepository.findById(testWohnung.getWohnung_id()).orElse(null);
        assertThat(found).isNotNull();

        found.setStrasse("Neue Straße");
        wohnungRepository.save(found);

        Wohnung updated = wohnungRepository.findById(found.getWohnung_id()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getStrasse()).isEqualTo("Neue Straße");
    }

    @Test
    public void testDeleteWohnung() {
        wohnungRepository.save(testWohnung);

        wohnungRepository.delete(testWohnung);

        Wohnung found = wohnungRepository.findById(testWohnung.getWohnung_id()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testFindNonExistentWohnung() {
        Wohnung found = wohnungRepository.findById(999L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testSearchWohnungByPartialMatch() {
        wohnungRepository.deleteAll();
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("Teststraße", "12", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> result = wohnungRepository.search("Test");
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Wohnung::getStrasse).containsOnly("Teststraße");
    }
}