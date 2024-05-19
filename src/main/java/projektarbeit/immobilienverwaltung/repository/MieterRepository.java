package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Mieter;

import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

public interface MieterRepository extends JpaRepository<Mieter, Long> {
    List<Mieter> findByWohnung(Wohnung wohnung);
}