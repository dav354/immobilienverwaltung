package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LandTest {

    @Test
    void testGetName() {
        // Teste einige Beispiel-Länder
        assertEquals("Afghanistan", Land.AF.getName());
        assertEquals("Deutschland", Land.DE.getName());
        assertEquals("Japan", Land.JP.getName());
        assertEquals("Vereinigte Staaten", Land.US.getName());
    }

    @Test
    void testToString() {
        // Teste, ob toString den richtigen Namen zurückgibt
        assertEquals("Afghanistan", Land.AF.toString());
        assertEquals("Deutschland", Land.DE.toString());
        assertEquals("Japan", Land.JP.toString());
        assertEquals("Vereinigte Staaten", Land.US.toString());
    }

    @Test
    void testAlleLänderHabenNamen() {
        // Stelle sicher, dass jedes Land einen nicht-null und nicht-leeren Namen hat
        for (Land land : Land.values()) {
            assertNotNull(land.getName(), "Ländername sollte nicht null sein");
            assertFalse(land.getName().isEmpty(), "Ländername sollte nicht leer sein");
        }
    }

    @Test
    void testAnzahlDerLänder() {
        int erwarteteAnzahlDerLänder = 196; // Diese Zahl sollte aktualisiert werden, wenn sich die Anzahl der Länder ändert
        assertEquals(erwarteteAnzahlDerLänder, Land.values().length, "Die Anzahl der Länder sollte mit der erwarteten Anzahl übereinstimmen");
    }

    @Test
    void testLänderCodes() {
        // Teste, ob die Ländercodes den erwarteten Werten entsprechen
        assertEquals("AF", Land.AF.name());
        assertEquals("DE", Land.DE.name());
        assertEquals("JP", Land.JP.name());
        assertEquals("US", Land.US.name());
    }

    // Zusätzliche sinnvolle Testfälle

    @Test
    void testEinzigartigeLändercodes() {
        // Stelle sicher, dass alle Ländercodes einzigartig sind
        Set<String> codes = new HashSet<>();
        for (Land land : Land.values()) {
            assertTrue(codes.add(land.name()), "Doppelte Ländercode gefunden: " + land.name());
        }
    }

    @Test
    void testEinzigartigeLändernamen() {
        // Stelle sicher, dass alle Ländernamen einzigartig sind
        Set<String> namen = new HashSet<>();
        for (Land land : Land.values()) {
            assertTrue(namen.add(land.getName()), "Doppelter Ländername gefunden: " + land.getName());
        }
    }

    @Test
    void testLändernamenNichtNullUndNichtLeer() {
        // Stelle sicher, dass kein Ländername null oder leer ist
        for (Land land : Land.values()) {
            assertNotNull(land.getName(), "Ländername sollte nicht null sein");
            assertFalse(land.getName().isEmpty(), "Ländername sollte nicht leer sein");
        }
    }
}