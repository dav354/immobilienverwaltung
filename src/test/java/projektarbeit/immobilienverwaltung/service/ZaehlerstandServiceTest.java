package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.model.Zaehlerstand;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZaehlerstandServiceTest {

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @InjectMocks
    private ZaehlerstandService zaehlerstandService;

    @Test
    void saveOrUpdateZaehlerstand() {
        Zaehlerstand zaehlerstand = new Zaehlerstand();
        when(zaehlerstandRepository.save(zaehlerstand)).thenReturn(zaehlerstand);

        Zaehlerstand result = zaehlerstandService.saveZaehlerstand(zaehlerstand);

        assertEquals(zaehlerstand, result);
        verify(zaehlerstandRepository, times(1)).save(zaehlerstand);
    }

    @Test
    void findAllZaehlerstaende() {
        List<Zaehlerstand> expectedZaehlerstaende = Arrays.asList(new Zaehlerstand(), new Zaehlerstand());
        when(zaehlerstandRepository.findAll()).thenReturn(expectedZaehlerstaende);

        List<Zaehlerstand> result = zaehlerstandService.findAllZaehlerstaende();

        assertEquals(expectedZaehlerstaende.size(), result.size());
        verify(zaehlerstandRepository, times(1)).findAll();
    }

    @Test
    void getZaehlerstandById() {
        Long id = 1L;
        Zaehlerstand expectedZaehlerstand = new Zaehlerstand();
        when(zaehlerstandRepository.findById(id)).thenReturn(Optional.of(expectedZaehlerstand));

        Zaehlerstand result = zaehlerstandService.getZaehlerstandById(id);

        assertEquals(expectedZaehlerstand, result);
        verify(zaehlerstandRepository, times(1)).findById(id);
    }

    @Test
    void deleteZaehlerstandById() {
        Long id = 1L;

        zaehlerstandService.deleteZaehlerstand(id);

        verify(zaehlerstandRepository, times(1)).deleteById(id);
    }

    @Test
    void findZaehlerstandByWohnung() {
        Wohnung wohnung = new Wohnung();
        List<Zaehlerstand> expectedZaehlerstaende = Arrays.asList(new Zaehlerstand(), new Zaehlerstand());
        when(zaehlerstandRepository.findByWohnung(wohnung)).thenReturn(expectedZaehlerstaende);

        List<Zaehlerstand> result = zaehlerstandService.findZaehlerstandByWohnung(wohnung);

        assertEquals(expectedZaehlerstaende.size(), result.size());
        verify(zaehlerstandRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void deleteZaehlerstand() {
        Zaehlerstand zaehlerstand = new Zaehlerstand();

        zaehlerstandService.deleteZaehlerstand(zaehlerstand);

        verify(zaehlerstandRepository, times(1)).delete(zaehlerstand);
    }
}