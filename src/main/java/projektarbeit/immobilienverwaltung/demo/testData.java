package projektarbeit.immobilienverwaltung.demo;

import projektarbeit.immobilienverwaltung.model.Eigentum;
import projektarbeit.immobilienverwaltung.repository.EigentumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class testData implements CommandLineRunner {

    @Autowired
    private EigentumRepository eigentumRepository;

    @Override
    public void run(String... args) throws Exception {
        loadEigentumData();
    }

    private void loadEigentumData() {
        if (eigentumRepository.count() == 0) { // Nur laden, wenn noch keine Daten vorhanden sind
            eigentumRepository.save(new Eigentum("123 Main St", "Wohnung", 10, 1200, 5, 1990, 2, 3, true, false, true, true));
            eigentumRepository.save(new Eigentum("456 Elm St", "Haus", 1, 200, 1, 1985, 1, 2, false, true, false, false));
        }
    }
}