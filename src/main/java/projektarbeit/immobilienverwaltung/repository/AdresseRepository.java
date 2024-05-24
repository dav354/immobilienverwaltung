package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;

import java.util.List;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    long countByPostleitzahl(Postleitzahl postleitzahl);
    @Query("select c from Adresse c " +
            "where lower(c.strasse) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.hausnummer) like lower(concat('%', :searchTerm, '%'))")
    List<Adresse> search(@Param("searchTerm") String searchTerm);
}