package projektarbeit.immobilienverwaltung.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.demo.AssignMieterToWohnungDemo;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.List;

@Service
public class MieterService {

    private static final Logger logger = LoggerFactory.getLogger(AssignMieterToWohnungDemo.class);

    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final DokumentRepository dokumentRepository;

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

    public long getWohnungCount() {
        return wohnungRepository.count();
    }

    public long getMieterCount() {
        return mieterRepository.count();
    }

    public long getZaehlerstandCount() {
        return zaehlerstandRepository.count();
    }

    public long getDokumentCount() {
        return dokumentRepository.count();
    }

    // Alle Mieter aus den Daten holen
    public List<Mieter> findAllMieter(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return mieterRepository.findAll();
        } else {
            return mieterRepository.search(stringFilter);
        }
    }

    // Overloaded method that takes no arguments
    public List<Mieter> findAllMieter(){
        return findAllMieter(null);
    }

    public long countMieter() {
        return mieterRepository.count();
    }

    public void deleteMieter(Mieter mieter) {
        mieterRepository.delete(mieter);
    }

    public void saveMieter(Mieter mieter) {
        if (mieter == null) {
            logger.error("Mieter is null.");
            return;
        }
        mieterRepository.save(mieter);
    }

    //Alle Wohnungen aus den Daten holen
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }

    /**
     * Assigns a list of Wohnungen to a Mieter and saves the changes.
     * @param mieter The Mieter to assign the Wohnungen to.
     * @param wohnungen The list of Wohnungen to assign.
     */
    @Transactional
    public void saveWohnungToMieter(Mieter mieter, List<Wohnung> wohnungen) {
        if (mieter == null || wohnungen == null || wohnungen.isEmpty()) {
            logger.error("saveWohnungToMieter: Mieter or Wohnung is NULL.");
            return;
        }

        wohnungen.forEach(wohnung -> {
            mieter.getWohnung().add(wohnung);  // Assuming Mieter has a collection of Wohnungen
            wohnung.setMieter(mieter);
        });

        mieterRepository.save(mieter);
        wohnungRepository.saveAll(wohnungen);  // Efficiently save all Wohnungen at once
    }

    /**
     * Removes a list of Wohnungen from a Mieter and updates the entities in the database.
     * @param mieter The Mieter from which the Wohnungen will be removed.
     * @param wohnungen The list of Wohnungen to remove.
     */
    @Transactional
    public void removeWohnungFromMieter(Mieter mieter, List<Wohnung> wohnungen) {
        if (mieter == null || wohnungen == null) {
            logger.error("removeWohnungFromMieter: Mieter or Wohnung is NULL.");
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
     * @return List of all available Wohnungen
     */
    @Transactional(readOnly = true)
    public List<Wohnung> findAvailableWohnungen() {
        return wohnungRepository.findByMieterIsNull();
    }
}