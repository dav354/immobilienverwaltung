package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Wohnung;

@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {
}