package com.lv.api.validation.impl;

import com.lv.api.constant.Constants;
import com.lv.api.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryKindValidation implements ConstraintValidator<CategoryKind, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer categoryKind, ConstraintValidatorContext constraintValidatorContext) {
        if(categoryKind == null && allowNull) {
            return true;
        }
        if (categoryKind != null) {
            switch (categoryKind) {
                case Constants.CATEGORY_KIND_NEWS:
                case Constants.CATEGORY_KIND_JOB:
                case Constants.CATEGORY_KIND_DEPARTMENT:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
