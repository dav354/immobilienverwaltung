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

    /**
     * Constructs a new WohnungService with the specified repositories and services.
     *
     * @param wohnungRepository the repository for Wohnung entities
     * @param dokumentRepository the repository for Dokument entities
     * @param mieterRepository the repository for Mieter entities
     * @param zaehlerstandRepository the repository for Zaehlerstand entities
     * @param adresseRepository the repository for Adresse entities
     * @param postleitzahlService the service for managing Postleitzahl entities
     * @param dokumentService the service for managing Dokument entities
     */
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

    /**
     * Retrieves all Wohnung entities.
     *
     * @return a list of all Wohnung entities
     */
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    /**
     * Saves a Wohnung entity. If the associated Adresse does not exist, it is created.
     *
     * @param wohnung the Wohnung entity to save
     * @return the saved Wohnung entity
     */
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

    /**
     * Deletes a Wohnung entity and its associated Adresse. Also removes references to the Wohnung from related entities.
     *
     * @param wohnung the Wohnung entity to delete
     */
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

        // Delete Zaehlerstand references to the Wohnung
        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);
        for (Zaehlerstand zaehlerstand : zaehlerstaende) {
            zaehlerstandRepository.delete(zaehlerstand); // Delete the Zaehlerstand
        }

        // Delete the Wohnung and its associated Adresse
        Adresse adresse = wohnung.getAdresse();
        wohnungRepository.delete(wohnung);
        adresseRepository.delete(adresse);

        // Delete the Postleitzahl if it is no longer used
        postleitzahlService.deletePostleitzahlIfUnused(adresse.getPostleitzahlObj());
    }

    /**
     * Retrieves the Dokument entities associated with a given Wohnung.
     *
     * @param wohnung the Wohnung entity for which to find Dokumente
     * @return a list of Dokument entities associated with the given Wohnung
     */
    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }
}