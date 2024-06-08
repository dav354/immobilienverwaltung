package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Service
public class MieterService {

    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final DokumentRepository dokumentRepository;
    private final MietvertragRepository mietvertragRepository;

    /**
     * Constructs a new MieterService with the given repositories.
     *
     * @param wohnungRepository      the repository for Wohnung entities
     * @param mieterRepository       the repository for Mieter entities
     * @param zaehlerstandRepository the repository for Zaehlerstand entities
     * @param dokumentRepository     the repository for Dokument entities
     * @param mietvertragRepository  the repository for Mietvertrag entities
     */
    @Autowired
    public MieterService(WohnungRepository wohnungRepository,
                         MieterRepository mieterRepository,
                         ZaehlerstandRepository zaehlerstandRepository,
                         DokumentRepository dokumentRepository,
                         MietvertragRepository mietvertragRepository) {
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.dokumentRepository = dokumentRepository;
        this.mietvertragRepository = mietvertragRepository;
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
        return stringFilter == null || stringFilter.isEmpty() ? mieterRepository.findAll() : mieterRepository.search(stringFilter);
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
    @Transactional
    public void deleteMieter(Mieter mieter) {
        if (mieter == null) throw new NullPointerException("Mieter is null");

        List<Mietvertrag> mietvertraege = mietvertragRepository.findByMieter_MieterId(mieter.getMieter_id());
        mietvertragRepository.deleteAll(mietvertraege);

        List<Dokument> dokumente = new ArrayList<>(mieter.getDokument());
        for (Dokument dokument : dokumente) {
            dokument.setMieter(null);
            dokumentRepository.save(dokument);
        }

        mieterRepository.delete(mieter);
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
     * Checks if an email already exists in the repository.
     *
     * @param email the email to check.
     * @return true if the email exists, false otherwise.
     */
    public boolean emailExists(String email) {
        return mieterRepository.existsByEmail(email);
    }

    /**
     * Save Mietvertrag.
     *
     * @param mietvertrag the Mietvertrag to save
     * @return the saved Mietvertrag
     */
    public Mietvertrag saveMietvertrag(Mietvertrag mietvertrag) {
        return mietvertragRepository.save(mietvertrag);
    }

    /**
     * Delete Mietvertrag.
     *
     * @param mietvertrag the Mietvertrag to delete
     */
    public void deleteMietvertrag(Mietvertrag mietvertrag) {
        mietvertragRepository.delete(mietvertrag);
    }
}