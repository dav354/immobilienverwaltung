package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projektarbeit.immobilienverwaltung.model.Mieter;

import java.util.List;

public interface MieterRepository extends JpaRepository<Mieter, Long> {
    @Query("SELECT m FROM Mieter m LEFT JOIN FETCH m.wohnung")
    List<Mieter> findAllWithWohnungen();

    //Query um die Mieter zu suchen mit dem Suchbegriff
    @Query("select c from Mieter c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.vorname) like lower(concat('%', :searchTerm, '%'))")
    List<Mieter> search(@Param("searchTerm") String searchTerm);
}