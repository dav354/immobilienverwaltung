package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projektarbeit.immobilienverwaltung.model.Mieter;

public class MietPeriodValidator implements ConstraintValidator<ValidMietPeriod, Mieter> {
    @Override
    public boolean isValid(Mieter mieter, ConstraintValidatorContext context) {
        if (mieter.getMietbeginn() != null && mieter.getMietende() != null) {
            return mieter.getMietbeginn().isBefore(mieter.getMietende());
        }
        return true; // Only validate if both dates are non-null
    }
}