package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdresseService {

    @Autowired
    private AdresseRepository adresseRepository;

    // Save or update an Adresse
    public Adresse saveOrUpdateAdresse(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    // Get all Adresses
    public List<Adresse> findAllAdresses() {
        return adresseRepository.findAll();
    }

    // Get an Adresse by id
    public Adresse getAdresseById(Long id) {
        return adresseRepository.findById(id).orElse(null);
    }

    // Delete an Adresse
    public void deleteAdresse(Long id) {
        adresseRepository.deleteById(id);
    }
}