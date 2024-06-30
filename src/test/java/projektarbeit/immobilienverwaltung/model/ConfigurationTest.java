package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationTest {

    private Configuration configuration;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        configuration = new Configuration();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGetAndSetConfigKey() {
        String configKey = "testKey";
        configuration.setConfigKey(configKey);
        assertEquals(configKey, configuration.getConfigKey());
    }

    @Test
    public void testGetAndSetConfigValue() {
        String configValue = "testValue";
        configuration.setConfigValue(configValue);
        assertEquals(configValue, configuration.getConfigValue());
    }

    @Test
    public void testConfigKeyNotNull() {
        configuration.setConfigKey(null);
        configuration.setConfigValue("someValue");

        Set<ConstraintViolation<Configuration>> violations = validator.validate(configuration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("configKey")));
    }

    @Test
    public void testConfigValueNotNull() {
        configuration.setConfigKey("someKey");
        configuration.setConfigValue(null);

        Set<ConstraintViolation<Configuration>> violations = validator.validate(configuration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("configValue")));
    }

    @Test
    public void testValidConfiguration() {
        configuration.setConfigKey("someKey");
        configuration.setConfigValue("someValue");

        Set<ConstraintViolation<Configuration>> violations = validator.validate(configuration);
        assertTrue(violations.isEmpty());
    }
}