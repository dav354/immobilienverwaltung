package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;

/**
 * Validator class for validating the rental period of a Mietvertrag.
 * Ensures that the rental start date (mietbeginn) is before the rental end date (mietende).
 */
@SuppressWarnings("SpellCheckingInspection")
public class MietPeriodValidator implements ConstraintValidator<ValidMietPeriod, Mietvertrag> {

    /**
     * Checks if the rental period is valid.
     * Validates that the rental start date (mietbeginn) is before the rental end date (mietende).
     *
     * @param mietvertrag The Mietvertrag object to validate.
     * @param context     The context in which the constraint is evaluated.
     * @return true if the rental period is valid, false otherwise.
     */
    @Override
    public boolean isValid(Mietvertrag mietvertrag, ConstraintValidatorContext context) {
        if (mietvertrag.getMietbeginn() != null && mietvertrag.getMietende() != null) {
            // Validate that the rental start date is before the rental end date
            return mietvertrag.getMietbeginn().isBefore(mietvertrag.getMietende());
        }
        // If either date is null, validation is not performed
        return true; // Only validate if both dates are non-null
    }
}