package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DokumentServiceTest {

    @Mock
    private DokumentRepository dokumentRepository;

    @InjectMocks
    private DokumentService dokumentService;

    @Test
    void findAllDokumente() {
        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(new Dokument());
        when(dokumentRepository.findAll()).thenReturn(dokumentList);

        List<Dokument> result = dokumentService.findAllDokumente();
        assertEquals(1, result.size());
        verify(dokumentRepository, times(1)).findAll();
    }

    @Test
    void findAllDokumenteWhenEmpty() {
        when(dokumentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Dokument> result = dokumentService.findAllDokumente();
        assertTrue(result.isEmpty());
        verify(dokumentRepository, times(1)).findAll();
    }

    @Test
    void deleteDokumenteByWohnung() {
        Wohnung wohnung = new Wohnung();
        Dokument dokumentWithMieter = new Dokument();
        dokumentWithMieter.setWohnung(wohnung);
        dokumentWithMieter.setMieter(new Mieter());

        Dokument dokumentWithoutMieter = new Dokument();
        dokumentWithoutMieter.setWohnung(wohnung);

        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(dokumentWithMieter);
        dokumentList.add(dokumentWithoutMieter);

        when(dokumentRepository.findByWohnung(any(Wohnung.class))).thenReturn(dokumentList);

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, times(1)).delete(dokumentWithoutMieter);
        verify(dokumentRepository, times(1)).save(dokumentWithMieter);
        assertNull(dokumentWithoutMieter.getWohnung());
        assertNull(dokumentWithMieter.getWohnung());
    }

    @Test
    void deleteDokumenteByWohnungWhenWohnungIsNull() {
        assertThrows(NullPointerException.class, () -> dokumentService.deleteDokumenteByWohnung(null));
    }

    @Test
    void findDokumenteByWohnung() {
        Wohnung wohnung = new Wohnung();
        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(new Dokument());
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(dokumentList);

        List<Dokument> result = dokumentService.findDokumenteByWohnung(wohnung);
        assertEquals(1, result.size());
        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void findDokumenteByWohnungWhenEmpty() {
        Wohnung wohnung = new Wohnung();
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(new ArrayList<>());

        List<Dokument> result = dokumentService.findDokumenteByWohnung(wohnung);
        assertTrue(result.isEmpty());
        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    void findDokumenteByMieter() {
        Mieter mieter = new Mieter();
        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(new Dokument());
        when(dokumentRepository.findByMieter(mieter)).thenReturn(dokumentList);

        List<Dokument> result = dokumentService.findDokumenteByMieter(mieter);
        assertEquals(1, result.size());
        verify(dokumentRepository, times(1)).findByMieter(mieter);
    }

    @Test
    void findDokumenteByMieterWhenEmpty() {
        Mieter mieter = new Mieter();
        when(dokumentRepository.findByMieter(mieter)).thenReturn(new ArrayList<>());

        List<Dokument> result = dokumentService.findDokumenteByMieter(mieter);
        assertTrue(result.isEmpty());
        verify(dokumentRepository, times(1)).findByMieter(mieter);
    }

    @Test
    void deleteDokumenteByWohnungWhenEmptyList() {
        Wohnung wohnung = new Wohnung();
        when(dokumentRepository.findByWohnung(any(Wohnung.class))).thenReturn(new ArrayList<>());

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, never()).delete(any(Dokument.class));
        verify(dokumentRepository, never()).save(any(Dokument.class));
    }

    @Test
    void deleteDokumenteByWohnungWhenAllHaveMieter() {
        Wohnung wohnung = new Wohnung();
        Dokument dokumentWithMieter1 = new Dokument();
        dokumentWithMieter1.setWohnung(wohnung);
        dokumentWithMieter1.setMieter(new Mieter());

        Dokument dokumentWithMieter2 = new Dokument();
        dokumentWithMieter2.setWohnung(wohnung);
        dokumentWithMieter2.setMieter(new Mieter());

        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(dokumentWithMieter1);
        dokumentList.add(dokumentWithMieter2);

        when(dokumentRepository.findByWohnung(any(Wohnung.class))).thenReturn(dokumentList);

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, never()).delete(any(Dokument.class));
        verify(dokumentRepository, times(2)).save(any(Dokument.class));
        assertNull(dokumentWithMieter1.getWohnung());
        assertNull(dokumentWithMieter2.getWohnung());
    }

    @Test
    void deleteDokumenteByWohnungWhenAllHaveNoMieter() {
        Wohnung wohnung = new Wohnung();
        Dokument dokumentWithoutMieter1 = new Dokument();
        dokumentWithoutMieter1.setWohnung(wohnung);

        Dokument dokumentWithoutMieter2 = new Dokument();
        dokumentWithoutMieter2.setWohnung(wohnung);

        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(dokumentWithoutMieter1);
        dokumentList.add(dokumentWithoutMieter2);

        when(dokumentRepository.findByWohnung(any(Wohnung.class))).thenReturn(dokumentList);

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, times(2)).delete(any(Dokument.class));
        verify(dokumentRepository, never()).save(any(Dokument.class));
    }

    @Test
    void findDokumenteByWohnungWithPagination() {
        Wohnung wohnung = new Wohnung();
        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.add(new Dokument());
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(dokumentList);

        List<Dokument> result = dokumentService.findDokumenteByWohnung(wohnung);
        assertEquals(1, result.size());
        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
    }
}