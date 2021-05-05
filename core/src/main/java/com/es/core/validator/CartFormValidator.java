package com.es.core.validator;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Stock;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component
public class CartFormValidator implements Validator {
    @Resource
    private PhoneDao phoneDao;
    private static final String QUANTITY_IS_NEGATIVE = "quantity.negative";
    private static final String QUANTITY_IS_STRING = "quantity.string";
    private static final String QUANTITY_OVER_STOCK = "quantity.stock";

    @Override
    public boolean supports(Class<?> aClass) {
        return CartForm.class.equals(aClass);
    }

    @Override
    public void validate(Object objectForValidating, Errors errors) {
        CartForm cartForm = (CartForm) objectForValidating;
        for (int i = 0; i < cartForm.getPhoneIds().size(); i++) {
            validateNext(errors, cartForm, i);
        }
    }

    private void validateNext(Errors errors, CartForm cartForm, int i) {
        long quantity;
        try {
            quantity = Long.parseLong(cartForm.getQuantities().get(i));
            if (quantity <= 0) {
                errors.rejectValue("quantities[" + i + "]", QUANTITY_IS_NEGATIVE, "Enter number more than 0.");
            }
            Long phoneId = Long.parseLong(cartForm.getPhoneIds().get(i));
            checkStock(phoneId, quantity, errors, i);
        } catch (NumberFormatException e) {
            errors.rejectValue("quantities[" + i + "]", QUANTITY_IS_STRING, "Enter integer number.");
        }
    }

    private void checkStock(Long phoneId, Long quantity, Errors errors, int i) {
        phoneDao.get(phoneId).ifPresent(phone -> {
            Stock stock = phoneDao.findStock(phone);
            if (stock.getStock() < quantity) {
                errors.rejectValue("quantities[" + i + "]", QUANTITY_OVER_STOCK, "Out of stock. Available: " + stock.getStock() + ".");
            }
        });
    }
}
