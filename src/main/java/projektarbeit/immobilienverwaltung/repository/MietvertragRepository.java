package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

/**
 * Repository interface for managing Mietvertrag entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface MietvertragRepository extends JpaRepository<Mietvertrag, Long> {

    /**
     * Finds all Mietvertrag entities associated with a given Mieter ID.
     *
     * @param mieterId the ID of the Mieter
     * @return a list of Mietvertrag entities associated with the given Mieter ID
     */
    List<Mietvertrag> findByMieter_MieterId(Long mieterId);

    /**
     * Finds the Mietvertrag entity associated with a given Wohnung.
     *
     * @param wohnung the Wohnung entity
     * @return the Mietvertrag entity associated with the given Wohnung, or null if none found
     */
    Mietvertrag findByWohnung(Wohnung wohnung);
}