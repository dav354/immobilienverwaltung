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

    public Wohnung save(Wohnung wohnung) {
        return wohnungRepository.save(wohnung);
    }

    @Transactional
    public void delete(Wohnung wohnung) {
        dokumentService.deleteDokumenteByWohnung(wohnung);

        List<Mieter> mieter = mieterRepository.findByWohnung(wohnung);
        for (Mieter m : mieter) {
            m.setWohnung(null);
            mieterRepository.save(m); // Save the updated Mieter with null Wohnung reference
        }

        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);
        for (Zaehlerstand z : zaehlerstaende) {
            z.setWohnung(null);
            zaehlerstandRepository.save(z); // Save the updated Zaehlerstand with null Wohnung reference
        }

        Adresse adresse = wohnung.getAdresse();
        wohnungRepository.delete(wohnung);
        adresseRepository.delete(adresse);
        postleitzahlService.deletePostleitzahlIfUnused(adresse.getPostleitzahlObj());
    }

    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }
}