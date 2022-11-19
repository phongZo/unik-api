package com.lv.api.validation.impl;

import com.lv.api.constant.Constants;
import com.lv.api.validation.AmountOrdersDetail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountOrdersDetailValidation implements ConstraintValidator<AmountOrdersDetail, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(AmountOrdersDetail constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer amount, ConstraintValidatorContext constraintValidatorContext) {
        if(amount == null && allowNull) {
            return true;
        }
        if(!(amount > Constants.MIN_OF_AMOUNT)) {
            return false;
        }
        return true;
    }
}
