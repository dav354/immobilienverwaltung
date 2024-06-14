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

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private MieterService mieterService;

    @Autowired
    private WohnungService wohnungService;

    @Autowired
    private MietvertragService mietvertragService;

    @GetMapping("/mieteinnahmen")
    public double getMieteinnahmen() {
        return mietvertragService.findAll().stream()
                .mapToDouble(Mietvertrag::getMiete)
                .sum();
    }

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

    @GetMapping("/mieter")
    public long getTotalMieter() {
        return mieterService.getMieterCount();
    }
}
