package projektarbeit.immobilienverwaltung.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.service.MieterService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for checking if an email is unique.
 * Implements the {@link ConstraintValidator} interface to validate the uniqueness of an email.
 */
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private static final Logger logger = LoggerFactory.getLogger(UniqueEmailValidator.class);

    private MieterService mieterService;

    /**
     * Default constructor needed for instantiation.
     */
    public UniqueEmailValidator() {}

    /**
     * Constructor for UniqueEmailValidator with MieterService dependency.
     *
     * @param mieterService The MieterService to check email uniqueness.
     */
    @Autowired
    public UniqueEmailValidator(MieterService mieterService) {
        this.mieterService = mieterService;
        logger.info("UniqueEmailValidator instantiated with MieterService.");
    }

    /**
     * Initializes the validator. This method can be used to set up any necessary state or resources.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(UniqueEmail constraintAnnotation) {}

    /**
     * Validates if the provided email is unique.
     *
     * @param email   the email address to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if the email is unique or null/empty, {@code false} otherwise
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // If the email is null or empty, consider it valid
        if (email == null || email.isEmpty()) {
            return true;
        }
        // Check if the email exists in the system
        return !mieterService.emailExists(email);
    }
}