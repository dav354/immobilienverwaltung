package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projektarbeit.immobilienverwaltung.model.Mieter;

/**
 * Validator class for validating the rental period of a tenant (Mieter).
 * Ensures that the rental start date (mietbeginn) is before the rental end date (mietende).
 */
public class MietPeriodValidator implements ConstraintValidator<ValidMietPeriod, Mieter> {

    /**
     * Checks if the rental period is valid.
     * Validates that the rental start date (mietbeginn) is before the rental end date (mietende).
     *
     * @param mieter  The tenant (Mieter) object to validate.
     * @param context The context in which the constraint is evaluated.
     * @return true if the rental period is valid, false otherwise.
     */
    @Override
    public boolean isValid(Mieter mieter, ConstraintValidatorContext context) {
        if (mieter.getMietbeginn() != null && mieter.getMietende() != null) {
            // Validate that the rental start date is before the rental end date
            return mieter.getMietbeginn().isBefore(mieter.getMietende());
        }
        // If either date is null, validation is not performed
        return true; // Only validate if both dates are non-null
    }
}