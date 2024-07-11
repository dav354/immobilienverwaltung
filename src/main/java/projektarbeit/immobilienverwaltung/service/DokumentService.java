package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;

import java.util.List;

/**
 * Service-Klasse zur Verwaltung von Dokument-Entitäten.
 */
@Service
public class DokumentService {

    private final DokumentRepository dokumentRepository;

    /**
     * Konstruktor für DokumentService mit dem angegebenen DokumentRepository.
     *
     * @param dokumentRepository das Repository für Dokument-Entitäten
     */
    @Autowired
    public DokumentService(DokumentRepository dokumentRepository) {
        this.dokumentRepository = dokumentRepository;
    }

    /**
     * Ruft alle Dokument-Entitäten ab.
     *
     * @return eine Liste aller Dokument-Entitäten
     */
    public List<Dokument> findAllDokumente() {
        return dokumentRepository.findAll();
    }

    /**
     * Löscht alle Dokument-Entitäten, die mit der angegebenen Wohnung verknüpft sind.
     * Wenn ein Dokument keinen zugehörigen Mieter hat, wird es gelöscht. Andernfalls wird die Referenz zur Wohnung auf null gesetzt und das Dokument wird aktualisiert.
     *
     * @param wohnung die Wohnung-Entität, deren zugehörige Dokument-Entitäten gelöscht oder aktualisiert werden sollen
     */
    @Transactional
    public void deleteDokumenteByWohnung(Wohnung wohnung) {
        if (wohnung == null) throw new NullPointerException("Wohnung ist null");
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
     * Findet und gibt eine Liste von Dokument-Entitäten zurück, die mit einer bestimmten Wohnung verknüpft sind.
     *
     * @param wohnung Die Wohnung-Entität, für die die zugehörigen Dokumente gefunden werden sollen.
     * @return Eine Liste von Dokument-Entitäten, die mit der angegebenen Wohnung verknüpft sind.
     */
    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }

    /**
     * Findet und gibt eine Liste von Dokument-Entitäten zurück, die mit einem bestimmten Mieter verknüpft sind.
     *
     * @param mieter Die Mieter-Entität, für die die zugehörigen Dokumente gefunden werden sollen.
     * @return Eine Liste von Dokument-Entitäten, die mit dem angegebenen Mieter verknüpft sind.
     */
    public List<Dokument> findDokumenteByMieter(Mieter mieter) {
        return dokumentRepository.findByMieter(mieter);
    }
}