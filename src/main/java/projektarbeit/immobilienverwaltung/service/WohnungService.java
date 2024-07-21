package projektarbeit.immobilienverwaltung.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service-Klasse zur Verwaltung von Wohnung-Entitäten und damit verbundenen Operationen.
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
public class WohnungService {

    private final WohnungRepository wohnungRepository;
    private final DokumentRepository dokumentRepository;
    private final MieterRepository mieterRepository;
    private final MietvertragRepository mietvertragRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final GeocodingService geocodingService;

    @Autowired
    public WohnungService(WohnungRepository wohnungRepository,
                          DokumentRepository dokumentRepository,
                          MieterRepository mieterRepository,
                          MietvertragRepository mietvertragRepository,
                          ZaehlerstandRepository zaehlerstandRepository,
                          GeocodingService geocodingService) {
        this.wohnungRepository = wohnungRepository;
        this.dokumentRepository = dokumentRepository;
        this.mieterRepository = mieterRepository;
        this.mietvertragRepository = mietvertragRepository;
        this.zaehlerstandRepository = zaehlerstandRepository;
        this.geocodingService = geocodingService;
    }

    /**
     * Ruft alle Wohnung-Entitäten ab.
     *
     * @return eine Liste aller Wohnung-Entitäten
     */
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    /**
     * Ruft alle Mieter-Entitäten ab.
     *
     * @return eine Liste aller Mieter-Entitäten
     */
    public List<Mieter> findAllMieter() {
        return mieterRepository.findAll();
    }

    /**
     * Findet eine Wohnung anhand ihrer ID.
     *
     * @param wohnungId die ID der zu findenden Wohnung
     * @return die Wohnung mit der angegebenen ID oder null, wenn sie nicht gefunden wurde
     */
    @Transactional(readOnly = true)
    public Wohnung findWohnungById(Long wohnungId) {
        return wohnungRepository.findById(wohnungId).orElse(null);
    }

    /**
     * Findet Wohnungen und gruppiert sie in eine hierarchische Struktur basierend auf ihrer Adresse.
     * Wenn es mehrere Wohnungen an derselben Adresse gibt, wird ein Header-Knoten mit den Adressdetails
     * erstellt und die einzelnen Wohnungen als dessen Kinder hinzugefügt.
     *
     * @param filter Der Filterstring, der bei der Suche nach Wohnungen angewendet wird.
     * @return Eine Liste von Wohnungen, von denen einige Header-Knoten sind, die mehrere Wohnungen an derselben Adresse gruppieren.
     */
    public List<Wohnung> findWohnungenWithHierarchy(String filter) {
        // Alle Wohnungen basierend auf dem Filter abrufen
        List<Wohnung> wohnungen = findAllWohnungen(filter);

        // Wohnungen nach ihrer Adresse (strasse + hausnummer) gruppieren
        Map<String, List<Wohnung>> groupedWohnungen = wohnungen.stream().collect(Collectors.groupingBy(this::getAddress));

        List<Wohnung> wohnungenWithHierarchy = new ArrayList<>();
        // Über jede Gruppe von Wohnungen iterieren
        groupedWohnungen.forEach((address, wohnungenForAddress) -> {
            if (wohnungenForAddress.size() > 1) {
                // Wenn es mehrere Wohnungen an derselben Adresse gibt, einen Header-Knoten erstellen
                Wohnung addressNode = new Wohnung();
                addressNode.setStrasse(wohnungenForAddress.get(0).getStrasse());
                addressNode.setHausnummer(wohnungenForAddress.get(0).getHausnummer());
                addressNode.setPostleitzahl(wohnungenForAddress.get(0).getPostleitzahl());
                addressNode.setStadt(wohnungenForAddress.get(0).getStadt());
                addressNode.setLand(wohnungenForAddress.get(0).getLand());
                addressNode.setHeader(true);
                addressNode.setSubWohnungen(new ArrayList<>(wohnungenForAddress));
                wohnungenWithHierarchy.add(addressNode);
            } else {
                // Wenn es nur eine Wohnung an der Adresse gibt, direkt zur Liste hinzufügen
                wohnungenWithHierarchy.addAll(wohnungenForAddress);
            }
        });

        return wohnungenWithHierarchy;
    }

    // Hilfsmethode zum Abrufen der Adresszeichenfolge
    private String getAddress(Wohnung wohnung) {
        return String.format("%s %s", wohnung.getStrasse(), wohnung.getHausnummer());
    }

    /**
     * Löscht eine Wohnung und deren zugehörige Details. Entfernt auch Referenzen zur Wohnung aus verwandten Entitäten.
     *
     * @param wohnung die zu löschende Wohnung
     */
    @Transactional
    public void delete(Wohnung wohnung) {
        // Dokumente löschen, die mit der Wohnung verknüpft sind
        List<Dokument> dokumente = dokumentRepository.findByWohnung(wohnung);
        if (dokumente != null && !dokumente.isEmpty()) {
            dokumentRepository.deleteAll(dokumente);
        }

        // Mietverträge löschen, die mit der Wohnung verknüpft sind
        Mietvertrag mietvertrag = mietvertragRepository.findByWohnung(wohnung);
        if (mietvertrag != null) {
            mietvertrag.setMieter(null);
            mietvertragRepository.delete(mietvertrag);
        }

        // Zählerstand-Referenzen zur Wohnung löschen
        List<Zaehlerstand> zaehlerstaende = zaehlerstandRepository.findByWohnung(wohnung);
        if (zaehlerstaende != null && !zaehlerstaende.isEmpty()) {
            zaehlerstandRepository.deleteAll(zaehlerstaende);
        }

        // Wohnung-Entität löschen
        wohnungRepository.delete(wohnung);
    }

