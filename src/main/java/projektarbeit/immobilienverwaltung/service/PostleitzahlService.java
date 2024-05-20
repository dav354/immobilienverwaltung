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

    @Autowired
    public PostleitzahlService(PostleitzahlRepository postleitzahlRepository, AdresseRepository adresseRepository) {
        this.postleitzahlRepository = postleitzahlRepository;
        this.adresseRepository = adresseRepository;
    }

    @Transactional
    public void deletePostleitzahlIfUnused(Postleitzahl postleitzahl) {
        long count = adresseRepository.countByPostleitzahl(postleitzahl);
        if (count == 0) {
            postleitzahlRepository.delete(postleitzahl);
        }
    }
}