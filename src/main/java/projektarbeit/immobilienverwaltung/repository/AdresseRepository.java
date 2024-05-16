package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Adresse;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
}