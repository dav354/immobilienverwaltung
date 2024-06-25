package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Role;

import java.util.Optional;

/**
 * Repository für die Verwaltung der Rollen-Entitäten.
 * Erweitert JpaRepository, um CRUD-Operationen bereitzustellen.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Findet eine Rolle anhand des Rollennamens.
     *
     * @param name der Name der zu findenden Rolle.
     * @return ein Optional, das die gefundene Rolle enthält, oder leer ist, wenn keine Rolle mit dem angegebenen Namen gefunden wurde.
     */
    Optional<Role> findByName(String name);
}
