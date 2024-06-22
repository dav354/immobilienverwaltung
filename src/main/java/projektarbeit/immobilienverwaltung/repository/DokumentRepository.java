package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
    List<Dokument> findByWohnung(Wohnung wohnung);
    List<Dokument> findByMieter(Mieter mieter);
}