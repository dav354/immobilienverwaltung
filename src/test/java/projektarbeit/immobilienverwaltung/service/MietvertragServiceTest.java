package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.MietvertragRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MietvertragServiceTest {

    @Mock
    private MietvertragRepository mietvertragRepository;

    @InjectMocks
    private MietvertragService mietvertragService;

    @Test
    void saveMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag();

        mietvertragService.saveMietvertrag(mietvertrag);

        verify(mietvertragRepository, times(1)).save(mietvertrag);
    }

    @Test
    void deleteMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag();

        mietvertragService.deleteMietvertrag(mietvertrag);

        verify(mietvertragRepository, times(1)).delete(mietvertrag);
    }

    @Test
    void findAll() {
        Mietvertrag mietvertrag1 = new Mietvertrag();
        Mietvertrag mietvertrag2 = new Mietvertrag();
        when(mietvertragRepository.findAll()).thenReturn(Arrays.asList(mietvertrag1, mietvertrag2));

        List<Mietvertrag> result = mietvertragService.findAll();

        assertEquals(2, result.size());
        verify(mietvertragRepository, times(1)).findAll();
    }

    @Test
    void findByMieter() {
        Long mieterId = 1L;
        Mietvertrag mietvertrag1 = new Mietvertrag();
        Mietvertrag mietvertrag2 = new Mietvertrag();
        when(mietvertragRepository.findByMieter_MieterId(mieterId)).thenReturn(Arrays.asList(mietvertrag1, mietvertrag2));

        List<Mietvertrag> result = mietvertragService.findByMieter(mieterId);

        assertEquals(2, result.size());
        verify(mietvertragRepository, times(1)).findByMieter_MieterId(mieterId);
    }

    @Test
    void findByWohnung() {
        Wohnung wohnung = new Wohnung();
        Mietvertrag mietvertrag = new Mietvertrag();
        when(mietvertragRepository.findByWohnung(wohnung)).thenReturn(mietvertrag);

        Mietvertrag result = mietvertragService.findByWohnung(wohnung);

        assertEquals(mietvertrag, result);
        verify(mietvertragRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void findMieterByWohnung_MietvertragExists() {
        Wohnung wohnung = new Wohnung();
        Mieter mieter = new Mieter();
        Mietvertrag mietvertrag = new Mietvertrag();
        mietvertrag.setMieter(mieter);
        when(mietvertragRepository.findByWohnung(wohnung)).thenReturn(mietvertrag);

        Mieter result = mietvertragService.findMieterByWohnung(wohnung);

        assertEquals(mieter, result);
        verify(mietvertragRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void findMieterByWohnung_NoMietvertragExists() {
        Wohnung wohnung = new Wohnung();
        when(mietvertragRepository.findByWohnung(wohnung)).thenReturn(null);

        Mieter result = mietvertragService.findMieterByWohnung(wohnung);

        assertNull(result);
        verify(mietvertragRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void createAndSaveMietvertrag() {
        Mieter mieter = new Mieter();
        Wohnung wohnung = new Wohnung();
        LocalDate mietbeginn = LocalDate.of(2024, 7, 1);
        LocalDate mietende = LocalDate.of(2024, 12, 31);
        double miete = 1000.0;
        double kaution = 2000.0;
        int anzahlBewohner = 2;

        mietvertragService.createAndSaveMietvertrag(mieter, wohnung, mietbeginn, mietende, miete, kaution, anzahlBewohner);

        ArgumentCaptor<Mietvertrag> captor = ArgumentCaptor.forClass(Mietvertrag.class);
        verify(mietvertragRepository, times(1)).save(captor.capture());

        Mietvertrag savedMietvertrag = captor.getValue();
        assertEquals(mieter, savedMietvertrag.getMieter());
        assertEquals(wohnung, savedMietvertrag.getWohnung());
        assertEquals(mietbeginn, savedMietvertrag.getMietbeginn());
        assertEquals(mietende, savedMietvertrag.getMietende());
        assertEquals(miete, savedMietvertrag.getMiete());
        assertEquals(kaution, savedMietvertrag.getKaution());
        assertEquals(anzahlBewohner, savedMietvertrag.getAnzahlBewohner());
    }
}