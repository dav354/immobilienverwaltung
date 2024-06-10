package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static projektarbeit.immobilienverwaltung.model.Land.*;
/*
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
    private DokumentService dokumentService;

    @InjectMocks
    private WohnungService wohnungService;

    private Wohnung wohnung;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        wohnung = new Wohnung("07111", "Stuttgart", "83783", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnung.setWohnung_id(1L);
    }

    @Test
    @Transactional
    void testFindAllWohnungen() {
        when(wohnungRepository.findAll()).thenReturn(Collections.singletonList(wohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen();
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.getFirst().getStrasse());
    }

    @Test
    @Transactional
    void testSaveWohnung() {
        when(wohnungRepository.save(any(Wohnung.class))).thenReturn(wohnung);

        Wohnung savedWohnung = wohnungService.save(wohnung);

        assertNotNull(savedWohnung);
        assertEquals("Teststrasse", savedWohnung.getStrasse());
        verify(wohnungRepository, times(1)).save(wohnung);
    }

    @Test
    @Transactional
    void testDeleteWohnung() {
        Wohnung wohnung = new Wohnung("07111", "Stuttgart", "86768", "Teststrasse", DE, 200, 1900, 2, 2, true, true, true, true, null, null);
        wohnung.setWohnung_id(1L);

        Mieter mieter = new Mieter();
        mieter.setWohnung(new ArrayList<>(Collections.singletonList(wohnung)));

        Dokument dokument = new Dokument();
        dokument.setWohnung(wohnung);

        Zaehlerstand zaehlerstand = new Zaehlerstand();
        zaehlerstand.setWohnung(wohnung);

        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(Collections.singletonList(dokument));
        when(zaehlerstandRepository.findByWohnung(wohnung)).thenReturn(Collections.singletonList(zaehlerstand));

        wohnungService.delete(wohnung);

        verify(dokumentService, times(1)).deleteDokumenteByWohnung(wohnung);
        verify(mieterRepository, times(1)).save(mieter);
        verify(zaehlerstandRepository, times(1)).delete(zaehlerstand);
        verify(wohnungRepository, times(1)).delete(wohnung);
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
        assertEquals(wohnung, dokumente.getFirst().getWohnung());
    }

    @Test
    void testFindAllWohnungen_NoFilter() {
        when(wohnungRepository.findAll()).thenReturn(Collections.singletonList(wohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen(null);
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.getFirst().getStrasse());

        wohnungen = wohnungService.findAllWohnungen("");
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.getFirst().getStrasse());
    }

    @Test
    void testFindAllWohnungen_WithFilter() {
        Wohnung matchingWohnung = new Wohnung("07111", "MatchingStadt", "87482", "MatchingStrasse", DE, 250, 2000, 3, 2, true, false, true, false, null, null);
        matchingWohnung.setWohnung_id(2L);

        when(wohnungRepository.findAll()).thenReturn(Collections.singletonList(matchingWohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen("MatchingStrasse");
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("MatchingStrasse", wohnungen.getFirst().getStrasse());
    }

    @Test
    void testFindAllWohnungen_NoMatchingFilter() {
        when(wohnungRepository.findAll()).thenReturn(Collections.emptyList());

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen("NonExistingStrasse");
        assertNotNull(wohnungen);
        assertTrue(wohnungen.isEmpty());
    }
}

 */