    /**
     * Ruft die Dokument-Entitäten ab, die mit einer bestimmten Wohnung verknüpft sind.
     *
     * @param wohnung die Wohnung, für die die Dokumente gefunden werden sollen
     * @return eine Liste von Dokument-Entitäten, die mit der angegebenen Wohnung verknüpft sind
     */
    public List<Dokument> findDokumenteByWohnung(Wohnung wohnung) {
        return dokumentRepository.findByWohnung(wohnung);
    }

    /**
     * Service-Methode, um alle Wohnungen basierend auf einem gegebenen Filterstring zu finden.
     * Wenn der Filterstring null oder leer ist, werden alle Wohnungen zurückgegeben.
     * Wenn der Filterstring angegeben wird, werden zunächst alle passenden Wohnungen gesucht.
     *
     * @param stringFilter Der Filterstring, um nach passenden Wohnungen zu suchen. Wenn null oder leer, werden alle Wohnungen zurückgegeben.
     * @return Eine Liste von Wohnungen, die dem angegebenen Filterstring entsprechen. Wenn kein Filter angegeben ist, werden alle Wohnungen zurückgegeben.
     */
    public List<Wohnung> findAllWohnungen(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return wohnungRepository.findAll();
        } else {
            return wohnungRepository.search(stringFilter);
        }
    }

    /**
     * Ruft alle Wohnungen ab, die keinen zugehörigen Mietvertrag haben.
     *
     * @return eine Liste von Wohnungen ohne Mietvertrag
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findWohnungenWithoutMietvertrag() {
        // Alle Wohnungen abrufen
        List<Wohnung> allWohnungen = wohnungRepository.findAll();

        // Alle Wohnungen mit einem Mietvertrag abrufen
        List<Wohnung> wohnungenWithMietvertrag = mietvertragRepository.findAll().stream()
                .map(Mietvertrag::getWohnung)
                .collect(Collectors.toList());

        // Wohnungen filtern, die keinen Mietvertrag haben
        return allWohnungen.stream()
                .filter(wohnung -> !wohnungenWithMietvertrag.contains(wohnung))
                .collect(Collectors.toList());
    }

    /**
     * Speichert eine Wohnung-Entität in der Datenbank.
     * Diese Methode ist transactional und stellt sicher, dass alle Operationen
     * innerhalb der Transaktion erfolgreich abgeschlossen werden oder keine.
     *
     * @param wohnung die zu speichernde Wohnung-Entität
     * @return die gespeicherte Wohnung-Entität
     */
    @Transactional
    public Wohnung save(@Valid Wohnung wohnung) {
        setCoordinates(wohnung);
        return wohnungRepository.save(wohnung);
    }

    /**
     * Setzt die Koordinaten (Breiten- und Längengrad) einer Wohnung basierend auf ihrer Adresse.
     *
     * @param wohnung die Wohnung-Entität, für die die Koordinaten gesetzt werden sollen
     */
    private void setCoordinates(Wohnung wohnung) {
        String address = String.format("%s %s, %s, %s, %s",
                wohnung.getStrasse(), wohnung.getHausnummer(),
                wohnung.getPostleitzahl(), wohnung.getStadt(),
                wohnung.getLand().name());

        double[] coordinates = geocodingService.getCoordinates(address);
        if (coordinates != null) {
            wohnung.setLatitude(coordinates[0]);
            wohnung.setLongitude(coordinates[1]);
        } else {
            wohnung.setLatitude(null);
            wohnung.setLongitude(null);
        }
    }

    /**
     * Ruft alle Wohnungen ab, für die kein aktiver Mietvertrag besteht, d.h. die Wohnung ist verfügbar.
     * Diese Methode ist transactional und nur lesend.
     *
     * @return eine Liste aller verfügbaren Wohnungen
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findAvailableWohnungen() {
        return mietvertragRepository.findAll().stream()
                .filter(mietvertrag -> mietvertrag.getMieter() == null)
                .map(Mietvertrag::getWohnung)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Ruft die Liste der Zählerstand-Entitäten ab, die mit einer bestimmten Wohnung verknüpft sind.
     * Diese Methode fragt das ZaehlerstandRepository ab, um alle Zählerstand-Einträge abzurufen, die mit der angegebenen Wohnung verknüpft sind.
     *
     * @param wohnung die Wohnung, für die die Zählerstand-Einträge gefunden werden sollen
     * @return eine Liste von Zählerstand-Einträgen, die mit der angegebenen Wohnung verknüpft sind
     */
    @Transactional(readOnly = true)
    public List<Zaehlerstand> findZaehlerstandByWohnung(Wohnung wohnung) {
        if (wohnung == null) {
            throw new IllegalArgumentException("Wohnung darf nicht null sein");
        }
        return zaehlerstandRepository.findByWohnung(wohnung);
    }
}