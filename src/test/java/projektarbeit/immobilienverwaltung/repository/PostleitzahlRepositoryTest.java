package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostleitzahlRepositoryTest {

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    @Test
    public void testSaveAndFindPostleitzahl() {
        Postleitzahl plz = new Postleitzahl("07111", "Stuttgart", "Deutschland");
        postleitzahlRepository.save(plz);

        Postleitzahl found = postleitzahlRepository.findById("07111").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getStadt()).isEqualTo("Stuttgart");
    }
}