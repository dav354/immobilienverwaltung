package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {

    @Query("SELECT w FROM Wohnung w WHERE w.adresse.adresse_id IN :adresseIds")
    List<Wohnung> findByAdresseIds(@Param("adresseIds") List<Long> adresseIds);

    List<Wohnung> findByMieterIsNull();
}