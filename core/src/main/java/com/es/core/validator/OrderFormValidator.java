package com.es.core.validator;

import com.es.core.forms.OrderForm;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderFormValidator implements Validator {
    private static final String INPUT_BLANK = "input.blank";
    private static final String INVALID_PHONE = "phone.invalid";

    @Override
    public boolean supports(Class<?> aClass) {
        return OrderForm.class.equals(aClass);
    }

    @Override
    public void validate(Object objectForValidating, Errors errors) {
        OrderForm orderForm = (OrderForm) objectForValidating;
        checkBlank(orderForm, errors);
        if (!orderForm.getContactPhoneNo().matches("\\+?\\d*")) {
            errors.rejectValue("contactPhoneNo", INVALID_PHONE, "Invalid phone number!");
        }
    }

    private void checkBlank(OrderForm orderForm, Errors errors) {
        if (StringUtils.isNullOrEmpty(orderForm.getFirstName())) {
            errors.rejectValue("firstName", INPUT_BLANK, "The value is required!");
        }
        if (StringUtils.isNullOrEmpty(orderForm.getLastName())) {
            errors.rejectValue("lastName", INPUT_BLANK, "The value is required!");
        }
        if (StringUtils.isNullOrEmpty(orderForm.getContactPhoneNo())) {
            errors.rejectValue("contactPhoneNo", INPUT_BLANK, "The value is required!");
        }
        if (StringUtils.isNullOrEmpty(orderForm.getDeliveryAddress())) {
            errors.rejectValue("deliveryAddress", INPUT_BLANK, "The value is required!");
        }
    }
}
