package projektarbeit.immobilienverwaltung.service;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WohnungService {

    @Autowired
    private WohnungRepository wohnungRepository;

    // Save or update an Objekt
    public Wohnung saveOrUpdateObjekt(Wohnung objekt) {
        return wohnungRepository.save(objekt);
    }

    // Get all Objekts
    public List<Wohnung> findAllObjekts() {
        return wohnungRepository.findAll();
    }

    // Get an Objekt by id
    public Wohnung getObjektById(Long id) {
        Optional<Wohnung> objekt = wohnungRepository.findById(id);
        return objekt.orElse(null);
    }

    // Delete an Objekt
    public void deleteObjekt(Long id) {
        wohnungRepository.deleteById(id);
    }
}