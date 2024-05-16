package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

public interface ZaehlerstandRepository extends JpaRepository<Zaehlerstand, Long> {
}