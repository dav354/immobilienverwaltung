package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    long countByPostleitzahl(Postleitzahl postleitzahl);
}