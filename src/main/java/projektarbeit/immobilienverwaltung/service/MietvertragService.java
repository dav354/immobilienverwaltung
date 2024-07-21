package projektarbeit.immobilienverwaltung.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MietvertragRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service-Klasse zur Verwaltung von Mietvertrag-Entitäten.
 */
@Service
public class MietvertragService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MietvertragRepository mietvertragRepository;
    private final WohnungService wohnungService;

    /**
     * Konstruktor für MietvertragService mit dem angegebenen Repository.
     *
     * @param mietvertragRepository das Repository zur Verwaltung von Mietvertrag-Entitäten
     */
    @Autowired
    public MietvertragService(MietvertragRepository mietvertragRepository, WohnungService wohnungService) {
        this.mietvertragRepository = mietvertragRepository;
        this.wohnungService = wohnungService;
    }

    /**
     * Speichert eine Mietvertrag-Entität.
     *
     * @param mietvertrag die zu speichernde Mietvertrag-Entität
     */
    public void saveMietvertrag(@Valid Mietvertrag mietvertrag) {
        mietvertragRepository.save(mietvertrag);
    }

    /**
     * Löscht eine Mietvertrag-Entität.
     *
     * @param mietvertrag die zu löschende Mietvertrag-Entität
     */
    @Transactional
    public void deleteMietvertrag(Mietvertrag mietvertrag) {
        Mietvertrag managedMietvertrag = entityManager.find(Mietvertrag.class, mietvertrag.getMietvertrag_id());

        if (managedMietvertrag != null) {
            Mieter m = managedMietvertrag.getMieter();
            Wohnung w = managedMietvertrag.getWohnung();

            if (w != null && w.getMietvertrag() != null) w.setMietvertrag(null);
            if (m != null && m.getMietvertraege() != null)  m.getMietvertraege().remove(managedMietvertrag);

            // Löschen Sie den Mietvertrag aus dem Repository
            mietvertragRepository.delete(managedMietvertrag);
        }
    }

    /**
     * Ruft alle Mietvertrag-Entitäten ab.
     *
     * @return eine Liste aller Mietvertrag-Entitäten
     */
    public List<Mietvertrag> findAll() {
        return mietvertragRepository.findAll();
    }

    /**
     * Findet Mietvertrag-Entitäten anhand der Mieter-ID.
     *
     * @param mieterId die ID des Mieters
     * @return eine Liste von Mietvertrag-Entitäten für die angegebene Mieter-ID
     */
    public List<Mietvertrag> findByMieter(Long mieterId) {
        return mietvertragRepository.findByMieter_MieterId(mieterId);
    }

    /**
     * Findet eine Mietvertrag-Entität anhand der Wohnung.
     *
     * @param wohnung die Wohnung, nach der gesucht werden soll
     * @return die Mietvertrag-Entität für die angegebene Wohnung
     */
    public Mietvertrag findByWohnung(Wohnung wohnung) {
        return mietvertragRepository.findByWohnung(wohnung);
    }

    /**
     * Findet den Mieter, der mit einer bestimmten Wohnung verknüpft ist.
     *
     * @param wohnung die Wohnung, nach der gesucht werden soll
     * @return der Mieter, der mit der angegebenen Wohnung verknüpft ist, oder null, wenn kein Mietvertrag existiert
     */
    public Mieter findMieterByWohnung(Wohnung wohnung) {
        Mietvertrag mietvertrag = mietvertragRepository.findByWohnung(wohnung);
        if (mietvertrag != null) {
            return mietvertrag.getMieter();
        }
        return null;
    }

    /**
     * Erstellt und speichert einen Mietvertrag für einen bestimmten Mieter und eine bestimmte Wohnung.
     *
     * @param mieter         Der Mieter, dem der Mietvertrag zugewiesen wird
     * @param wohnung        Die Wohnung, für die der Mietvertrag erstellt wird
     * @param mietbeginn     Das Startdatum des Mietvertrags
     * @param mietende       Das Enddatum des Mietvertrags
     * @param miete          Der monatliche Mietbetrag
     * @param kaution        Der Kautionsbetrag
     * @param anzahlBewohner Die Anzahl der Bewohner
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