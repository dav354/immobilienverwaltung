package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WohnungService {

    private final WohnungRepository wohnungRepository;
    private final DokumentRepository dokumentRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;

    @Autowired
    public WohnungService(WohnungRepository wohnungRepository,
                          DokumentRepository dokumentRepository,
                          MieterRepository mieterRepository,
                          ZaehlerstandRepository zaehlerstandRepository) {
        this.wohnungRepository = wohnungRepository;
        this.dokumentRepository = dokumentRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
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

    public List<Wohnung> findWohnungenWithHierarchy(String filter) {
        List<Wohnung> wohnungen = findAllWohnungen(filter);

        Map<String, List<Wohnung>> groupedWohnungen = wohnungen.stream()
                .collect(Collectors.groupingBy(wohnung -> wohnung.getStrasse() + " " + wohnung.getHausnummer()));

        List<Wohnung> wohnungsWithHierarchy = new ArrayList<>();
        groupedWohnungen.forEach((address, wohnungenForAddress) -> {
            if (wohnungenForAddress.size() > 1) {
                Wohnung addressNode = new Wohnung();
                addressNode.setStrasse(wohnungenForAddress.get(0).getStrasse());
                addressNode.setHausnummer(wohnungenForAddress.get(0).getHausnummer());
                addressNode.setPostleitzahl(wohnungenForAddress.get(0).getPostleitzahl());
                addressNode.setStadt(wohnungenForAddress.get(0).getStadt());
                addressNode.setLand(wohnungenForAddress.get(0).getLand());
                addressNode.setHeader(true);
                addressNode.setSubWohnungen(new ArrayList<>(wohnungenForAddress));
                wohnungsWithHierarchy.add(addressNode);
            } else {
                wohnungsWithHierarchy.addAll(wohnungenForAddress);
            }
        });

        return wohnungsWithHierarchy;
    }

    /**
     * Saves a Wohnung entity.
     *
     * @param wohnung the Wohnung entity to save
     * @return the saved Wohnung entity
     */
    @Transactional
    public Wohnung save(Wohnung wohnung) {
        // Save or fetch Mieter entity if it's not null
        Mieter mieter = wohnung.getMieter();
        if (mieter != null) {
            mieterRepository.save(mieter);
        }

        // Save the Wohnung entity
        return wohnungRepository.save(wohnung);
    }

    /**
     * Deletes a Wohnung entity and its associated details. Also removes references to the Wohnung from related entities.
     *
     * @param wohnung the Wohnung entity to delete
     */
    @Transactional
    public void delete(Wohnung wohnung) {
        // Delete documents associated with the Wohnung
        dokumentRepository.deleteAll(dokumentRepository.findByWohnung(wohnung));

        // Remove Mieter references to the Wohnung
        List<Mieter> mieter = mieterRepository.findAll();
        for (Mieter m : mieter) {
            if (m.getWohnung().equals(wohnung)) {
                m.setWohnung(null);
                mieterRepository.save(m); // Save the updated Mieter with null Wohnung reference
            }
        }

        // Delete Zaehlerstand references to the Wohnung
        zaehlerstandRepository.deleteAll(zaehlerstandRepository.findByWohnung(wohnung));

        // Delete the Wohnung entity
        wohnungRepository.delete(wohnung);
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
     * If the filter string is provided, it first searches for all matching Wohnungen.
     *
     * @param stringFilter The filter string to search for matching Wohnungen. If null or empty, all Wohnungen are returned.
     * @return A list of Wohnungen that match the given filter string. If no filter is provided, returns all Wohnungen.
     */
    public List<Wohnung> findAllWohnungen(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return wohnungRepository.findAll();
        } else {
            return wohnungRepository.search(stringFilter);
        }
    }
}