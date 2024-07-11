package projektarbeit.immobilienverwaltung.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.List;

/**
 * Service-Klasse zur Verwaltung von Zaehlerstand-Entitäten.
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
public class ZaehlerstandService {

    private final ZaehlerstandRepository zaehlerstandRepository;

    /**
     * Konstruktor für ZaehlerstandService mit dem angegebenen ZaehlerstandRepository.
     *
     * @param zaehlerstandRepository das Repository für Zaehlerstand-Entitäten
     */
    @Autowired
    public ZaehlerstandService(ZaehlerstandRepository zaehlerstandRepository) {
        this.zaehlerstandRepository = zaehlerstandRepository;
    }

    /**
     * Speichert oder aktualisiert eine Zaehlerstand-Entität.
     *
     * @param zaehlerstand die zu speichernde oder zu aktualisierende Zaehlerstand-Entität
     * @return die gespeicherte oder aktualisierte Zaehlerstand-Entität
     */
    public Zaehlerstand saveZaehlerstand(@Valid Zaehlerstand zaehlerstand) {
        return zaehlerstandRepository.save(zaehlerstand);
    }

    /**
     * Ruft alle Zaehlerstand-Entitäten ab.
     *
     * @return eine Liste aller Zaehlerstand-Entitäten
     */
    public List<Zaehlerstand> findAllZaehlerstaende() {
        return zaehlerstandRepository.findAll();
    }

    /**
     * Ruft eine Zaehlerstand-Entität anhand ihrer ID ab.
     *
     * @param id die ID der abzurufenden Zaehlerstand-Entität
     * @return die Zaehlerstand-Entität mit der angegebenen ID oder null, wenn sie nicht gefunden wurde
     */
    public Zaehlerstand getZaehlerstandById(Long id) {
        return zaehlerstandRepository.findById(id).orElse(null);
    }

    /**
     * Löscht eine Zaehlerstand-Entität anhand ihrer ID.
     *
     * @param id die ID der zu löschenden Zaehlerstand-Entität
     */
    public void deleteZaehlerstand(Long id) {
        zaehlerstandRepository.deleteById(id);
    }

    /**
     * Findet und gibt eine Liste von Zaehlerstand-Entitäten zurück, die mit einer bestimmten Wohnung verknüpft sind.
     *
     * @param wohnung Die Wohnung-Entität, für die die zugehörigen Zaehlerstände gefunden werden sollen.
     * @return Eine Liste von Zaehlerstand-Entitäten, die mit der angegebenen Wohnung verknüpft sind.
     */
    public List<Zaehlerstand> findZaehlerstandByWohnung(Wohnung wohnung) {
        return zaehlerstandRepository.findByWohnung(wohnung);
    }

    /**
     * Löscht eine Zaehlerstand-Entität.
     *
     * @param zaehlerstand die zu löschende Zaehlerstand-Entität
     */
    public void deleteZaehlerstand(Zaehlerstand zaehlerstand) {
        zaehlerstandRepository.delete(zaehlerstand);
    }
}