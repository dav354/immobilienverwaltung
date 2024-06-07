package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {

    List<Wohnung> findByMieterIsNull();

    @Query("SELECT w FROM Wohnung w " +
            "WHERE lower(w.strasse) LIKE lower(concat('%', :searchTerm, '%')) " +
            "OR lower(w.hausnummer) LIKE lower(concat('%', :searchTerm, '%'))")
    List<Wohnung> search(@Param("searchTerm") String searchTerm);
}