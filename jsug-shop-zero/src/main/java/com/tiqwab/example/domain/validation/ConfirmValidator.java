package com.tiqwab.example.domain.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ConfirmValidator implements ConstraintValidator<Confirm, Object> {

    private String field;
    private String confirmField;
    private String message;

    @Override
    public void initialize(Confirm constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.confirmField = "confirm" + StringUtils.capitalize(field);
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object fieldValue = beanWrapper.getPropertyValue(this.field);
        Object confirmFieldValue = beanWrapper.getPropertyValue(this.confirmField);
        if (Objects.equals(fieldValue, confirmFieldValue)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(confirmField).addConstraintViolation();
            return false;
        }
    }

}
