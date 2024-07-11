package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Benutzerdefinierter Validator zur Validierung, ob das angegebene Jahr gültig ist.
 */
public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

    /**
     * Initialisiert den Validator.
     *
     * @param constraint die Instanz der Constraint-Annotation
     */
    @Override
    public void initialize(ValidYear constraint) {}

    /**
     * Validiert, ob das angegebene Jahr gültig ist.
     *
     * @param value   der zu validierende Jahreswert
     * @param context der Kontext, in dem die Einschränkung bewertet wird
     * @return true, wenn das Jahr gültig ist, andernfalls false
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // Wenn der Jahreswert null ist, wird er als gültig betrachtet
        if (value == null) {
            return true;
        }

        // Das aktuelle Jahr ermitteln
        int currentYear = LocalDate.now().getYear();

        // Überprüfen, ob das angegebene Jahr in einem gültigen Bereich liegt
        return value > 999 && value <= currentYear;
    }
}