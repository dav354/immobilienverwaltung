package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projektarbeit.immobilienverwaltung.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
