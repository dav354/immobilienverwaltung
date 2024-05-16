package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ZaehlerService {

    @Autowired
    private ZaehlerstandRepository zaehlerstandRepository;

    // Save or update a Zaehlerstand
    public Zaehlerstand saveOrUpdateZaehlerstand(Zaehlerstand zaehlerstand) {
        return zaehlerstandRepository.save(zaehlerstand);
    }

    // Retrieve all Zaehlerstande
    public List<Zaehlerstand> findAllZaehlerstaende() {
        return zaehlerstandRepository.findAll();
    }

    // Retrieve a single Zaehlerstand by ID
    public Zaehlerstand getZaehlerstandById(Long id) {
        return zaehlerstandRepository.findById(id).orElse(null);
    }

    // Delete a Zaehlerstand by ID
    public void deleteZaehlerstand(Long id) {
        zaehlerstandRepository.deleteById(id);
    }
}