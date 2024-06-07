package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Custom validator to validate if the year provided is valid.
 */
public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

    /**
     * Initializes the validator.
     *
     * @param constraint the constraint annotation instance
     */
    @Override
    public void initialize(ValidYear constraint) {}

    /**
     * Validates if the provided year is valid.
     *
     * @param value   the year value to validate
     * @param context context in which the constraint is evaluated
     * @return true if the year is valid, false otherwise
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // If the year value is null, consider it as valid
        if (value == null) {
            return true;
        }

        // Get the current year
        int currentYear = LocalDate.now().getYear();

        // Check if the provided year is within a valid range
        return value > 999 && value <= currentYear;
    }
}