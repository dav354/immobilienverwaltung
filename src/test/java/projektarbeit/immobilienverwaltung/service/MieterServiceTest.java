package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.MietvertragRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MieterServiceTest {

    @Mock
    private WohnungRepository wohnungRepository;

    @Mock
    private MieterRepository mieterRepository;

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @Mock
    private DokumentRepository dokumentRepository;

    @Mock
    private MietvertragRepository mietvertragRepository;

    @InjectMocks
    private MieterService mieterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWohnungCount() {
        when(wohnungRepository.count()).thenReturn(10L);
        long count = mieterService.getWohnungCount();
        assertEquals(10L, count);
    }

    @Test
    void testGetMieterCount() {
        when(mieterRepository.count()).thenReturn(5L);
        long count = mieterService.getMieterCount();
        assertEquals(5L, count);
    }

    @Test
    void testGetZaehlerstandCount() {
        when(zaehlerstandRepository.count()).thenReturn(7L);
        long count = mieterService.getZaehlerstandCount();
        assertEquals(7L, count);
    }

    @Test
    void testGetDokumentCount() {
        when(dokumentRepository.count()).thenReturn(15L);
        long count = mieterService.getDokumentCount();
        assertEquals(15L, count);
    }

    @Test
    void testFindAllWohnungen() {
        List<Wohnung> wohnungList = Arrays.asList(new Wohnung(), new Wohnung());
        when(wohnungRepository.findAll()).thenReturn(wohnungList);
        List<Wohnung> result = mieterService.findAllWohnungen();
        assertEquals(wohnungList, result);
    }

    @Test
    void testFindAllMieter_NoFilter() {
        List<Mieter> mieterList = Arrays.asList(new Mieter(), new Mieter());
        when(mieterRepository.findAll()).thenReturn(mieterList);
        List<Mieter> result = mieterService.findAllMieter(null);
        assertEquals(mieterList, result);
    }

    @Test
    void testFindAllMieter_WithFilter() {
        List<Mieter> mieterList = Arrays.asList(new Mieter(), new Mieter());
        when(mieterRepository.search("test")).thenReturn(mieterList);
        List<Mieter> result = mieterService.findAllMieter("test");
        assertEquals(mieterList, result);
    }

    @Test
    void testDeleteMieter() {
        Mieter mieter = new Mieter();
        Dokument dokument = new Dokument();
        dokument.setMieter(mieter);
        Wohnung wohnung = new Wohnung();
        List<Mietvertrag> mietvertraege = Arrays.asList(new Mietvertrag());

        mieter.setDokument(new ArrayList<>(List.of(dokument)));

        when(mietvertragRepository.findByMieter_MieterId(anyLong())).thenReturn(mietvertraege);

        mieterService.deleteMieter(mieter);

        verify(dokumentRepository, times(1)).save(dokument);
        verify(mieterRepository, times(1)).delete(mieter);
    }

    @Test
    void testSaveMieter() {
        Mieter mieter = new Mieter();
        mieterService.saveMieter(mieter);
        verify(mieterRepository, times(1)).save(mieter);
    }

    @Test
    void testEmailExists() {
        String email = "test@example.com";
        when(mieterRepository.existsByEmail(email)).thenReturn(true);
        boolean exists = mieterService.emailExists(email);
        assertTrue(exists);
    }

    @Test
    void testSaveMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag();
        when(mietvertragRepository.save(mietvertrag)).thenReturn(mietvertrag);
        Mietvertrag savedMietvertrag = mieterService.saveMietvertrag(mietvertrag);
        assertEquals(mietvertrag, savedMietvertrag);
        verify(mietvertragRepository, times(1)).save(mietvertrag);
    }

    @Test
    void testDeleteMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag();
        mieterService.deleteMietvertrag(mietvertrag);
        verify(mietvertragRepository, times(1)).delete(mietvertrag);
    }

    @Test
    void testFindById() {
        Mieter mieter = new Mieter();
        when(mieterRepository.findById(1L)).thenReturn(Optional.of(mieter));
        Mieter foundMieter = mieterService.findById(1L);
        assertEquals(mieter, foundMieter);
    }

    @Test
    void testFindById_NotFound() {
        when(mieterRepository.findById(1L)).thenReturn(Optional.empty());
        Mieter foundMieter = mieterService.findById(1L);
        assertNull(foundMieter);
    }
}