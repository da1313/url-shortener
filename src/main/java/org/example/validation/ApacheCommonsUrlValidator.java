package org.example.validation;

import org.apache.commons.validator.routines.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ApacheCommonsUrlValidator implements ConstraintValidator<URL, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(value);
    }
}
