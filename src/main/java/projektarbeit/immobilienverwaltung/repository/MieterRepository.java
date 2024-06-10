package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projektarbeit.immobilienverwaltung.model.Mieter;

import java.util.List;

public interface MieterRepository extends JpaRepository<Mieter, Long> {
    //Query um die Mieter zu suchen mit dem Suchbegriff
    @Query("select c from Mieter c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.vorname) like lower(concat('%', :searchTerm, '%'))")
    List<Mieter> search(@Param("searchTerm") String searchTerm);

    /**
     * Checks if a Mieter with the given email exists.
     *
     * @param email the email to check.
     * @return true if a Mieter with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}