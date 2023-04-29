package account.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BreachedValidator.class)
public @interface BreachedConstraint {

    String message() default "The password is in the hacker's database!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
