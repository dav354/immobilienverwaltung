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

    @Autowired
    public AdresseService(AdresseRepository adresseRepository, PostleitzahlRepository postleitzahlRepository) {
        this.adresseRepository = adresseRepository;
        this.postleitzahlRepository = postleitzahlRepository;
    }

    @Transactional
    public Adresse save(Adresse adresse) {
        // Save Postleitzahl if it's new
        if (!postleitzahlRepository.existsById(adresse.getPostleitzahlObj().getPostleitzahl())) {
            postleitzahlRepository.save(adresse.getPostleitzahlObj());
        }
        return adresseRepository.save(adresse);
    }
}