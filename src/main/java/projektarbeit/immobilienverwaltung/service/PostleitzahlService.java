package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.PostleitzahlRepository;

@Service
public class PostleitzahlService {

    private final PostleitzahlRepository postleitzahlRepository;
    private final AdresseRepository adresseRepository;

    /**
     * Constructs a new PostleitzahlService with the specified repositories.
     *
     * @param postleitzahlRepository the repository for Postleitzahl entities
     * @param adresseRepository the repository for Adresse entities
     */
    @Autowired
    public PostleitzahlService(PostleitzahlRepository postleitzahlRepository, AdresseRepository adresseRepository) {
        this.postleitzahlRepository = postleitzahlRepository;
        this.adresseRepository = adresseRepository;
    }

    /**
     * Deletes the given Postleitzahl entity if it is not used by any Adresse entities.
     *
     * @param postleitzahl the Postleitzahl entity to delete if unused
     */
    @Transactional
    public void deletePostleitzahlIfUnused(Postleitzahl postleitzahl) {
        long count = adresseRepository.countByPostleitzahl(postleitzahl);
        if (count == 0) {
            postleitzahlRepository.delete(postleitzahl);
        }
    }

    /**
     * Saves the given Postleitzahl entity.
     *
     * @param postleitzahl the Postleitzahl entity to save
     * @return the saved Postleitzahl entity
     */
    @Transactional
    public Postleitzahl save(Postleitzahl postleitzahl) {
        return postleitzahlRepository.save(postleitzahl);
    }
}