package projektarbeit.immobilienverwaltung.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projektarbeit.immobilienverwaltung.model.Configuration;
import projektarbeit.immobilienverwaltung.repository.ConfigurationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @InjectMocks
    private ConfigurationService configurationService;

    @Test
    void testFindByKey() {
        Configuration config = new Configuration();
        config.setConfigKey("testKey");
        config.setConfigValue("testValue");

        when(configurationRepository.findById("testKey")).thenReturn(Optional.of(config));

        Optional<Configuration> foundConfig = configurationService.findByKey("testKey");

        assertTrue(foundConfig.isPresent());
        assertEquals("testValue", foundConfig.get().getConfigValue());
    }

    @Test
    void testFindByKey_NotFound() {
        when(configurationRepository.findById("nonExistentKey")).thenReturn(Optional.empty());

        Optional<Configuration> foundConfig = configurationService.findByKey("nonExistentKey");

        assertFalse(foundConfig.isPresent());
    }

    @Test
    void testSave() {
        Configuration config = new Configuration();
        config.setConfigKey("testKey");
        config.setConfigValue("testValue");

        configurationService.save(config);

        verify(configurationRepository, times(1)).save(config);
    }

    @Test
    void testSetDarkMode_NewConfig() {
        when(configurationRepository.findById("darkMode")).thenReturn(Optional.empty());

        configurationService.setDarkMode(true);

        verify(configurationRepository, times(1)).save(argThat(config ->
                config.getConfigKey().equals("darkMode") && config.getConfigValue().equals("true")
        ));
    }

    @Test
    void testSetDarkMode_ExistingConfig() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("false");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        configurationService.setDarkMode(true);

        verify(configurationRepository, times(1)).save(config);
        assertEquals("true", config.getConfigValue());
    }

    @Test
    void testIsDarkMode_Activated() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("true");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        boolean isDarkMode = configurationService.isDarkMode();

        assertTrue(isDarkMode);
    }

    @Test
    void testIsDarkMode_Deactivated() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("false");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        boolean isDarkMode = configurationService.isDarkMode();

        assertFalse(isDarkMode);
    }

    @Test
    void testIsDarkMode_DefaultTrue() {
        when(configurationRepository.findById("darkMode")).thenReturn(Optional.empty());

        boolean isDarkMode = configurationService.isDarkMode();

        assertTrue(isDarkMode);
    }

    @Test
    void testSetDarkMode_NullConfig() {
        when(configurationRepository.findById("darkMode")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> configurationService.setDarkMode(false));
        verify(configurationRepository, times(1)).save(argThat(config ->
                config.getConfigKey().equals("darkMode") && config.getConfigValue().equals("false")
        ));
    }

    @Test
    void testIsDarkMode_InvalidValue() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("invalidBoolean");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        boolean isDarkMode = configurationService.isDarkMode();

        assertFalse(isDarkMode);
    }

    @Test
    void testSaveWithInvalidValue() {
        Configuration config = new Configuration();
        config.setConfigKey("invalidConfig");
        config.setConfigValue("invalid@value");

        assertDoesNotThrow(() -> configurationService.save(config));

        verify(configurationRepository, times(1)).save(config);
    }

    @Test
    void testRetrieveConfigurationWithInvalidValue() {
        Configuration config = new Configuration();
        config.setConfigKey("testKey");
        config.setConfigValue("invalid@value");

        when(configurationRepository.findById("testKey")).thenReturn(Optional.of(config));

        Optional<Configuration> foundConfig = configurationService.findByKey("testKey");

        assertTrue(foundConfig.isPresent());
        assertEquals("invalid@value", foundConfig.get().getConfigValue());
    }

    @Test
    void testSetDarkModeWithInvalidValue() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("invalidValue");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        configurationService.setDarkMode(true);

        verify(configurationRepository, times(1)).save(config);
        assertEquals("true", config.getConfigValue());
    }

    @Test
    void testIsDarkModeWithInvalidBooleanValue() {
        Configuration config = new Configuration();
        config.setConfigKey("darkMode");
        config.setConfigValue("invalidBoolean");

        when(configurationRepository.findById("darkMode")).thenReturn(Optional.of(config));

        boolean isDarkMode = configurationService.isDarkMode();

        assertFalse(isDarkMode);
    }
}