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

    public List<Mieter> findAllMieter() {
        return mieterRepository.findAll();
    }
}