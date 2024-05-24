package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ZaehlerstandServiceTest {

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @InjectMocks
    private ZaehlerstandService zaehlerstandService;

    private Zaehlerstand zaehlerstand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        zaehlerstand = new Zaehlerstand();
        zaehlerstand.setZaehlerstandId(1L);
        zaehlerstand.setAblesedatum(LocalDate.of(2024,1,1));
        zaehlerstand.setAblesewert(123.45);
    }

    @Test
    void testSaveOrUpdateZaehlerstand() {
        when(zaehlerstandRepository.save(any(Zaehlerstand.class))).thenReturn(zaehlerstand);

        Zaehlerstand savedZaehlerstand = zaehlerstandService.saveOrUpdateZaehlerstand(zaehlerstand);

        assertNotNull(savedZaehlerstand);
        assertEquals(1L, savedZaehlerstand.getZaehlerstandId());
        verify(zaehlerstandRepository, times(1)).save(zaehlerstand);
    }

    @Test
    void testFindAllZaehlerstaende() {
        Zaehlerstand zaehlerstand2 = new Zaehlerstand();
        zaehlerstand2.setZaehlerstandId(2L);
        zaehlerstand2.setAblesedatum(LocalDate.of(2024,1,1));
        zaehlerstand2.setAblesewert(234.56);

        List<Zaehlerstand> zaehlerstaende = Arrays.asList(zaehlerstand, zaehlerstand2);
        when(zaehlerstandRepository.findAll()).thenReturn(zaehlerstaende);

        List<Zaehlerstand> result = zaehlerstandService.findAllZaehlerstaende();

        assertEquals(2, result.size());
        verify(zaehlerstandRepository, times(1)).findAll();
    }

    @Test
    void testGetZaehlerstandById() {
        when(zaehlerstandRepository.findById(anyLong())).thenReturn(Optional.of(zaehlerstand));

        Zaehlerstand result = zaehlerstandService.getZaehlerstandById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getZaehlerstandId());
        verify(zaehlerstandRepository, times(1)).findById(1L);
    }

    @Test
    void testGetZaehlerstandById_NotFound() {
        when(zaehlerstandRepository.findById(anyLong())).thenReturn(Optional.empty());

        Zaehlerstand result = zaehlerstandService.getZaehlerstandById(1L);

        assertNull(result);
        verify(zaehlerstandRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteZaehlerstand() {
        doNothing().when(zaehlerstandRepository).deleteById(anyLong());

        zaehlerstandService.deleteZaehlerstand(1L);

        verify(zaehlerstandRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllZaehlerstaende_EmptyList() {
        when(zaehlerstandRepository.findAll()).thenReturn(Collections.emptyList());

        List<Zaehlerstand> result = zaehlerstandService.findAllZaehlerstaende();

        assertTrue(result.isEmpty());
        verify(zaehlerstandRepository, times(1)).findAll();
    }

    @Test
    void testSaveOrUpdateZaehlerstand_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            zaehlerstandService.saveOrUpdateZaehlerstand(null);
        });
    }

    @Test
    void testGetZaehlerstandById_ExceptionHandling() {
        when(zaehlerstandRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            zaehlerstandService.getZaehlerstandById(1L);
        });
    }

    @Test
    void testDeleteZaehlerstand_ExceptionHandling() {
        doThrow(new RuntimeException("Database error")).when(zaehlerstandRepository).deleteById(anyLong());

        assertThrows(RuntimeException.class, () -> {
            zaehlerstandService.deleteZaehlerstand(1L);
        });
    }

    @Test
    void testSaveOrUpdateZaehlerstand_BoundaryValues() {
        zaehlerstand.setAblesewert(Double.MAX_VALUE);
        when(zaehlerstandRepository.save(any(Zaehlerstand.class))).thenReturn(zaehlerstand);

        Zaehlerstand savedZaehlerstand = zaehlerstandService.saveOrUpdateZaehlerstand(zaehlerstand);

        assertEquals(Double.MAX_VALUE, savedZaehlerstand.getAblesewert());
        verify(zaehlerstandRepository, times(1)).save(zaehlerstand);
    }
}