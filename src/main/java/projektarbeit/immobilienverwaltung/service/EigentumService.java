package projektarbeit.immobilienverwaltung.service;

import projektarbeit.immobilienverwaltung.model.Eigentum;
import projektarbeit.immobilienverwaltung.repository.EigentumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EigentumService {

    @Autowired
    private EigentumRepository eigentumRepository;

    // Methode zum Speichern und Aktualisieren von Eigentum
    public Eigentum saveOrUpdateEigentum(Eigentum eigentum) {
        return eigentumRepository.save(eigentum);
    }

    // Methode zum Abrufen aller Eigentum-Objekte
    public List<Eigentum> findAllEigentum() {
        return eigentumRepository.findAll();
    }

    // Methode zum Abrufen eines Eigentum-Objekts nach ID
    public Eigentum findEigentumById(Long id) {
        Optional<Eigentum> result = eigentumRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        return null; // oder throw new EntityNotFoundException("Eigentum nicht gefunden");
    }

    // Methode zum Löschen eines Eigentum-Objekts nach ID
    public void deleteEigentum(Long id) {
        eigentumRepository.deleteById(id);
    }

    // Optional: Methode zum Abrufen von Eigentum nach bestimmten Kriterien
    public List<Eigentum> findByEigentumstyp(String eigentumstyp) {
        // Beispiel für eine benutzerdefinierte Methode im Repository
        return eigentumRepository.findByEigentumstyp(eigentumstyp);
    }
}
