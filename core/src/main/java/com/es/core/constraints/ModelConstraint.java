package com.es.core.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ModelConstraintValidator.class)
public @interface ModelConstraint {
    String message() default "Product not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
