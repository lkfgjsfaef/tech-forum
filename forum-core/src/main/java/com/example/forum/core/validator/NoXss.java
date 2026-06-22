package com.example.forum.core.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoXssValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoXss {
    String message() default "内容包含非法字符";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
