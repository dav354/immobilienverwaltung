package projektarbeit.immobilienverwaltung.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MieterService {

    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final DokumentRepository dokumentRepository;

    /**
     * Constructs a new MieterService with the given repositories.
     *
     * @param wohnungRepository      the repository for Wohnung entities
     * @param mieterRepository       the repository for Mieter entities
     * @param zaehlerstandRepository the repository for Zaehlerstand entities
     * @param dokumentRepository     the repository for Dokument entities
     */
    @Autowired
    public MieterService(WohnungRepository wohnungRepository,
                         MieterRepository mieterRepository,
                         ZaehlerstandRepository zaehlerstandRepository,
                         DokumentRepository dokumentRepository) {
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.dokumentRepository = dokumentRepository;
    }

    /**
     * Returns the count of Wohnung entities.
     *
     * @return the number of Wohnung entities
     */
    public long getWohnungCount() {
        return wohnungRepository.count();
    }

    /**
     * Returns the count of Mieter entities.
     *
     * @return the number of Mieter entities
     */
    public long getMieterCount() {
        return mieterRepository.count();
    }

    /**
     * Returns a List of all Wohnungen
     *
     * @return List of all Wohnungen
     */
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    /**
     * Returns the count of Zaehlerstand entities.
     *
     * @return the number of Zaehlerstand entities
     */
    public long getZaehlerstandCount() {
        return zaehlerstandRepository.count();
    }

    /**
     * Returns the count of Dokument entities.
     *
     * @return the number of Dokument entities
     */
    public long getDokumentCount() {
        return dokumentRepository.count();
    }

    /**
     * Retrieves all Mieter entities that match the given filter string.
     * If the filter string is null or empty, returns all Mieter entities.
     *
     * @param stringFilter the filter string to search for
     * @return a list of matching Mieter entities
     */
    public List<Mieter> findAllMieter(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return mieterRepository.findAll();
        } else {
            return mieterRepository.search(stringFilter);
        }
    }

    /**
     * Overloaded method that retrieves all Mieter entities without any filtering.
     *
     * @return a list of all Mieter entities
     */
    public List<Mieter> findAllMieter() {
        return findAllMieter(null);
    }

    /**
     * Deletes the given Mieter entity from the repository and sets the associated Wohnungen & Dokumente to null.
     *
     * @param mieter the Mieter entity to delete
     */
    public void deleteMieter(Mieter mieter) {
        if (mieter == null) throw new NullPointerException("Mieter is null");

        // Clone the lists to ensure they are mutable
        List<Dokument> dokumente = new ArrayList<>(mieter.getDokument());
        for (Dokument dokument : dokumente) {
            dokument.setMieter(null);
            dokumentRepository.save(dokument);
        }

        List<Wohnung> wohnungen = new ArrayList<>(mieter.getWohnung());
        for (Wohnung wohnung : wohnungen) {
            wohnung.setMieter(null); // Check inside this method for any collection operation errors
            wohnungRepository.save(wohnung); // Save the updated Wohnung entity
        }

        mieterRepository.delete(mieter); // Delete the Mieter entity
    }

    /**
     * Saves the given tenant (Mieter) to the repository.
     * Logs an error message if the tenant is null and does not perform any save operation.
     *
     * @param mieter The tenant to be saved. Must not be null.
     */
    public void saveMieter(Mieter mieter) {
        if (mieter == null) throw new NullPointerException("Mieter is null");
        mieterRepository.save(mieter);
    }

    /**
     * Assigns a list of Wohnungen to a Mieter and saves the changes.
     *
     * @param mieter    The Mieter to assign the Wohnungen to.
     * @param wohnungen The list of Wohnungen to assign.
     */
    @Transactional
    public void saveWohnungToMieter(Mieter mieter, List<Wohnung> wohnungen) {
        if (mieter == null) throw new NullPointerException("saveWohnungToMieter: Mieter is NULL.");
        wohnungen.forEach(wohnung -> {
            mieter.getWohnung().add(wohnung);  // Assuming Mieter has a collection of Wohnungen
            wohnung.setMieter(mieter);
        });

        mieterRepository.save(mieter);
        wohnungRepository.saveAll(wohnungen);  // Efficiently save all Wohnungen at once
    }

    /**
     * Removes a list of Wohnungen from a Mieter and updates the entities in the database.
     *
     * @param mieter    The Mieter from which the Wohnungen will be removed.
     * @param wohnungen The list of Wohnungen to remove.
     */
    @Transactional
    public void removeWohnungFromMieter(Mieter mieter, List<Wohnung> wohnungen) {
        if (mieter == null || wohnungen == null) {

            return;
        }

        wohnungen.forEach(wohnung -> {
            mieter.getWohnung().remove(wohnung);  // Remove the Wohnung from the Mieter's collection
            wohnung.setMieter(null);  // Clear the Mieter reference from the Wohnung
        });

        mieterRepository.save(mieter);
        wohnungRepository.saveAll(wohnungen);  // Efficiently save all Wohnungen at once
    }

    /**
     * Gets all Wohnungen where Mieter is NULL, so the wohnung is free
     *
     * @return List of all available Wohnungen
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findAvailableWohnungen() {
        return wohnungRepository.findByMieterIsNull();
    }
}