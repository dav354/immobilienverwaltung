package projektarbeit.immobilienverwaltung.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating the uniqueness of an email address.
 * This annotation can be applied to fields or methods to ensure that the email address is unique.
 */
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    /**
     * The default error message that will be shown when the email is not unique.
     *
     * @return the error message
     */
    String message() default "Email address must be unique";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     * This must default to an empty array of Class objects.
     *
     * @return the array of groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects
     * to a constraint. This attribute is not used by the API itself.
     *
     * @return the array of payload classes
     */
    Class<? extends Payload>[] payload() default {};
}