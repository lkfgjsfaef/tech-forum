package com.example.forum.core.validator;

import com.example.forum.core.util.XssUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoXssValidator implements ConstraintValidator<NoXss, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        if (XssUtil.containsXss(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("内容包含不安全的HTML/JS代码").addConstraintViolation();
            return false;
        }
        return true;
    }
}
