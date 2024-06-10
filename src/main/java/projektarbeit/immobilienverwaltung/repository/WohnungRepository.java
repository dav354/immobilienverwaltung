package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

/**
 * Repository interface for managing Wohnung entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {

    /**
     * Searches for Wohnungen based on the given search term.
     * The search is performed on the 'strasse' and 'hausnummer' fields.
     *
     * @param searchTerm the search term to filter Wohnungen
     * @return a list of Wohnungen that match the search term
     */
    @Query("SELECT w FROM Wohnung w " +
            "WHERE lower(w.strasse) LIKE lower(concat('%', :searchTerm, '%')) " +
            "OR lower(w.hausnummer) LIKE lower(concat('%', :searchTerm, '%'))")
    List<Wohnung> search(@Param("searchTerm") String searchTerm);
}