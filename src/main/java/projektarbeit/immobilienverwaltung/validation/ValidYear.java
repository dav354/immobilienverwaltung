package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Benutzerdefinierte Annotation zur Validierung eines Jahresfelds.
 * Stellt sicher, dass das annotierte Jahresfeld gemäß der angegebenen Validierungslogik gültig ist.
 */
@Documented
@Constraint(validatedBy = YearValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {

    /**
     * Die Standardfehlermeldung, die verwendet wird, wenn das Jahr ungültig ist.
     *
     * @return die Standardfehlermeldung
     */
    String message() default "Ungültiges Jahr";

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