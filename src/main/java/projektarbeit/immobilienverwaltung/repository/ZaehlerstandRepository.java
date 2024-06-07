package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface ZaehlerstandRepository extends JpaRepository<Zaehlerstand, Long> {
    List<Zaehlerstand> findByWohnung(Wohnung wohnung);
}