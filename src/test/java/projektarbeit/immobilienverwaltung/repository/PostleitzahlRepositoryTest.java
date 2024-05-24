package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.*;

@DataJpaTest
public class PostleitzahlRepositoryTest {

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindPostleitzahl() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        Postleitzahl found = postleitzahlRepository.findById("07111").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getStadt()).isEqualTo("Stuttgart");
        assertThat(found.getLand()).isEqualTo(DE);
    }

    @Test
    public void testSaveAndFindMultiplePostleitzahlen() {
        Postleitzahl plz1 = new Postleitzahl("07111", "Stuttgart", DE);
        Postleitzahl plz2 = new Postleitzahl("07112", "Berlin", DE);
        postleitzahlRepository.save(plz1);
        postleitzahlRepository.save(plz2);

        Postleitzahl found1 = postleitzahlRepository.findById("07111").orElse(null);
        Postleitzahl found2 = postleitzahlRepository.findById("07112").orElse(null);
        assertThat(found1).isNotNull();
        assertThat(found2).isNotNull();
        assertThat(found1.getStadt()).isEqualTo("Stuttgart");
        assertThat(found2.getStadt()).isEqualTo("Berlin");
    }

    @Test
    public void testDeletePostleitzahl() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        postleitzahlRepository.deleteById("07111");
        Optional<Postleitzahl> found = postleitzahlRepository.findById("07111");
        assertThat(found).isEmpty();
    }

    @Test
    public void testUpdatePostleitzahl() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", DE);
        postleitzahlRepository.save(plz);

        plz.setStadt("Munich");
        postleitzahlRepository.save(plz);

        Postleitzahl updated = postleitzahlRepository.findById("07111").orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getStadt()).isEqualTo("Munich");
    }
}