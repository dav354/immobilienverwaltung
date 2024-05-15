package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.EigentumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EigentumService {

    @Autowired
    private EigentumRepository eigentumRepository;

    // Methode zum Speichern und Aktualisieren von Eigentum
    public Wohnung saveOrUpdateEigentum(Wohnung wohnung) {
        return eigentumRepository.save(wohnung);
    }

    // Methode zum Abrufen aller Eigentum-Objekte
    public List<Wohnung> findAllEigentum() {
        return eigentumRepository.findAll();
    }

    // Methode zum Abrufen eines Eigentum-Objekts nach ID
    public Wohnung findEigentumById(Long id) {
        Optional<Wohnung> result = eigentumRepository.findById(id);
        return result.orElse(null);
    }

    // Methode zum Löschen eines Eigentum-Objekts nach ID
    public void deleteEigentum(Long id) {
        eigentumRepository.deleteById(id);
    }
}