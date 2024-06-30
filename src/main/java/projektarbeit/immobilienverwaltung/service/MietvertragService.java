package projektarbeit.immobilienverwaltung.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MietvertragRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing Mietvertrag (rental contract) entities.
 */
@Service
public class MietvertragService {

    private final MietvertragRepository mietvertragRepository;

    /**
     * Constructs a MietvertragService with the given repository.
     *
     * @param mietvertragRepository the repository for managing Mietvertrag entities
     */
    @Autowired
    public MietvertragService(MietvertragRepository mietvertragRepository) {
        this.mietvertragRepository = mietvertragRepository;
    }

    /**
     * Saves a Mietvertrag entity.
     *
     * @param mietvertrag the Mietvertrag entity to save
     */
    public void saveMietvertrag(@Valid Mietvertrag mietvertrag) {
        mietvertragRepository.save(mietvertrag);
    }

    /**
     * Deletes a Mietvertrag entity.
     *
     * @param mietvertrag the Mietvertrag entity to delete
     */
    public void deleteMietvertrag(Mietvertrag mietvertrag) {
        mietvertragRepository.delete(mietvertrag);
    }

    /**
     * Retrieves all Mietvertrag entities.
     *
     * @return a list of all Mietvertrag entities
     */
    public List<Mietvertrag> findAll() {
        return mietvertragRepository.findAll();
    }

    /**
     * Finds Mietvertrag entities by the Mieter ID.
     *
     * @param mieterId the ID of the Mieter
     * @return a list of Mietvertrag entities for the given Mieter ID
     */
    public List<Mietvertrag> findByMieter(Long mieterId) {
        return mietvertragRepository.findByMieter_MieterId(mieterId);
    }

    /**
     * Finds a Mietvertrag entity by the Wohnung.
     *
     * @param wohnung the Wohnung to search for
     * @return the Mietvertrag entity for the given Wohnung
     */
    public Mietvertrag findByWohnung(Wohnung wohnung) {
        return mietvertragRepository.findByWohnung(wohnung);
    }

    /**
     * Finds the Mieter associated with a given Wohnung.
     *
     * @param wohnung the Wohnung to search for
     * @return the Mieter associated with the given Wohnung, or null if no Mietvertrag exists
     */
    public Mieter findMieterByWohnung(Wohnung wohnung) {
        Mietvertrag mietvertrag = mietvertragRepository.findByWohnung(wohnung);
        if (mietvertrag != null) {
            return mietvertrag.getMieter();
        }
        return null;
    }

    /**
     * Creates and saves a Mietvertrag for a given Mieter and Wohnung.
     *
     * @param mieter         The Mieter to whom the Mietvertrag is assigned
     * @param wohnung        The Wohnung for which the Mietvertrag is created
     * @param mietbeginn     The start date of the Mietvertrag
     * @param mietende       The end date of the Mietvertrag
     * @param miete          The monthly rent amount
     * @param kaution        The deposit amount
     * @param anzahlBewohner The number of inhabitants
     */
    public void createAndSaveMietvertrag(Mieter mieter, Wohnung wohnung, LocalDate mietbeginn, LocalDate mietende, double miete, double kaution, int anzahlBewohner) {
        Mietvertrag mietvertrag = new Mietvertrag();
        mietvertrag.setMieter(mieter);
        mietvertrag.setWohnung(wohnung);
        mietvertrag.setMietbeginn(mietbeginn);
        mietvertrag.setMietende(mietende);
        mietvertrag.setMiete(miete);
        mietvertrag.setKaution(kaution);
        mietvertrag.setAnzahlBewohner(anzahlBewohner);
        saveMietvertrag(mietvertrag);
    }
}