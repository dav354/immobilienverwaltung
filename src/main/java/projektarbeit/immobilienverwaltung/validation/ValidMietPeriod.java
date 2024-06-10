package projektarbeit.immobilienverwaltung.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom annotation for validating the rental period of a tenant (Mieter).
 * Ensures that the rental start date (mietbeginn) is before the rental end date (mietende).
 */
@SuppressWarnings("SpellCheckingInspection")
@Target(TYPE) // Annotation can be applied to types (classes)
@Retention(RUNTIME) // Annotation is available at runtime
@Constraint(validatedBy = MietPeriodValidator.class) // Specifies the validator class
public @interface ValidMietPeriod {

    /**
     * The default error message to be used if the rental period is invalid.
     *
     * @return the default error message
     */
    String message() default "Mietbeginn must be before Mietende";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Jakarta Bean Validation API to assign custom payload objects to a constraint.
     *
     * @return the payload for the constraint
     */
    Class<? extends Payload>[] payload() default {};
}