package projektarbeit.immobilienverwaltung.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WService {

    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final AdresseRepository adresseRepository;
    private final PostleitzahlService postleitzahlService;
    private final DokumentService dokumentService;

    public WService(WohnungRepository wohnungRepository, MieterRepository mieterRepository, ZaehlerstandRepository zaehlerstandRepository,
                    AdresseRepository adresseRepository, PostleitzahlService postleitzahlService, DokumentService dokumentService) {
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.adresseRepository = adresseRepository;
        this.postleitzahlService = postleitzahlService;
        this.dokumentService = dokumentService;
    }

    public List<Wohnung> findAllWohnungen(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return wohnungRepository.findAll();
        } else {
            // Finde alle Adressen, die den Filtertext enthalten
            List<Adresse> matchingAddresses = adresseRepository.search(stringFilter);

            // Extrahiere die IDs der passenden Adressen
            List<Long> matchingAddressIds = matchingAddresses.stream()
                    .map(Adresse::getAdresse_id)
                    .collect(Collectors.toList());

            // Finde alle Wohnungen, die eine dieser Adressen haben
            return wohnungRepository.findByAdresseIds(matchingAddressIds);
        }
    }

    public long countWohnung() {
        return wohnungRepository.count();
    }

    @Transactional
    public Wohnung saveWohnungen(Wohnung wohnung) {
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
    public void deleteWohnungen(Wohnung wohnung) {
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

    public List<Mieter> findAllMieter() {
        return mieterRepository.findAll();
    }
}