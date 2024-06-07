package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projektarbeit.immobilienverwaltung.model.Land;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/*
class PostleitzahlServiceTest {

    @Mock
    private PostleitzahlRepository postleitzahlRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @InjectMocks
    private PostleitzahlService postleitzahlService;

    private Postleitzahl postleitzahl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postleitzahl = new Postleitzahl();
        postleitzahl.setPostleitzahl("12345");
        postleitzahl.setStadt("Teststadt");
        postleitzahl.setLand(Land.DE);
    }

    @Test
    void testSavePostleitzahl() {
        when(postleitzahlRepository.save(any(Postleitzahl.class))).thenReturn(postleitzahl);

        Postleitzahl savedPostleitzahl = postleitzahlService.save(postleitzahl);

        assertNotNull(savedPostleitzahl);
        assertEquals("12345", savedPostleitzahl.getPostleitzahl());
        verify(postleitzahlRepository, times(1)).save(postleitzahl);
    }

    @Test
    void testDeletePostleitzahlIfUnused() {
        when(adresseRepository.countByPostleitzahl(postleitzahl)).thenReturn(0L);

        postleitzahlService.deletePostleitzahlIfUnused(postleitzahl);

        verify(postleitzahlRepository, times(1)).delete(postleitzahl);
    }

    @Test
    void testDeletePostleitzahlIfUsed() {
        when(adresseRepository.countByPostleitzahl(postleitzahl)).thenReturn(5L);

        postleitzahlService.deletePostleitzahlIfUnused(postleitzahl);

        verify(postleitzahlRepository, never()).delete(postleitzahl);
    }

    @Test
    void testDeletePostleitzahlIfUnused_NotUsed() {
        Postleitzahl postleitzahl = new Postleitzahl();
        postleitzahl.setPostleitzahl("12345");

        when(adresseRepository.countByPostleitzahl(postleitzahl)).thenReturn(0L);

        postleitzahlService.deletePostleitzahlIfUnused(postleitzahl);

        verify(postleitzahlRepository, times(1)).delete(postleitzahl);
    }

    @Test
    void testDeletePostleitzahlIfUnused_StillInUse() {
        Postleitzahl postleitzahl = new Postleitzahl();
        postleitzahl.setPostleitzahl("12345");

        when(adresseRepository.countByPostleitzahl(postleitzahl)).thenReturn(1L);

        postleitzahlService.deletePostleitzahlIfUnused(postleitzahl);

        verify(postleitzahlRepository, never()).delete(postleitzahl);
    }

    @Test
    void testSavePostleitzahl_NullPostleitzahl() {
        assertThrows(IllegalArgumentException.class, () -> {
            postleitzahlService.save(null);
        });
    }

    @Test
    void testDeletePostleitzahlIfUnused_NullPostleitzahl() {
        assertThrows(IllegalArgumentException.class, () -> {
            postleitzahlService.deletePostleitzahlIfUnused(null);
        });
    }

    @Test
    void testSavePostleitzahl_RepositoryException() {
        Postleitzahl postleitzahl = new Postleitzahl();
        postleitzahl.setPostleitzahl("12345");

        when(postleitzahlRepository.save(any(Postleitzahl.class))).thenThrow(new RuntimeException("Repository error"));

        assertThrows(RuntimeException.class, () -> {
            postleitzahlService.save(postleitzahl);
        });
    }
}

 */