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
import projektarbeit.immobilienverwaltung.repository.MieterRepository;
import projektarbeit.immobilienverwaltung.repository.WohnungRepository;
import projektarbeit.immobilienverwaltung.repository.ZaehlerstandRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
class MieterServiceTest {

    @Mock
    private WohnungRepository wohnungRepository;

    @Mock
    private MieterRepository mieterRepository;

    @Mock
    private ZaehlerstandRepository zaehlerstandRepository;

    @Mock
    private DokumentRepository dokumentRepository;

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
        wohnung.setMieter(mieter);

        // Use mutable lists
        mieter.setDokument(new ArrayList<>(List.of(dokument)));
        mieter.setWohnung(new ArrayList<>(List.of(wohnung)));


        when(dokumentRepository.save(any(Dokument.class))).thenReturn(dokument);
        when(wohnungRepository.save(any(Wohnung.class))).thenReturn(wohnung);

        mieterService.deleteMieter(mieter);

        verify(dokumentRepository, times(1)).save(dokument);
        verify(wohnungRepository, times(1)).save(wohnung);
        verify(mieterRepository, times(1)).delete(mieter);
    }

    @Test
    void testSaveMieter() {
        Mieter mieter = new Mieter();
        mieterService.saveMieter(mieter);
        verify(mieterRepository, times(1)).save(mieter);
    }

    // TODO fix counting error
    /*
    @Test
    void testSaveWohnungToMieter() {
        Mieter mieter = new Mieter();
        Wohnung wohnung1 = new Wohnung();
        Wohnung wohnung2 = new Wohnung();
        List<Wohnung> wohnungen = Arrays.asList(wohnung1, wohnung2);

        // Ensure the mieter starts with no wohnung assigned
        assertTrue(mieter.getWohnung().isEmpty());

        mieterService.saveWohnungToMieter(mieter, wohnungen);

        assertEquals(2, mieter.getWohnung().size());
        verify(mieterRepository, times(1)).save(mieter);
        verify(wohnungRepository, times(1)).saveAll(wohnungen);
    }

    @Test
    void testRemoveWohnungFromMieter() {
        Mieter mieter = new Mieter();
        Wohnung wohnung1 = new Wohnung();
        Wohnung wohnung2 = new Wohnung();

        // Use mutable lists
        mieter.setWohnung(new ArrayList<>(List.of(wohnung1, wohnung2)));
        List<Wohnung> wohnungen = Arrays.asList(wohnung1, wohnung2);

        mieterService.removeWohnungFromMieter(mieter, wohnungen);

        assertEquals(0, mieter.getWohnung().size());
        verify(mieterRepository, times(1)).save(mieter);
        verify(wohnungRepository, times(1)).saveAll(wohnungen);
    }
}
  */