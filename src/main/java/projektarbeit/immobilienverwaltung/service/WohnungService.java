package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WohnungService {

    private final WohnungRepository wohnungRepository;
    private final DokumentRepository dokumentRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final AdresseRepository adresseRepository;
    private final PostleitzahlService postleitzahlService;
    private final DokumentService dokumentService;
    private final PostleitzahlRepository postleitzahlRepository;

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
     * @param postleitzahlRepository the repository for Postleitzahl entities
     */
    @Autowired
    public WohnungService(WohnungRepository wohnungRepository,
                          DokumentRepository dokumentRepository,
                          MieterRepository mieterRepository,
                          ZaehlerstandRepository zaehlerstandRepository,
                          AdresseRepository adresseRepository,
                          PostleitzahlService postleitzahlService,
                          DokumentService dokumentService,
                          PostleitzahlRepository postleitzahlRepository) {
        this.wohnungRepository = wohnungRepository;
        this.dokumentRepository = dokumentRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.adresseRepository = adresseRepository;
        this.postleitzahlService = postleitzahlService;
        this.dokumentService = dokumentService;
        this.postleitzahlRepository = postleitzahlRepository;
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
     * Retrieves all Mieter entities.
     *
     * @return a list of all Mieter entities
     */
    public List<Mieter> findAllMieter() {
        return mieterRepository.findAll();
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
        Postleitzahl postleitzahl = adresse.getPostleitzahlObj();

        // Save or fetch Postleitzahl
        if (!postleitzahlRepository.existsById(postleitzahl.getPostleitzahl())) {
            postleitzahl = postleitzahlRepository.save(postleitzahl);
        } else {
            postleitzahl = postleitzahlRepository.findById(postleitzahl.getPostleitzahl())
                    .orElseThrow(() -> new RuntimeException("Postleitzahl nicht gefunden"));
        }

        // Save or fetch Adresse
        if (adresse.getAdresse_id() == null || !adresseRepository.existsById(adresse.getAdresse_id())) {
            adresse.setPostleitzahlObj(postleitzahl);
            adresse = adresseRepository.save(adresse);
        } else {
            adresse = adresseRepository.findById(adresse.getAdresse_id())
                    .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));
            adresse.setPostleitzahlObj(postleitzahl);
        }

        // Set the address to the Wohnung
        wohnung.setAdresse(adresse);

        // Save the Mieter entity if it's not null
        Mieter mieter = wohnung.getMieter();
        if (mieter != null) {
            mieterRepository.save(mieter);
        }

        // Save the Wohnung entity
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

    /**
     * Service method to find all Wohnungen (apartments) based on a given string filter.
     * If the filter string is null or empty, it returns all Wohnungen.
     * If the filter string is provided, it first searches for all matching Adressen (addresses),
     * extracts their IDs, and then finds all Wohnungen associated with those address IDs.
     *
     * @param stringFilter The filter string to search for matching Adressen. If null or empty, all Wohnungen are returned.
     * @return A list of Wohnungen that match the given filter string. If no filter is provided, returns all Wohnungen.
     */
    public List<Wohnung> findAllWohnungen(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return wohnungRepository.findAll();
        } else {
            // Find all addresses that contain the filter text
            List<Adresse> matchingAddresses = adresseRepository.search(stringFilter);

            // Extract the IDs of the matching addresses
            List<Long> matchingAddressIds = matchingAddresses.stream()
                    .map(Adresse::getAdresse_id)
                    .collect(Collectors.toList());

            // Find all Wohnungen that have one of these addresses
            return wohnungRepository.findByAdresseIds(matchingAddressIds);
        }
    }
}