package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;

import java.util.List;

@Service
public class MieterService {

    private final MieterRepository mieterRepository;

    @Autowired
    public MieterService(MieterRepository mieterRepository) {
        this.mieterRepository = mieterRepository;
    }

    // Save or update a Mieter
    public Mieter saveOrUpdateMieter(Mieter mieter) {
        return mieterRepository.save(mieter);
    }

    // Retrieve all Mieters
    public List<Mieter> findAllMieter() {
        return mieterRepository.findAll();
    }

    // Retrieve a single Mieter by ID
    public Mieter getMieterById(Long id) {
        return mieterRepository.findById(id).orElse(null);
    }

    // Delete a Mieter by ID
    public void deleteMieter(Long id) {
        mieterRepository.deleteById(id);
    }
}