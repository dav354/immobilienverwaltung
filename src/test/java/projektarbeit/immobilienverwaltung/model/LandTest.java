package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandTest {

    @Test
    void testGetName() {
        // Test a few sample countries
        assertEquals("Afghanistan", Land.AF.getName());
        assertEquals("Deutschland", Land.DE.getName());
        assertEquals("Japan", Land.JP.getName());
        assertEquals("Vereinigte Staaten", Land.US.getName());
    }

    @Test
    void testToString() {
        // Test that toString returns the correct name
        assertEquals("Afghanistan", Land.AF.toString());
        assertEquals("Deutschland", Land.DE.toString());
        assertEquals("Japan", Land.JP.toString());
        assertEquals("Vereinigte Staaten", Land.US.toString());
    }

    @Test
    void testAllCountriesHaveNames() {
        // Ensure that every country has a non-null, non-empty name
        for (Land land : Land.values()) {
            assertNotNull(land.getName(), "Country name should not be null");
            assertFalse(land.getName().isEmpty(), "Country name should not be empty");
        }
    }

    //TODO add missing countries
    /*
    @Test
    void testNumberOfCountries() {
        // Ensure the number of countries matches the expected number
        int expectedNumberOfCountries = 195; // As of current data
        assertEquals(expectedNumberOfCountries, Land.values().length, "The number of countries should match the expected count");
    }
     */

    @Test
    void testCountryCodes() {
        // Test that the country codes match the expected values
        assertEquals("AF", Land.AF.name());
        assertEquals("DE", Land.DE.name());
        assertEquals("JP", Land.JP.name());
        assertEquals("US", Land.US.name());
    }
}