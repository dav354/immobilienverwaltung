package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository für die Verwaltung der Benutzer-Entitäten.
 * Erweitert JpaRepository, um CRUD-Operationen bereitzustellen.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Findet einen Benutzer anhand des Benutzernamens.
     *
     * @param username der Benutzername des zu findenden Benutzers.
     * @return ein Optional, das den gefundenen Benutzer enthält, oder leer ist, wenn kein Benutzer mit dem angegebenen Benutzernamen gefunden wurde.
     */
    Optional<User> findByUsername(String username);

    /**
     * Findet alle Benutzer, die von einem bestimmten Administrator erstellt wurden.
     *
     * @param admin der Administrator, der die Benutzer erstellt hat.
     * @return eine Liste der von dem angegebenen Administrator erstellten Benutzer.
     */
    List<User> findByCreatedByAdmin(User admin);
}
