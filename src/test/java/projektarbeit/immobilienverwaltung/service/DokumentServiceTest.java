package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projektarbeit.immobilienverwaltung.model.Dokument;
import projektarbeit.immobilienverwaltung.model.Mieter;
import projektarbeit.immobilienverwaltung.model.Wohnung;
import projektarbeit.immobilienverwaltung.repository.DokumentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DokumentServiceTest {

    @Mock
    private DokumentRepository dokumentRepository;

    @InjectMocks
    private DokumentService dokumentService;

    private Wohnung wohnung;
    private Dokument dokument1;
    private Dokument dokument2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        wohnung = new Wohnung();
        wohnung.setWohnung_id(1L);

        dokument1 = new Dokument();
        dokument1.setDokument_id(1L);
        dokument1.setWohnung(wohnung);

        dokument2 = new Dokument();
        dokument2.setDokument_id(2L);
        dokument2.setWohnung(wohnung);
        dokument2.setMieter(null); // Kein zugeordneter Mieter
    }

    @Test
    void testFindAllDokumente() {
        List<Dokument> dokumente = new ArrayList<>();
        dokumente.add(dokument1);
        dokumente.add(dokument2);

        when(dokumentRepository.findAll()).thenReturn(dokumente);

        List<Dokument> result = dokumentService.findAllDokumente();

        assertEquals(2, result.size());
        verify(dokumentRepository, times(1)).findAll();
    }

    @Test
    void testDeleteDokumenteByWohnung_WithNullWohnung() {
        assertThrows(NullPointerException.class, () -> {
            dokumentService.deleteDokumenteByWohnung(null);
        }, "Wohnung ist null");
    }

    @Test
    void testFindAllDokumente_EmptyList() {
        when(dokumentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Dokument> result = dokumentService.findAllDokumente();
        assertTrue(result.isEmpty());
        verify(dokumentRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteDokumenteByWohnung_WithMieter() {
        dokument1.setMieter(new Mieter());
        List<Dokument> dokumente = Arrays.asList(dokument1, dokument2);
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(dokumente);

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, times(1)).save(dokument1);
        verify(dokumentRepository, times(1)).delete(dokument2);
        assertNull(dokument1.getWohnung());
    }

    @Test
    public void testDeleteDokumenteByWohnung_WithoutMieter() {
        List<Dokument> dokumente = Arrays.asList(dokument1, dokument2);
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(dokumente);

        dokumentService.deleteDokumenteByWohnung(wohnung);

        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
        verify(dokumentRepository, never()).save(any(Dokument.class));
        verify(dokumentRepository, times(2)).delete(any(Dokument.class));
    }

    @Test
    public void testFindDokumenteByWohnung() {
        List<Dokument> dokumente = Arrays.asList(dokument1, dokument2);
        when(dokumentRepository.findByWohnung(wohnung)).thenReturn(dokumente);

        List<Dokument> result = dokumentService.findDokumenteByWohnung(wohnung);

        assertEquals(2, result.size());
        verify(dokumentRepository, times(1)).findByWohnung(wohnung);
    }

    @Test
    public void testFindDokumenteByMieter() {
        Mieter mieter = new Mieter();
        dokument1.setMieter(mieter);
        dokument2.setMieter(mieter);
        List<Dokument> dokumente = Arrays.asList(dokument1, dokument2);
        when(dokumentRepository.findByMieter(mieter)).thenReturn(dokumente);

        List<Dokument> result = dokumentService.findDokumenteByMieter(mieter);

        assertEquals(2, result.size());
        verify(dokumentRepository, times(1)).findByMieter(mieter);
    }

    @Test
    public void testFindDokumenteByMieter_NoDokumente() {
        Mieter mieter = new Mieter();
        when(dokumentRepository.findByMieter(mieter)).thenReturn(new ArrayList<>());

        List<Dokument> result = dokumentService.findDokumenteByMieter(mieter);

        assertTrue(result.isEmpty());
        verify(dokumentRepository, times(1)).findByMieter(mieter);
    }
}