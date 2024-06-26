package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Service
public class ZaehlerstandService {

    private final ZaehlerstandRepository zaehlerstandRepository;

    /**
     * Constructs a new ZaehlerstandService with the specified ZaehlerstandRepository.
     *
     * @param zaehlerstandRepository the repository for Zaehlerstand entities
     */
    @Autowired
    public ZaehlerstandService(ZaehlerstandRepository zaehlerstandRepository) {
        this.zaehlerstandRepository = zaehlerstandRepository;
    }

    /**
     * Saves or updates a Zaehlerstand entity.
     *
     * @param zaehlerstand the Zaehlerstand entity to save or update
     * @return the saved or updated Zaehlerstand entity
     */
    public Zaehlerstand saveOrUpdateZaehlerstand(Zaehlerstand zaehlerstand) {
        if (zaehlerstand == null) throw new NullPointerException("Zaehlerstand is null");
        return zaehlerstandRepository.save(zaehlerstand);
    }

    /**
     * Retrieves all Zaehlerstand entities.
     *
     * @return a list of all Zaehlerstand entities
     */
    public List<Zaehlerstand> findAllZaehlerstaende() {
        return zaehlerstandRepository.findAll();
    }

    /**
     * Retrieves a Zaehlerstand entity by its ID.
     *
     * @param id the ID of the Zaehlerstand entity to retrieve
     * @return the Zaehlerstand entity with the specified ID, or null if not found
     */
    public Zaehlerstand getZaehlerstandById(Long id) {
        return zaehlerstandRepository.findById(id).orElse(null);
    }

    /**
     * Deletes a Zaehlerstand entity by its ID.
     *
     * @param id the ID of the Zaehlerstand entity to delete
     */
    public void deleteZaehlerstand(Long id) {
        zaehlerstandRepository.deleteById(id);
    }

    /**
     * Finds and returns a list of Zaehlerstand entities associated with a given Wohnung.
     *
     * @param wohnung     The Wohnung entity for which to find associated Zaehlerstände.
     * @param pageRequest
     * @return A list of Zaehlerstand entities associated with the specified Wohnung.
     */
    public List<Zaehlerstand> findZaehlerstandByWohnung(Wohnung wohnung) {
        return zaehlerstandRepository.findByWohnung(wohnung);
    }

    /**
     * Deletes a Zaehlerstand entity.
     *
     * @param zaehlerstand the Zaehlerstand to delete
     */
    public void deleteZaehlerstand(Zaehlerstand zaehlerstand) {
        zaehlerstandRepository.delete(zaehlerstand);
    }

}