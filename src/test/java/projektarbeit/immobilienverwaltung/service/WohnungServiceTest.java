package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import projektarbeit.immobilienverwaltung.model.*;
import projektarbeit.immobilienverwaltung.repository.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static projektarbeit.immobilienverwaltung.model.Land.*;

class WohnungServiceTest {

    @Mock
    private WohnungRepository wohnungRepository;

    @Mock
    private DokumentRepository dokumentRepository;

    @Mock
    private MieterRepository mieterRepository;

    @Mock
    private MietvertragRepository mietvertragRepository;

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @Mock
    private GeocodingService geocodingService;

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
        assertEquals("Teststrasse", wohnungen.get(0).getStrasse());
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

    @Test
    void testFindAllWohnungen_NoFilter() {
        when(wohnungRepository.findAll()).thenReturn(Collections.singletonList(wohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen(null);
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.get(0).getStrasse());

        wohnungen = wohnungService.findAllWohnungen("");
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("Teststrasse", wohnungen.get(0).getStrasse());
    }

    @Test
    void testFindAllWohnungen_WithFilter() {
        Wohnung matchingWohnung = new Wohnung("07111", "MatchingStadt", "87482", "MatchingStrasse", DE, 250, 2000, 3, 2, true, false, true, false, null, null);
        matchingWohnung.setWohnung_id(2L);

        when(wohnungRepository.search("MatchingStrasse")).thenReturn(Collections.singletonList(matchingWohnung));

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen("MatchingStrasse");
        assertNotNull(wohnungen);
        assertFalse(wohnungen.isEmpty());
        assertEquals(1, wohnungen.size());
        assertEquals("MatchingStrasse", wohnungen.get(0).getStrasse());
    }

    @Test
    void testFindAllWohnungen_NoMatchingFilter() {
        when(wohnungRepository.search("NonExistingStrasse")).thenReturn(Collections.emptyList());

        List<Wohnung> wohnungen = wohnungService.findAllWohnungen("NonExistingStrasse");
        assertNotNull(wohnungen);
        assertTrue(wohnungen.isEmpty());
    }

    @Test
    void testFindWohnungById() {
        when(wohnungRepository.findById(1L)).thenReturn(Optional.of(wohnung));

        Wohnung foundWohnung = wohnungService.findWohnungById(1L);

        assertNotNull(foundWohnung);
        assertEquals(1L, foundWohnung.getWohnung_id());
    }

    @Test
    void testFindWohnungById_NotFound() {
        when(wohnungRepository.findById(1L)).thenReturn(Optional.empty());

        Wohnung foundWohnung = wohnungService.findWohnungById(1L);

        assertNull(foundWohnung);
    }

    @Test
    void testFindWohnungenWithHierarchy() {
        Wohnung wohnung1 = new Wohnung("07111", "MatchingStadt", "87482", "MatchingStrasse", DE, 250, 2000, 3, 2, true, false, true, false, null, null);
        Wohnung wohnung2 = new Wohnung("07111", "MatchingStadt", "87482", "MatchingStrasse", DE, 150, 1995, 2, 1, true, true, false, true, null, null);
        when(wohnungRepository.findAll()).thenReturn(Arrays.asList(wohnung1, wohnung2));

        List<Wohnung> result = wohnungService.findWohnungenWithHierarchy("MatchingStrasse");

        assertNotNull(result);
        assertEquals(1, result.size());
        Wohnung header = result.get(0);
        assertTrue(header.isHeader());
        assertEquals(2, header.getSubWohnungen().size());
    }

    @Test
    void testFindWohnungenWithoutMietvertrag() {
        Wohnung wohnungWithMietvertrag = new Wohnung();
        Wohnung wohnungWithoutMietvertrag = new Wohnung();
        Mietvertrag mietvertrag = new Mietvertrag();
        mietvertrag.setWohnung(wohnungWithMietvertrag);

        when(wohnungRepository.findAll()).thenReturn(Arrays.asList(wohnungWithMietvertrag, wohnungWithoutMietvertrag));
        when(mietvertragRepository.findAll()).thenReturn(Collections.singletonList(mietvertrag));

        List<Wohnung> result = wohnungService.findWohnungenWithoutMietvertrag();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(wohnungWithoutMietvertrag, result.get(0));
    }

    @Test
    void testFindAvailableWohnungen() {
        Wohnung wohnungWithMietvertrag = new Wohnung();
        Wohnung wohnungWithoutMietvertrag = new Wohnung();
        Mietvertrag mietvertrag = new Mietvertrag();
        mietvertrag.setWohnung(wohnungWithMietvertrag);

        when(mietvertragRepository.findAll()).thenReturn(Collections.singletonList(mietvertrag));

        List<Wohnung> result = wohnungService.findAvailableWohnungen();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(wohnungWithMietvertrag, result.get(0));
    }

    @Test
    void testFindZaehlerstandByWohnung() {
        Zaehlerstand zaehlerstand = new Zaehlerstand();
        zaehlerstand.setWohnung(wohnung);

        when(zaehlerstandRepository.findByWohnung(any(Wohnung.class))).thenReturn(Collections.singletonList(zaehlerstand));

        List<Zaehlerstand> result = wohnungService.findZaehlerstandByWohnung(wohnung);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindZaehlerstandByWohnung_WithNullWohnung() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> wohnungService.findZaehlerstandByWohnung(null),
                "Expected findZaehlerstandByWohnung to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Wohnung cannot be null"));
    }

    @Test
    void testSaveWohnung_WithGeocoding() throws Exception {
        Wohnung wohnung = new Wohnung("Teststrasse", "123", "12345", "Teststadt", DE, 100, 1990, 2, 2, true, false, false, false, null, null);
        double[] coordinates = {52.5200, 13.4050};
        when(geocodingService.getCoordinates(anyString())).thenReturn(coordinates);

        Wohnung savedWohnung = wohnungService.save(wohnung);

        assertNotNull(savedWohnung);
        assertEquals(52.5200, savedWohnung.getLatitude());
        assertEquals(13.4050, savedWohnung.getLongitude());
        verify(wohnungRepository, times(1)).save(wohnung);
    }

    @Test
    void testSaveWohnung_WithGeocodingException() throws Exception {
        Wohnung wohnung = new Wohnung("Teststrasse", "123", "12345", "Teststadt", DE, 100, 1990, 2, 2, true, false, false, false, null, null);
        when(geocodingService.getCoordinates(anyString())).thenThrow(new IOException());

        Wohnung savedWohnung = wohnungService.save(wohnung);

        assertNotNull(savedWohnung);
        assertNull(savedWohnung.getLatitude());
        assertNull(savedWohnung.getLongitude());
        verify(wohnungRepository, times(1)).save(wohnung);
    }
}