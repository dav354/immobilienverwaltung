package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Adresse;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WohnungService {

    private final WohnungRepository wohnungRepository;
    private final DokumentRepository dokumentRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final AdresseRepository adresseRepository;
    private final PostleitzahlService postleitzahlService;
    private final DokumentService dokumentService;

    @Autowired
    public WohnungService(WohnungRepository wohnungRepository,
                          DokumentRepository dokumentRepository,
                          MieterRepository mieterRepository,
                          ZaehlerstandRepository zaehlerstandRepository,
                          AdresseRepository adresseRepository,
                          PostleitzahlService postleitzahlService,
                          DokumentService dokumentService) {
        this.wohnungRepository = wohnungRepository;
        this.dokumentRepository = dokumentRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.adresseRepository = adresseRepository;
        this.postleitzahlService = postleitzahlService;
        this.dokumentService = dokumentService;
    }

    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    @Transactional
    public Wohnung save(Wohnung wohnung) {
        Adresse adresse = wohnung.getAdresse();

        // Überprüfen, ob die Adresse bereits existiert
        if (adresse.getAdresse_id() == null || !adresseRepository.existsById(adresse.getAdresse_id())) {
            adresse = adresseRepository.save(adresse);
        } else {
            adresse = adresseRepository.findById(adresse.getAdresse_id()).orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));
        }

        // Setze die Adresse der Wohnung
        wohnung.setAdresse(adresse);

        return wohnungRepository.save(wohnung);
    }

    @Transactional
    public void delete(Wohnung wohnung) {
        // Delete documents associated with the Wohnung
        dokumentService.deleteDokumenteByWohnung(wohnung);

        // Remove Mieter references to the Wohnung
        List<Mieter> mieter = mieterRepository.findAllWithWohnungen();
        for (Mieter m : mieter) {
            m.setWohnung(new ArrayList<>());
            mieterRepository.save(m); // Save the updated Mieter with null Wohnung reference
        }

        // Remove Zaehlerstand references to the Wohnung
        List<Zaehlerstand> zaehlerstaend = zaehlerstandRepository.findByWohnung(wohnung);
        for (Zaehlerstand z : zaehlerstaend) {
            z.setWohnung(null);
            zaehlerstandRepository.save(z); // Save the updated Zaehlerstand with null Wohnung reference
        }

        // Delete the Wohnung and its associated Adresse
        Adresse adresse = wohnung.getAdresse();
        wohnungRepository.delete(wohnung);
        adresseRepository.delete(adresse);

        // Delete the Postleitzahl if it is no longer used
        postleitzahlService.deletePostleitzahlIfUnused(adresse.getPostleitzahlObj());
    }

    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }
}