package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projektarbeit.immobilienverwaltung.validation.ValidYear;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {
    public void initialize(ValidYear constraint) {
    }

    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int currentYear = LocalDate.now().getYear();
        return value > 999 && value <= currentYear;
    }
}