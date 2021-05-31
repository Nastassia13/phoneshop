package com.es.core.constraints;

import com.es.core.dao.PhoneDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ModelConstraintValidator implements ConstraintValidator<ModelConstraint, String> {
    @Resource
    private PhoneDao phoneDao;

    @Override
    public boolean isValid(String phoneModel, ConstraintValidatorContext constraintValidatorContext) {
        return phoneModel.isEmpty() || phoneDao.get(phoneModel).isPresent();
    }
}
