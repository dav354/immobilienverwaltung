package projektarbeit.immobilienverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projektarbeit.immobilienverwaltung.model.Wohnung;

import java.util.List;

/**
 * Repository-Schnittstelle zur Verwaltung von Wohnung-Entitäten.
 * Erweitert JpaRepository, um CRUD-Operationen und benutzerdefinierte Abfragemethoden bereitzustellen.
 */
@Repository
public interface WohnungRepository extends JpaRepository<Wohnung, Long> {

    /**
     * Sucht nach Wohnungen basierend auf dem angegebenen Suchbegriff.
     * Die Suche wird auf den Feldern 'strasse' und 'hausnummer' durchgeführt.
     *
     * @param searchTerm der Suchbegriff, um Wohnungen zu filtern
     * @return eine Liste von Wohnungen, die dem Suchbegriff entsprechen
     */
    @Query("SELECT w FROM Wohnung w " +
            "WHERE lower(w.strasse) LIKE lower(concat('%', :searchTerm, '%')) " +
            "OR lower(w.hausnummer) LIKE lower(concat('%', :searchTerm, '%'))")
    List<Wohnung> search(@Param("searchTerm") String searchTerm);
}