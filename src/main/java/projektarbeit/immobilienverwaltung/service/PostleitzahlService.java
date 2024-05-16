package projektarbeit.immobilienverwaltung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Postleitzahl;
import projektarbeit.immobilienverwaltung.repository.PostleitzahlRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostleitzahlService {

    @Autowired
    private PostleitzahlRepository postleitzahlRepository;

    // Save or update a Postleitzahl
    public Postleitzahl saveOrUpdatePostleitzahl(Postleitzahl postleitzahl) {
        return postleitzahlRepository.save(postleitzahl);
    }

    // Retrieve all Postleitzahlen
    public List<Postleitzahl> findAllPostleitzahlen() {
        return postleitzahlRepository.findAll();
    }

    // Retrieve a single Postleitzahl by ID
    public Postleitzahl getPostleitzahlById(int id) {
        return postleitzahlRepository.findById(id).orElse(null);
    }

    // Delete a Postleitzahl by ID
    public void deletePostleitzahl(int id) {
        postleitzahlRepository.deleteById(id);
    }
}