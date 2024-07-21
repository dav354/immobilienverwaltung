package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Benutzerdefinierte Annotation zur Validierung des Mietzeitraums eines Mieters (Mieter).
 * Stellt sicher, dass das Mietbeginn-Datum (mietbeginn) vor dem Mietende-Datum (mietende) liegt.
 */
@SuppressWarnings("SpellCheckingInspection")
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = MietPeriodValidator.class)
public @interface ValidMietPeriod {

    /**
     * Die Standardfehlermeldung, die verwendet wird, wenn der Mietzeitraum ungültig ist.
     *
     * @return die Standardfehlermeldung
     */
    String message() default "Mietbeginn muss vor Mietende liegen";

    /**
     * Ermöglicht die Angabe von Validierungsgruppen, zu denen diese Einschränkung gehört.
     *
     * @return die Gruppen, zu denen die Einschränkung gehört
     */
    Class<?>[] groups() default {};

    /**
     * Kann von Clients der Jakarta Bean Validation API verwendet werden, um benutzerdefinierte Payload-Objekte einer Einschränkung zuzuweisen.
     *
     * @return die Payload für die Einschränkung
     */
    Class<? extends Payload>[] payload() default {};
}