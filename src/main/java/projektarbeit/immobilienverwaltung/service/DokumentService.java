package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;

import java.util.List;

@Service
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    /**
     * Constructs a new DokumentService with the specified DokumentRepository.
     *
     * @param dokumentRepository the repository for Dokument entities
     */
    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    /**
     * Retrieves all Dokument entities.
     *
     * @return a list of all Dokument entities
     */
    public List<Dokument> findAllDokumente() {
        return dokumentRepository.findAll();
    }

    /**
     * Deletes all Dokument entities associated with the given Wohnung. If a Dokument has no associated Mieter, it is deleted. Otherwise, the reference to the Wohnung is set to null and the Dokument is updated.
     *
     * @param wohnung the Wohnung entity whose associated Dokument entities are to be deleted or updated
     */
    @Transactional
    public void deleteDokumenteByWohnung(Wohnung wohnung) {
        if (wohnung == null) throw new NullPointerException("Wohnung is null");
        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);
        for (Dokument dokument : dokumente) {
            dokument.setWohnung(null);
            if (dokument.getMieter() == null) {
                dokumentRepository.delete(dokument);
            } else {
                dokumentRepository.save(dokument);
            }
        }
    }

    /**
     * Finds and returns a list of Dokument entities associated with a given Wohnung.
     *
     * @param wohnung     The Wohnung entity for which to find associated Dokumente.
     * @param pageRequest
     * @return A list of Dokument entities associated with the specified Wohnung.
     */
    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }

    /**
     * Finds and returns a list of Dokument entities associated with a given Mieter.
     *
     * @param mieter The Mieter entity for which to find associated Dokumente.
     * @return A list of Dokument entities associated with the specified Mieter.
     */
    public List<Dokument> findDokumenteByMieter(Mieter mieter) {
        return dokumentRepository.findByMieter(mieter);
    }
}