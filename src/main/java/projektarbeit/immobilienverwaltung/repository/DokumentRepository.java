package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Dokument;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
}