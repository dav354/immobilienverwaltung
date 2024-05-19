package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

public interface PostleitzahlRepository extends JpaRepository<Postleitzahl, String> {
}