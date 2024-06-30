package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import projektarbeit.immobilienverwaltung.model.Configuration;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ConfigurationRepositoryTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    private Configuration configuration;

    @BeforeEach
    public void setUp() {
        configuration = new Configuration();
        configuration.setConfigKey("testKey");
        configuration.setConfigValue("testValue");
    }

    @Test
    public void testSaveConfiguration() {
        Configuration savedConfiguration = configurationRepository.save(configuration);
        assertNotNull(savedConfiguration);
        assertEquals("testKey", savedConfiguration.getConfigKey());
        assertEquals("testValue", savedConfiguration.getConfigValue());
    }

    @Test
    public void testFindById() {
        configurationRepository.save(configuration);
        Optional<Configuration> foundConfiguration = configurationRepository.findById("testKey");
        assertTrue(foundConfiguration.isPresent());
        assertEquals("testKey", foundConfiguration.get().getConfigKey());
        assertEquals("testValue", foundConfiguration.get().getConfigValue());
    }

    @Test
    public void testUpdateConfiguration() {
        configurationRepository.save(configuration);
        Configuration updatedConfiguration = new Configuration();
        updatedConfiguration.setConfigKey("testKey");
        updatedConfiguration.setConfigValue("newValue");

        configurationRepository.save(updatedConfiguration);

        Optional<Configuration> foundConfiguration = configurationRepository.findById("testKey");
        assertTrue(foundConfiguration.isPresent());
        assertEquals("newValue", foundConfiguration.get().getConfigValue());
    }

    @Test
    public void testDeleteConfiguration() {
        configurationRepository.save(configuration);
        configurationRepository.deleteById("testKey");

        Optional<Configuration> foundConfiguration = configurationRepository.findById("testKey");
        assertFalse(foundConfiguration.isPresent());
    }

    @Test
    public void testFindAllConfigurations() {
        Configuration configuration1 = new Configuration();
        configuration1.setConfigKey("testKey1");
        configuration1.setConfigValue("testValue1");

        Configuration configuration2 = new Configuration();
        configuration2.setConfigKey("testKey2");
        configuration2.setConfigValue("testValue2");

        configurationRepository.save(configuration);
        configurationRepository.save(configuration1);
        configurationRepository.save(configuration2);

        Iterable<Configuration> configurations = configurationRepository.findAll();
        assertNotNull(configurations);
        assertEquals(3, ((Collection<?>) configurations).size());
    }
}