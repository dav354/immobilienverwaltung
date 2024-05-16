package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Mieter;

public interface MieterRepository extends JpaRepository<Mieter, Long> {
}