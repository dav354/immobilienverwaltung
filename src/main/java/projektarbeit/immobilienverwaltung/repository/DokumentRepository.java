package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
    @Query("SELECT d FROM Dokument d JOIN FETCH d.wohnung WHERE d.wohnung = :wohnung")
    List<Dokument> findByWohnung(Wohnung wohnung);

    @Query("SELECT d FROM Dokument d JOIN FETCH d.mieter WHERE d.mieter = :mieter")
    List<Dokument> findByMieter(Mieter mieter);
}