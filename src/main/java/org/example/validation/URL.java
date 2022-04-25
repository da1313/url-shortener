package org.example.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ApacheCommonsUrlValidator.class)
public @interface URL {
    String message() default "{org.hibernate.validator.constraints.URL.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default  {};
}
