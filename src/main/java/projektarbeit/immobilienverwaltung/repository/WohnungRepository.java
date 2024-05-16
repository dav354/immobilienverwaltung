package projektarbeit.immobilienverwaltung.repository;

import projektarbeit.immobilienverwaltung.model.Wohnung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {
}