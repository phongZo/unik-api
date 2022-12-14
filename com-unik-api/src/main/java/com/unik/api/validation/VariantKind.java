package com.unik.api.validation;

import com.unik.api.validation.impl.VariantKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VariantKindValidation.class)
@Documented
public @interface VariantKind {
    boolean allowNull() default true;
    String message() default "Variant kind is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
