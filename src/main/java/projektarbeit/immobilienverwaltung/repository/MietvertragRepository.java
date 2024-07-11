package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

/**
 * Repository-Schnittstelle zur Verwaltung von Mietvertrag-Entitäten.
 * Erweitert JpaRepository, um CRUD-Operationen und benutzerdefinierte Abfragemethoden bereitzustellen.
 */
public interface MietvertragRepository extends JpaRepository<Mietvertrag, Long> {

    /**
     * Findet alle Mietvertrag-Entitäten, die mit einer bestimmten Mieter-ID verknüpft sind.
     *
     * @param mieterId die ID des Mieters
     * @return eine Liste von Mietvertrag-Entitäten, die mit der angegebenen Mieter-ID verknüpft sind
     */
    List<Mietvertrag> findByMieter_MieterId(Long mieterId);

    /**
     * Findet die Mietvertrag-Entität, die mit einer bestimmten Wohnung verknüpft ist.
     *
     * @param wohnung die Wohnung-Entität
     * @return die Mietvertrag-Entität, die mit der angegebenen Wohnung verknüpft ist, oder null, falls keine gefunden wurde
     */
    Mietvertrag findByWohnung(Wohnung wohnung);
}