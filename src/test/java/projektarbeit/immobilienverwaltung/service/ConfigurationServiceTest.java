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
    void testSetVermieteteChecked_NewConfig() {
        when(configurationRepository.findById("vermieteteChecked")).thenReturn(Optional.empty());

        configurationService.setVermieteteChecked(true);

        verify(configurationRepository, times(1)).save(argThat(config ->
                config.getConfigKey().equals("vermieteteChecked") && config.getConfigValue().equals("true")
        ));
    }

    @Test
    void testSetVermieteteChecked_ExistingConfig() {
        Configuration config = new Configuration();
        config.setConfigKey("vermieteteChecked");
        config.setConfigValue("false");

        when(configurationRepository.findById("vermieteteChecked")).thenReturn(Optional.of(config));

        configurationService.setVermieteteChecked(true);

        verify(configurationRepository, times(1)).save(config);
        assertEquals("true", config.getConfigValue());
    }

    @Test
    void testGetVermieteteChecked_Activated() {
        Configuration config = new Configuration();
        config.setConfigKey("vermieteteChecked");
        config.setConfigValue("true");

        when(configurationRepository.findById("vermieteteChecked")).thenReturn(Optional.of(config));

        boolean isChecked = configurationService.getVermieteteChecked();

        assertTrue(isChecked);
    }

    @Test
    void testGetVermieteteChecked_Deactivated() {
        Configuration config = new Configuration();
        config.setConfigKey("vermieteteChecked");
        config.setConfigValue("false");

        when(configurationRepository.findById("vermieteteChecked")).thenReturn(Optional.of(config));

        boolean isChecked = configurationService.getVermieteteChecked();

        assertFalse(isChecked);
    }

    @Test
    void testGetVermieteteChecked_DefaultTrue() {
        when(configurationRepository.findById("vermieteteChecked")).thenReturn(Optional.empty());

        boolean isChecked = configurationService.getVermieteteChecked();

        assertTrue(isChecked);
    }

    @Test
    void testSetUnvermieteteChecked_NewConfig() {
        when(configurationRepository.findById("unvermieteteChecked")).thenReturn(Optional.empty());

        configurationService.setUnvermieteteChecked(true);

        verify(configurationRepository, times(1)).save(argThat(config ->
                config.getConfigKey().equals("unvermieteteChecked") && config.getConfigValue().equals("true")
        ));
    }

    @Test
    void testSetUnvermieteteChecked_ExistingConfig() {
        Configuration config = new Configuration();
        config.setConfigKey("unvermieteteChecked");
        config.setConfigValue("false");

        when(configurationRepository.findById("unvermieteteChecked")).thenReturn(Optional.of(config));

        configurationService.setUnvermieteteChecked(true);

        verify(configurationRepository, times(1)).save(config);
        assertEquals("true", config.getConfigValue());
    }

    @Test
    void testGetUnvermieteteChecked_Activated() {
        Configuration config = new Configuration();
        config.setConfigKey("unvermieteteChecked");
        config.setConfigValue("true");

        when(configurationRepository.findById("unvermieteteChecked")).thenReturn(Optional.of(config));

        boolean isChecked = configurationService.getUnvermieteteChecked();

        assertTrue(isChecked);
    }

    @Test
    void testGetUnvermieteteChecked_Deactivated() {
        Configuration config = new Configuration();
        config.setConfigKey("unvermieteteChecked");
        config.setConfigValue("false");

        when(configurationRepository.findById("unvermieteteChecked")).thenReturn(Optional.of(config));

        boolean isChecked = configurationService.getUnvermieteteChecked();

        assertFalse(isChecked);
    }

    @Test
    void testGetUnvermieteteChecked_DefaultTrue() {
        when(configurationRepository.findById("unvermieteteChecked")).thenReturn(Optional.empty());

        boolean isChecked = configurationService.getUnvermieteteChecked();

        assertTrue(isChecked);
    }
}