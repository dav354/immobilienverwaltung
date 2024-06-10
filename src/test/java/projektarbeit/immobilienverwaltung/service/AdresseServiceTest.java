package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projektarbeit.immobilienverwaltung.model.Land;
/*
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdresseServiceTest {

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private PostleitzahlRepository postleitzahlRepository;

    @InjectMocks
    private AdresseService adresseService;

    private Adresse adresse;
    private Postleitzahl postleitzahl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postleitzahl = new Postleitzahl("12345", "Berlin", Land.DE);
        adresse = new Adresse(postleitzahl, "Musterstraße", "1");
        adresse.setAdresse_id(1L);
    }

    @Test
    void testSaveAdresse_NewPostleitzahl() {
        when(postleitzahlRepository.existsById(postleitzahl.getPostleitzahl())).thenReturn(false);
        when(postleitzahlRepository.save(postleitzahl)).thenReturn(postleitzahl);
        when(adresseRepository.save(adresse)).thenReturn(adresse);

        Adresse savedAdresse = adresseService.save(adresse);

        verify(postleitzahlRepository, times(1)).save(postleitzahl);
        verify(adresseRepository, times(1)).save(adresse);
        assertEquals(adresse, savedAdresse);
    }

    @Test
    void testSaveAdresse_ExistingPostleitzahl() {
        when(postleitzahlRepository.existsById(postleitzahl.getPostleitzahl())).thenReturn(true);
        when(adresseRepository.save(adresse)).thenReturn(adresse);

        Adresse savedAdresse = adresseService.save(adresse);

        verify(postleitzahlRepository, never()).save(postleitzahl);
        verify(adresseRepository, times(1)).save(adresse);
        assertEquals(adresse, savedAdresse);
    }

    @Test
    void testUpdateAdresse() {
        when(adresseRepository.save(adresse)).thenReturn(adresse);
        adresse.setStrasse("NeueStrasse");

        Adresse updatedAdresse = adresseService.save(adresse);

        assertEquals("NeueStrasse", updatedAdresse.getStrasse());
        verify(adresseRepository, times(1)).save(adresse);
    }

    @Test
    void testSaveAdresse_NullAdresse() {
        assertThrows(NullPointerException.class, () -> {
            adresseService.save(null);
        });
    }

    @Test
    void testSetStrasse_MaxLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            adresse.setStrasse("a".repeat(101));
        });
    }

    @Test
    void testSetHausnummer_MaxLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            adresse.setHausnummer("a".repeat(7));
        });
    }

    @Test
    void testSetStrasse_ValidLength() {
        adresse.setStrasse("GültigeStrasse");
        assertEquals("GültigeStrasse", adresse.getStrasse());
    }

    @Test
    void testSetHausnummer_ValidLength() {
        adresse.setHausnummer("123A");
        assertEquals("123A", adresse.getHausnummer());
    }

    @Test
    void testGettersAndSetters() {
        adresse.setAdresse_id(100L);
        assertEquals(100L, adresse.getAdresse_id());

        adresse.setPostleitzahlObj(new Postleitzahl("54321", "AndereStadt", Land.CH));
        assertEquals("54321", adresse.getPostleitzahlObj().getPostleitzahl());
        assertEquals("AndereStadt", adresse.getPostleitzahlObj().getStadt());
        assertEquals(Land.CH, adresse.getPostleitzahlObj().getLand());

        adresse.setStrasse("NeueStrasse");
        assertEquals("NeueStrasse", adresse.getStrasse());

        adresse.setHausnummer("5B");
        assertEquals("5B", adresse.getHausnummer());
    }

    @Test
    void testToString() {
        String expected = String.format("Adresse['%s', Hausnummer='%s', Strasse='%s']",
                adresse.getPostleitzahlObj(),
                adresse.getHausnummer(),
                adresse.getStrasse());
        assertEquals(expected, adresse.toString());
    }
}

 */