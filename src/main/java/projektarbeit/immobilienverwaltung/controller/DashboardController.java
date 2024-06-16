package projektarbeit.immobilienverwaltung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.service.MieterService;
import projektarbeit.immobilienverwaltung.service.MietvertragService;
import projektarbeit.immobilienverwaltung.service.WohnungService;

import java.util.HashMap;
import java.util.Map;

/**
 * Der DashboardController stellt REST-APIs zur Verfügung, die statistische Informationen
 * für das Dashboard der Immobilienverwaltungsanwendung liefern.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private MieterService mieterService;

    @Autowired
    private WohnungService wohnungService;

    @Autowired
    private MietvertragService mietvertragService;

    /**
     * API-Endpunkt zum Abrufen der gesamten Mieteinnahmen.
     *
     * @return die Gesamtsumme der Mieteinnahmen.
     */
    @GetMapping("/mieteinnahmen")
    public double getMieteinnahmen() {
        return mietvertragService.findAll().stream()
                .mapToDouble(Mietvertrag::getMiete)
                .sum();
    }

    /**
     * API-Endpunkt zum Abrufen von Statistiken über Immobilien.
     *
     * @return eine Map mit der Gesamtzahl der Immobilien und der Anzahl der vermieteten Immobilien.
     */
    @GetMapping("/immobilien")
    public Map<String, Long> getImmobilienStats() {
        Map<String, Long> stats = new HashMap<>();
        long totalImmobilien = wohnungService.findAllWohnungen().size();
        long vermieteteImmobilien = mietvertragService.findAll().stream()
                .filter(mietvertrag -> mietvertrag.getMietende() == null || mietvertrag.getMietende().isAfter(java.time.LocalDate.now()))
                .count();
        stats.put("total", totalImmobilien);
        stats.put("vermietet", vermieteteImmobilien);
        return stats;
    }

    /**
     * API-Endpunkt zum Abrufen der Gesamtzahl der Mieter.
     *
     * @return die Gesamtzahl der Mieter.
     */
    @GetMapping("/mieter")
    public long getTotalMieter() {
        return mieterService.getMieterCount();
    }
}
