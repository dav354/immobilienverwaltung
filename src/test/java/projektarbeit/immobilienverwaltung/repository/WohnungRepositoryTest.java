package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

@DataJpaTest
public class WohnungRepositoryTest {

    @Autowired
    private WohnungRepository wohnungRepository;

    @Test
    public void testFindByMieterIsNull() {
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);

        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> availableWohnungen = wohnungRepository.findByMieterIsNull();
        assertThat(availableWohnungen).hasSize(2);
        assertThat(availableWohnungen).extracting(Wohnung::getGesamtQuadratmeter).containsExactlyInAnyOrder(200, 150);
    }

    @Test
    public void testSearchWohnungen() {
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("AndereStrasse", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);

        List<Wohnung> result = wohnungRepository.search("Teststrasse");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStrasse()).isEqualTo("Teststrasse");

        result = wohnungRepository.search("AndereStrasse");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStrasse()).isEqualTo("AndereStrasse");

        result = wohnungRepository.search("11");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getHausnummer()).isEqualTo("11");
    }

    @Test
    public void testSearchWohnungen_NoMatches() {
        Wohnung w1 = new Wohnung("Teststraße", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Wohnung w2 = new Wohnung("AndereStrasse", "11", "83248", "Teststadt", DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        wohnungRepository.save(w1);
        wohnungRepository.save(w2);


        List<Wohnung> result = wohnungRepository.search("NonExistingStrasse");
        assertThat(result).isEmpty();
    }
}