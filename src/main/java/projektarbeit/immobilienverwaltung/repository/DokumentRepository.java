package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
    /**
     * Findet alle Dokumente, die mit einer bestimmten Wohnung verknüpft sind.
     *
     * @param wohnung Die Wohnung, für die die Dokumente abgerufen werden sollen.
     * @return Eine Liste von Dokumenten, die mit der angegebenen Wohnung verknüpft sind.
     */
    @Query("SELECT d FROM Dokument d JOIN FETCH d.wohnung WHERE d.wohnung = :wohnung")
    List<Dokument> findByWohnung(Wohnung wohnung);

    /**
     * Findet alle Dokumente, die mit einem bestimmten Mieter verknüpft sind.
     *
     * @param mieter Der Mieter, für den die Dokumente abgerufen werden sollen.
     * @return Eine Liste von Dokumenten, die mit dem angegebenen Mieter verknüpft sind.
     */
    @Query("SELECT d FROM Dokument d JOIN FETCH d.mieter WHERE d.mieter = :mieter")
    List<Dokument> findByMieter(Mieter mieter);
}