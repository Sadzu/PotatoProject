package ru.cft.template.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCostValidator implements ConstraintValidator<ValidCost, Long> {

    @Override
    public void initialize(ValidCost constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value != null && value > 0;
    }
}
