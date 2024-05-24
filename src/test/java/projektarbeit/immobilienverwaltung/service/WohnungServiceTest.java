package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.AdresseRepository;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WohnungServiceTest {

    @Mock
    private WohnungRepository wohnungRepository;

    @Mock
    private DokumentRepository dokumentRepository;

    @Mock
    private MieterRepository mieterRepository;

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private PostleitzahlService postleitzahlService;

    @Mock
    private DokumentService dokumentService;

    @InjectMocks
    private WohnungService wohnungService;

    private Wohnung wohnung;
    private Adresse adresse;
    private Postleitzahl postleitzahl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postleitzahl = new Postleitzahl("07111", "Stuttgart", Land.DE);
        adresse = new Adresse(postleitzahl, "Teststrasse", "11");
        adresse.setAdresse_id(1L);

        wohnung = new Wohnung(adresse, 200, 1900, 2, 2, true, true, true, true);
        wohnung.setWohnungId(1L);
    }

    @Test
    @Transactional
    void testFindAllWohnungen() {
        when(wohnungRepository.findAll()).thenReturn(Collections.singletonList(wohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen();
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.get(0).getAdresse().getStrasse());
    }

    @Test
    @Transactional
    void testSaveWohnung_NewAdresse() {
        when(adresseRepository.existsById(adresse.getAdresse_id())).thenReturn(false);
        when(adresseRepository.save(any(Adresse.class))).thenReturn(adresse);
        when(wohnungRepository.save(any(Wohnung.class))).thenReturn(wohnung);

        Wohnung savedWohnung = wohnungService.save(wohnung);

        assertNotNull(savedWohnung);
        assertEquals("Teststrasse", savedWohnung.getAdresse().getStrasse());
        verify(adresseRepository, times(1)).save(adresse);
        verify(wohnungRepository, times(1)).save(wohnung);
    }

    @Test
    @Transactional
    void testSaveWohnung_ExistingAdresse() {
        when(adresseRepository.existsById(adresse.getAdresse_id())).thenReturn(true);
        when(adresseRepository.findById(adresse.getAdresse_id())).thenReturn(Optional.of(adresse));
        when(wohnungRepository.save(any(Wohnung.class))).thenReturn(wohnung);

        Wohnung savedWohnung = wohnungService.save(wohnung);

        assertNotNull(savedWohnung);
        assertEquals("Teststrasse", savedWohnung.getAdresse().getStrasse());
        verify(adresseRepository, never()).save(adresse);
        verify(wohnungRepository, times(1)).save(wohnung);
    }

    @Test
    @Transactional
    void testDeleteWohnung() {
        Wohnung wohnung = new Wohnung();
        Adresse adresse = new Adresse();
        Postleitzahl postleitzahl = new Postleitzahl();
        wohnung.setAdresse(adresse);
        adresse.setPostleitzahlObj(postleitzahl);

        Mieter mieter = new Mieter();
        mieter.setWohnung(new ArrayList<>(Collections.singletonList(wohnung)));

        Dokument dokument = new Dokument();
        dokument.setWohnung(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand();
        zaehlerstand.setWohnung(wohnung);

        when(mieterRepository.findAllWithWohnungen()).thenReturn(Collections.singletonList(mieter));
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(Collections.singletonList(dokument));
        when(zaehlerstandRepository.findByWohnung(wohnung)).thenReturn(Collections.singletonList(zaehlerstand));

        wohnungService.delete(wohnung);

        verify(dokumentService, times(1)).deleteDokumenteByWohnung(wohnung);
        verify(mieterRepository, times(1)).save(mieter);
        verify(zaehlerstandRepository, times(1)).delete(zaehlerstand);
        verify(wohnungRepository, times(1)).delete(wohnung);
        verify(adresseRepository, times(1)).delete(adresse);
        verify(postleitzahlService, times(1)).deletePostleitzahlIfUnused(postleitzahl);
    }

    @Test
    void testFindDokumenteByWohnung() {
        Dokument dokument = new Dokument();
        dokument.setWohnung(wohnung);

        when(dokumentRepository.findByWohnung(any(Wohnung.class))).thenReturn(Collections.singletonList(dokument));

        List<Dokument> dokumente = wohnungService.findDokumenteByWohnung(wohnung);

        assertNotNull(dokumente);
        assertFalse(dokumente.isEmpty());
        assertEquals(1, dokumente.size());
        assertEquals(wohnung, dokumente.get(0).getWohnung());
    }
}