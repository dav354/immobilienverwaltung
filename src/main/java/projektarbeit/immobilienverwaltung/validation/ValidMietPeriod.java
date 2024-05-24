package projektarbeit.immobilienverwaltung.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = MietPeriodValidator.class)
public @interface ValidMietPeriod {
    String message() default "Mietbeginn must be before Mietende";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}