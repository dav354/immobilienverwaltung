package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface ZaehlerstandRepository extends JpaRepository<Zaehlerstand, Long> {

    /**
     * Finds all Zaehlerstand entries associated with the given Wohnung.
     *
     * @param wohnung the Wohnung entity for which to find Zaehlerstand entries.
     * @return a list of Zaehlerstand entries associated with the given Wohnung.
     */
    List<Zaehlerstand> findByWohnung(Wohnung wohnung);
}