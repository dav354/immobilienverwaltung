package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.repository.MietvertragRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Service-Klasse für das Dashboard, die Methoden zur Berechnung von Statistiken und Einnahmen bereitstellt.
 */
@Service
public class DashboardService {

    private final MieterRepository mieterRepository;
    private final WohnungRepository wohnungRepository;
    private final MietvertragRepository mietvertragRepository;

    @Autowired
    public DashboardService(MieterRepository mieterRepository,
                            WohnungRepository wohnungRepository,
                            MietvertragRepository mietvertragRepository) {
        this.mieterRepository = mieterRepository;
        this.wohnungRepository = wohnungRepository;
        this.mietvertragRepository = mietvertragRepository;
    }

    /**
     * Berechnet die gesamten Mieteinnahmen.
     *
     * @return die Gesamtsumme der Mieteinnahmen.
     */
    public double getMieteinnahmen() {
        return mietvertragRepository.findAll().stream()
                .mapToDouble(Mietvertrag::getMiete)
                .sum();
    }

    /**
     * Berechnet die Statistiken über Immobilien.
     *
     * @return eine Map mit der Gesamtzahl der Immobilien und der Anzahl der vermieteten Immobilien.
     */
    public Map<String, Long> getImmobilienStats() {
        Map<String, Long> stats = new HashMap<>();
        long totalImmobilien = wohnungRepository.count();
        long vermieteteImmobilien = mietvertragRepository.findAll().stream()
                .filter(mietvertrag -> mietvertrag.getMietende() == null || mietvertrag.getMietende().isAfter(java.time.LocalDate.now()))
                .count();
        stats.put("total", totalImmobilien);
        stats.put("vermietet", vermieteteImmobilien);
        return stats;
    }

    /**
     * Berechnet die Gesamtzahl der Mieter.
     *
     * @return die Gesamtzahl der Mieter.
     */
    public long getTotalMieter() {
        return mieterRepository.count();
    }
}