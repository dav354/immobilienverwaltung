package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projektarbeit.immobilienverwaltung.model.Mieter;

import java.util.List;

public interface MieterRepository extends JpaRepository<Mieter, Long> {

    /**
     * Sucht Mieter mit einem bestimmten Suchbegriff.
     * Die Suche wird sowohl im Namen als auch im Vornamen durchgeführt.
     *
     * @param searchTerm Der Suchbegriff, nach dem gesucht werden soll.
     * @return Eine Liste von Mietern, deren Name oder Vorname den Suchbegriff enthält.
     */
    @Query("select c from Mieter c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.vorname) like lower(concat('%', :searchTerm, '%'))")
    List<Mieter> search(@Param("searchTerm") String searchTerm);

    /**
     * Prüft, ob ein Mieter mit der angegebenen E-Mail existiert.
     *
     * @param email die zu prüfende E-Mail.
     * @return true, wenn ein Mieter mit der E-Mail existiert, sonst false.
     */
    boolean existsByEmail(String email);
}