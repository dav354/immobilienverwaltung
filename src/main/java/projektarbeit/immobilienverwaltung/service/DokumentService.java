package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DokumentService {

    @Autowired
    private DokumentRepository dokumentRepository;

    // Save or update a Dokument
    public Dokument saveOrUpdateDokument(Dokument dokument) {
        return dokumentRepository.save(dokument);
    }

    // Retrieve all Dokuments
    public List<Dokument> findAllDokuments() {
        return dokumentRepository.findAll();
    }

    // Retrieve a single Dokument by ID
    public Dokument getDokumentById(Long id) {
        return dokumentRepository.findById(id).orElse(null);
    }

    // Delete a Dokument by ID
    public void deleteDokument(Long id) {
        dokumentRepository.deleteById(id);
    }
}