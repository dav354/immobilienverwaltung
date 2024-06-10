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

@SuppressWarnings("SpellCheckingInspection")
@Service
public class WohnungService {

    private final WohnungRepository wohnungRepository;
    private final DokumentRepository dokumentRepository;
    private final MieterRepository mieterRepository;
    private final  MietvertragRepository mietvertragRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;

    @Autowired
    public WohnungService(WohnungRepository wohnungRepository,
                          DokumentRepository dokumentRepository,
                          MieterRepository mieterRepository,
                          MietvertragRepository mietvertragRepository,
                          ZaehlerstandRepository zaehlerstandRepository) {
        this.wohnungRepository = wohnungRepository;
        this.dokumentRepository = dokumentRepository;
        this.mieterRepository = mieterRepository;
        this.mietvertragRepository = mietvertragRepository;
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

    /**
     * Finds Wohnungen (apartments) and groups them into a hierarchical structure based on their address.
     * If there are multiple Wohnungen at the same address, a header node is created with the address details
     * and the individual Wohnungen as its children.
     *
     * @param filter The filter string to apply when searching for Wohnungen.
     * @return A list of Wohnungen, some of which may be header nodes grouping multiple Wohnungen at the same address.
     */
    public List<Wohnung> findWohnungenWithHierarchy(String filter) {
        // Retrieve all Wohnungen based on the filter
        List<Wohnung> wohnungen = findAllWohnungen(filter);

        // Group Wohnungen by their address (strasse + hausnummer)
        Map<String, List<Wohnung>> groupedWohnungen = wohnungen.stream()
                .collect(Collectors.groupingBy(wohnung -> wohnung.getStrasse() + " " + wohnung.getHausnummer()));

        List<Wohnung> wohnungsWithHierarchy = new ArrayList<>();
        // Iterate over each group of Wohnungen
        groupedWohnungen.forEach((address, wohnungenForAddress) -> {
            if (wohnungenForAddress.size() > 1) {
                // If there are multiple Wohnungen at the same address, create a header node
                Wohnung addressNode = new Wohnung();
                addressNode.setStrasse(wohnungenForAddress.getFirst().getStrasse());
                addressNode.setHausnummer(wohnungenForAddress.getFirst().getHausnummer());
                addressNode.setPostleitzahl(wohnungenForAddress.getFirst().getPostleitzahl());
                addressNode.setStadt(wohnungenForAddress.getFirst().getStadt());
                addressNode.setLand(wohnungenForAddress.getFirst().getLand());
                addressNode.setHeader(true);
                addressNode.setSubWohnungen(new ArrayList<>(wohnungenForAddress));
                wohnungsWithHierarchy.add(addressNode);
            } else {
                // If there is only one Wohnung at the address, add it directly to the list
                wohnungsWithHierarchy.addAll(wohnungenForAddress);
            }
        });

        return wohnungsWithHierarchy;
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

        // Delete Mietverträge associated with the Wohnung
        Mietvertrag mietvertrag = mietvertragRepository.findByWohnung(wohnung);
        mietvertragRepository.delete(mietvertrag);

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

    /**
     * Gets all Wohnungen that do not have an associated Mietvertrag.
     *
     * @return List of Wohnungen with no Mietvertrag
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findWohnungenWithoutMietvertrag() {
        // Get all Wohnungen
        List<Wohnung> allWohnungen = wohnungRepository.findAll();

        // Get all Wohnungen with a Mietvertrag
        List<Wohnung> wohnungenWithMietvertrag = mietvertragRepository.findAll().stream()
                .map(Mietvertrag::getWohnung)
                .toList();

        // Filter out Wohnungen that have a Mietvertrag
        return allWohnungen.stream()
                .filter(wohnung -> !wohnungenWithMietvertrag.contains(wohnung))
                .collect(Collectors.toList());
    }

    /**
     * Saves a Wohnung (apartment) entity to the database.
     * This method is transactional, ensuring that all operations
     * within the transaction are completed successfully or none are.
     *
     * @param wohnung the Wohnung entity to save
     * @return the saved Wohnung entity
     */
    @Transactional
    public Wohnung save(Wohnung wohnung) {
        return wohnungRepository.save(wohnung);
    }

    /**
     * Gets all Wohnungen where there is no active Mietvertrag, meaning the Wohnung is available.
     * This method is transactional and read-only.
     *
     * @return List of all available Wohnungen
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findAvailableWohnungen() {
        return mietvertragRepository.findAll().stream()
                .filter(mietvertrag -> mietvertrag.getMieter() == null)
                .map(Mietvertrag::getWohnung)
                .distinct()
                .collect(Collectors.toList());
    }
}