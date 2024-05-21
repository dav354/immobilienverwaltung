package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projektarbeit.immobilienverwaltung.model.Mieter;

import java.util.List;
import java.util.Optional;

public interface MieterRepository extends JpaRepository<Mieter, Long> {
    @Query("SELECT m FROM Mieter m LEFT JOIN FETCH m.wohnung")
    List<Mieter> findAllWithWohnungen();
}