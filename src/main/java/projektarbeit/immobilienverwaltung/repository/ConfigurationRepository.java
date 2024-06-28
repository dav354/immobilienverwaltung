package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Configuration;

/**
 * Repository-Schnittstelle zur Verwaltung von Configuration-Entitäten.
 * Diese Schnittstelle erweitert JpaRepository und bietet CRUD-Operationen für die Configuration-Entität.
 */
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}