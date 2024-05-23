package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.PostleitzahlRepository;

import java.util.List;

@Service
public class AdresseService {

    private final AdresseRepository adresseRepository;
    private final PostleitzahlRepository postleitzahlRepository;

    /**
     * Constructs a new AdresseService with the specified repositories.
     *
     * @param adresseRepository the repository for Adresse entities
     * @param postleitzahlRepository the repository for Postleitzahl entities
     */
    @Autowired
    public AdresseService(AdresseRepository adresseRepository, PostleitzahlRepository postleitzahlRepository) {
        this.adresseRepository = adresseRepository;
        this.postleitzahlRepository = postleitzahlRepository;
    }

    /**
     * Saves the given Adresse entity. If the associated Postleitzahl is new, it is also saved.
     *
     * @param adresse the Adresse entity to save
     * @return the saved Adresse entity
     */
    @Transactional
    public Adresse save(Adresse adresse) {
        // Save Postleitzahl if it's new
        if (!postleitzahlRepository.existsById(adresse.getPostleitzahlObj().getPostleitzahl())) {
            postleitzahlRepository.save(adresse.getPostleitzahlObj());
        }
        return adresseRepository.save(adresse);
    }
}