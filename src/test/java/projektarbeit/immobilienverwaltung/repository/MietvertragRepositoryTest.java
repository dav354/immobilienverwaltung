package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static projektarbeit.immobilienverwaltung.model.Land.DE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MietvertragRepositoryTest {

    @Autowired
    private MietvertragRepository mietvertragRepository;

    @Autowired
    private WohnungRepository wohnungRepository;

    @Autowired
    private MieterRepository mieterRepository;

    private Wohnung wohnung;
    private Mieter mieter;

    @BeforeEach
    public void setUp() {
        wohnung = new Wohnung("Teststraße", "11", "07111", "Stuttgart", DE, 200, 1900, 2, 1, true, true, true, true, null, null);
        wohnung = wohnungRepository.save(wohnung);

        mieter = new Mieter("Mustermann", "Max", "491234567890", "max@mustermann.de", 3500.0);
        mieter = mieterRepository.save(mieter);
    }

    @Test
    public void testSaveAndFindMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag(mieter, wohnung, LocalDate.now(), LocalDate.now().plusYears(1), 1500.0, 800.0, 2);
        mietvertragRepository.save(mietvertrag);

        Mietvertrag found = mietvertragRepository.findById(mietvertrag.getMietvertrag_id()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getMieter().getName()).isEqualTo("Mustermann");
        assertThat(found.getWohnung().getStrasse()).isEqualTo("Teststraße");
    }

    @Test
    public void testFindByMieterId() {
        Mietvertrag mietvertrag1 = new Mietvertrag(mieter, wohnung, LocalDate.now(), LocalDate.now().plusYears(1), 1500.0, 800.0, 2);
        Mietvertrag mietvertrag2 = new Mietvertrag(mieter, wohnung, LocalDate.now().plusDays(1), LocalDate.now().plusYears(2), 1600.0, 850.0, 2);
        mietvertragRepository.save(mietvertrag1);
        mietvertragRepository.save(mietvertrag2);

        List<Mietvertrag> mietvertraege = mietvertragRepository.findByMieter_MieterId(mieter.getMieter_id());

        assertThat(mietvertraege).hasSize(2);
    }

    @Test
    public void testFindByWohnung() {
        Mietvertrag mietvertrag = new Mietvertrag(mieter, wohnung, LocalDate.now(), LocalDate.now().plusYears(1), 1500.0, 800.0, 2);
        mietvertragRepository.save(mietvertrag);

        Mietvertrag found = mietvertragRepository.findByWohnung(wohnung);

        assertThat(found).isNotNull();
        assertThat(found.getWohnung().getStrasse()).isEqualTo("Teststraße");
    }

    @Test
    public void testDeleteMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag(mieter, wohnung, LocalDate.now(), LocalDate.now().plusYears(1), 1500.0, 800.0, 2);
        mietvertragRepository.save(mietvertrag);

        mietvertragRepository.delete(mietvertrag);

        Mietvertrag found = mietvertragRepository.findById(mietvertrag.getMietvertrag_id()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    public void testFindByNonExistentMieterId() {
        List<Mietvertrag> mietvertraege = mietvertragRepository.findByMieter_MieterId(999L);

        assertThat(mietvertraege).isEmpty();
    }
}