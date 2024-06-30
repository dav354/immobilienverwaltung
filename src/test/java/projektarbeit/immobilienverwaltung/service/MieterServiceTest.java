package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    void getWohnungCount() {
        when(wohnungRepository.count()).thenReturn(5L);

        long count = mieterService.getWohnungCount();

        assertEquals(5L, count);
        verify(wohnungRepository, times(1)).count();
    }

    @Test
    void getMieterCount() {
        when(mieterRepository.count()).thenReturn(10L);

        long count = mieterService.getMieterCount();

        assertEquals(10L, count);
        verify(mieterRepository, times(1)).count();
    }

    @Test
    void findAllWohnungen() {
        List<Wohnung> mockWohnungen = new ArrayList<>();
        mockWohnungen.add(new Wohnung());
        mockWohnungen.add(new Wohnung());
        when(wohnungRepository.findAll()).thenReturn(mockWohnungen);

        List<Wohnung> wohnungen = mieterService.findAllWohnungen();

        assertEquals(2, wohnungen.size());
        verify(wohnungRepository, times(1)).findAll();
    }

    @Test
    void getZaehlerstandCount() {
        when(zaehlerstandRepository.count()).thenReturn(7L);

        long count = mieterService.getZaehlerstandCount();

        assertEquals(7L, count);
        verify(zaehlerstandRepository, times(1)).count();
    }

    @Test
    void getDokumentCount() {
        when(dokumentRepository.count()).thenReturn(15L);

        long count = mieterService.getDokumentCount();

        assertEquals(15L, count);
        verify(dokumentRepository, times(1)).count();
    }

    @Test
    void findAllMieter_WithFilter() {
        when(mieterRepository.search("Filter")).thenReturn(new ArrayList<>());

        List<Mieter> mieterList = mieterService.findAllMieter("Filter");

        assertNotNull(mieterList);
        assertEquals(0, mieterList.size());
        verify(mieterRepository, times(1)).search("Filter");
    }

    @Test
    void findAllMieter_NoFilter() {
        List<Mieter> mockMieter = new ArrayList<>();
        mockMieter.add(new Mieter());
        mockMieter.add(new Mieter());
        when(mieterRepository.findAll()).thenReturn(mockMieter);

        List<Mieter> mieterList = mieterService.findAllMieter();

        assertEquals(2, mieterList.size());
        verify(mieterRepository, times(1)).findAll();
    }

    @Test
    void deleteMieter() {
        Mieter mieter = new Mieter();
        mieter.setMieter_id(1L);

        List<Mietvertrag> mockMietvertraege = new ArrayList<>();
        mockMietvertraege.add(new Mietvertrag(mieter, new Wohnung(), LocalDate.now(), LocalDate.now().plusYears(1), 1000, 500,1));
        when(mietvertragRepository.findByMieter_MieterId(1L)).thenReturn(mockMietvertraege);

        mieterService.deleteMieter(mieter);

        verify(mietvertragRepository, times(1)).deleteAll(mockMietvertraege);
        verify(mieterRepository, times(1)).delete(mieter);
    }

    @Test
    void saveMieter() {
        Mieter mieter = new Mieter();

        mieterService.saveMieter(mieter);

        verify(mieterRepository, times(1)).save(mieter);
    }

    @Test
    void emailExists() {
        when(mieterRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertTrue(mieterService.emailExists("test@example.com"));
        assertFalse(mieterService.emailExists("nonexistent@example.com"));

        verify(mieterRepository, times(1)).existsByEmail("test@example.com");
        verify(mieterRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    void deleteMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag();

        mieterService.deleteMietvertrag(mietvertrag);

        verify(mietvertragRepository, times(1)).delete(mietvertrag);
    }

    @Test
    void findById() {
        Mieter mieter = new Mieter();
        mieter.setMieter_id(1L);
        Optional<Mieter> optionalMieter = Optional.of(mieter);
        when(mieterRepository.findById(1L)).thenReturn(optionalMieter);

        Mieter foundMieter = mieterService.findById(1L);

        assertNotNull(foundMieter);
        assertEquals(mieter, foundMieter);

        verify(mieterRepository, times(1)).findById(1L);
    }
}