package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom annotation for validating a year field.
 * Ensures that the annotated year field is valid according to the specified validation logic.
 */
@Documented // Indicates that elements using this annotation should be documented by javadoc and similar tools
@Constraint(validatedBy = YearValidator.class) // Specifies the validator class
@Target({ ElementType.FIELD, ElementType.METHOD }) // Annotation can be applied to fields and methods
@Retention(RetentionPolicy.RUNTIME) // Annotation is available at runtime
public @interface ValidYear {

    /**
     * The default error message to be used if the year is invalid.
     *
     * @return the default error message
     */
    String message() default "Invalid year";

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