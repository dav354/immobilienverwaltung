package projektarbeit.immobilienverwaltung.repository;

import projektarbeit.immobilienverwaltung.model.Eigentum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EigentumRepository extends JpaRepository<Eigentum, Long> {
    List<Eigentum> findByEigentumstyp(String eigentumstyp);
}