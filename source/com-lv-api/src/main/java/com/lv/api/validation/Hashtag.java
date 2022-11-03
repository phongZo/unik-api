package com.lv.api.validation;

import com.lv.api.validation.impl.HashtagValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HashtagValidation.class)
@Documented
public @interface Hashtag {
    boolean allowNull() default true;

    String message() default "Hashtag is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
