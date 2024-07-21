package projektarbeit.immobilienverwaltung.service;

import jakarta.validation.Valid;
import org.hibernate.Hibernate;
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

/**
 * Service-Klasse zur Verwaltung von Mieter-Entitäten und damit verbundenen Operationen.
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Service
public class MieterService {

    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final DokumentRepository dokumentRepository;
    private final MietvertragRepository mietvertragRepository;
    private final MietvertragService mietvertragService;

    /**
     * Konstruktor für MieterService mit den angegebenen Repositories.
     *
     * @param wohnungRepository      das Repository für Wohnung-Entitäten
     * @param mieterRepository       das Repository für Mieter-Entitäten
     * @param zaehlerstandRepository das Repository für Zaehlerstand-Entitäten
     * @param dokumentRepository     das Repository für Dokument-Entitäten
     * @param mietvertragRepository  das Repository für Mietvertrag-Entitäten
     */
    @Autowired
    public MieterService(WohnungRepository wohnungRepository,
                         MieterRepository mieterRepository,
                         ZaehlerstandRepository zaehlerstandRepository,
                         DokumentRepository dokumentRepository,
                         MietvertragRepository mietvertragRepository,
                         MietvertragService mietvertragService) {
        this.wohnungRepository = wohnungRepository;
        this.mieterRepository = mieterRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.dokumentRepository = dokumentRepository;
        this.mietvertragRepository = mietvertragRepository;
        this.mietvertragService = mietvertragService;
    }

    /**
     * Gibt die Anzahl der Wohnung-Entitäten zurück.
     *
     * @return die Anzahl der Wohnung-Entitäten
     */
    public long getWohnungCount() {
        return wohnungRepository.count();
    }

    /**
     * Gibt die Anzahl der Mieter-Entitäten zurück.
     *
     * @return die Anzahl der Mieter-Entitäten
     */
    public long getMieterCount() {
        return mieterRepository.count();
    }

    /**
     * Gibt eine Liste aller Wohnungen zurück.
     *
     * @return eine Liste aller Wohnungen
     */
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    /**
     * Gibt die Anzahl der Zaehlerstand-Entitäten zurück.
     *
     * @return die Anzahl der Zaehlerstand-Entitäten
     */
    public long getZaehlerstandCount() {
        return zaehlerstandRepository.count();
    }

    /**
     * Gibt die Anzahl der Dokument-Entitäten zurück.
     *
     * @return die Anzahl der Dokument-Entitäten
     */
    public long getDokumentCount() {
        return dokumentRepository.count();
    }

    /**
     * Ruft alle Mieter-Entitäten ab, die dem angegebenen Filterstring entsprechen.
     * Wenn der Filterstring null oder leer ist, werden alle Mieter-Entitäten zurückgegeben.
     *
     * @param stringFilter der Filterstring, nach dem gesucht werden soll
     * @return eine Liste der passenden Mieter-Entitäten
     */
    public List<Mieter> findAllMieter(String stringFilter) {
        return stringFilter == null || stringFilter.isEmpty() ? mieterRepository.findAll() : mieterRepository.search(stringFilter);
    }

    /**
     * Überladene Methode, die alle Mieter-Entitäten ohne Filterung abruft.
     *
     * @return eine Liste aller Mieter-Entitäten
     */
    public List<Mieter> findAllMieter() {
        return findAllMieter(null);
    }

    /**
     * Löscht die angegebene Mieter-Entität aus dem Repository und setzt die zugehörigen Wohnungen und Dokumente auf null.
     *
     * @param mieter die zu löschende Mieter-Entität
     */
    @Transactional
    public void deleteMieter(Mieter mieter) {
        if (mieter == null) throw new NullPointerException("Mieter ist null");

        List<Mietvertrag> mietvertraege = mietvertragRepository.findByMieter_MieterId(mieter.getMieter_id());
        if (!mietvertraege.isEmpty()) mietvertraege.forEach(mietvertragService::deleteMietvertrag);

        // Initialisieren der Dokumente-Sammlung
        mieter = mieterRepository.findById(mieter.getMieter_id()).orElse(null);
        if (mieter != null) {
            Hibernate.initialize(mieter.getDokument());
            List<Dokument> dokumente = new ArrayList<>(mieter.getDokument());
            if (!dokumente.isEmpty()) dokumentRepository.deleteAll(dokumente);

            mieterRepository.delete(mieter);
        }
    }

    /**
     * Speichert den angegebenen Mieter im Repository.
     * Gibt eine Fehlermeldung aus, wenn der Mieter null ist, und führt keine Speicheroperation durch.
     *
     * @param mieter Der zu speichernde Mieter. Darf nicht null sein.
     */
    public void saveMieter(@Valid Mieter mieter) {
        mieterRepository.save(mieter);
    }

    /**
     * Prüft, ob eine E-Mail bereits im Repository existiert.
     *
     * @param email die zu prüfende E-Mail.
     * @return true, wenn die E-Mail existiert, andernfalls false.
     */
    public boolean emailExists(String email) {
        return mieterRepository.existsByEmail(email);
    }

    /**
     * Findet einen Mieter anhand der ID.
     *
     * @param id die ID des Mieters
     * @return der gefundene Mieter oder null, wenn keiner gefunden wurde
     */
    public Mieter findById(Long id) {
        return mieterRepository.findById(id).orElse(null);
    }
}