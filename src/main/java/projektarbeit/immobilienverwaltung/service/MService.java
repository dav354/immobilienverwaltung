package projektarbeit.immobilienverwaltung.service;


import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.util.List;

@Service
public class MService {

    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;

    //Konstruktor der Repositorys
    public MService(MieterRepository mieterRepository,
                    WohnungRepository wohnungRepository) {
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
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

