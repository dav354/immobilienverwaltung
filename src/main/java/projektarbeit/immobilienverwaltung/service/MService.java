package projektarbeit.immobilienverwaltung.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.List;

@Service
public class MService {


    private final WohnungRepository wohnungRepository;
    private final MieterRepository mieterRepository;
    private final ZaehlerstandRepository zaehlerstandRepository;
    private final DokumentRepository dokumentRepository;

    @Autowired
    public MService(WohnungRepository wohnungRepository, MieterRepository mieterRepository,
                    ZaehlerstandRepository zaehlerstandRepository, DokumentRepository dokumentRepository) {
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

    public long countMieter() {
        return mieterRepository.count();
    }

    public void deleteMieter(Mieter mieter) {
        mieterRepository.delete(mieter);
    }

    public void saveMieter(Mieter mieter) {
        if (mieter == null) {
            System.err.println("Mieter is null.");
            return;
        }
        mieterRepository.save(mieter);
    }

    //Alle Wohnungen aus den Daten holen
    public List<Wohnung> findAllWohnungen() {
        return wohnungRepository.findAll();
    }
}