package com.notebook.interpreter.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { InterpreterRequestValidator.class })
public @interface CorrectRequest {
    String message() default "Invalid Interpret Request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
