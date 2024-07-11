package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;

import java.util.List;

/**
 * Repository-Schnittstelle zur Verwaltung von Zählerstand-Entitäten.
 * Erweitert JpaRepository, um CRUD-Operationen und benutzerdefinierte Abfragemethoden bereitzustellen.
 */
@SuppressWarnings("SpellCheckingInspection")
public interface ZaehlerstandRepository extends JpaRepository<Zaehlerstand, Long> {

    /**
     * Findet alle Zaehlerstand-Einträge, die mit der angegebenen Wohnung verknüpft sind.
     *
     * @param wohnung die Wohnung-Entität, für die die Zaehlerstand-Einträge gefunden werden sollen.
     * @return eine Liste von Zaehlerstand-Einträgen, die mit der angegebenen Wohnung verknüpft sind.
     */
    List<Zaehlerstand> findByWohnung(Wohnung wohnung);
}