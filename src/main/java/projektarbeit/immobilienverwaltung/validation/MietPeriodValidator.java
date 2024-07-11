package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import projektarbeit.immobilienverwaltung.model.Mietvertrag;

/**
 * Validator-Klasse zur Validierung des Mietzeitraums eines Mietvertrags.
 * Stellt sicher, dass das Mietbeginn-Datum (mietbeginn) vor dem Mietende-Datum (mietende) liegt.
 */
@SuppressWarnings("SpellCheckingInspection")
public class MietPeriodValidator implements ConstraintValidator<ValidMietPeriod, Mietvertrag> {

    /**
     * Überprüft, ob der Mietzeitraum gültig ist.
     * Validiert, dass das Mietbeginn-Datum (mietbeginn) vor dem Mietende-Datum (mietende) liegt.
     *
     * @param mietvertrag Der zu validierende Mietvertrag.
     * @param context     Der Kontext, in dem die Einschränkung bewertet wird.
     * @return true, wenn der Mietzeitraum gültig ist, andernfalls false.
     */
    @Override
    public boolean isValid(Mietvertrag mietvertrag, ConstraintValidatorContext context) {
        if (mietvertrag.getMietbeginn() != null && mietvertrag.getMietende() != null) {
            // Validiert, dass das Mietbeginn-Datum vor dem Mietende-Datum liegt
            return mietvertrag.getMietbeginn().isBefore(mietvertrag.getMietende());
        }
        // Wenn eines der beiden Daten null ist, wird keine Validierung durchgeführt
        return true; // Nur validieren, wenn beide Daten nicht null sind
    }
}