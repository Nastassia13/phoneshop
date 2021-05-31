package com.es.core.constraints;

import com.es.core.dao.PhoneDao;
import com.es.core.forms.QuickForm;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class StockConstraintValidator implements ConstraintValidator<StockConstraint, QuickForm> {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    @Override
    public boolean isValid(QuickForm quickForm, ConstraintValidatorContext constraintValidatorContext) {
        if (quickForm.getPhoneModel().isEmpty()) {
            return true;
        }
        Optional<Phone> optionalPhone = phoneDao.get(quickForm.getPhoneModel());
        if (optionalPhone.isEmpty()) {
            return false;
        }
        return checkStock(optionalPhone.get(), quickForm.getQuantity(), constraintValidatorContext);
    }

    private boolean checkStock(Phone phone, Long quantity, ConstraintValidatorContext constraintValidatorContext) {
        if (quantity == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Not a number").addConstraintViolation();
            return false;
        }
        Stock stock = phoneDao.findStock(phone);
        Long quantityInCart = findQuantityInCart(phone.getId());
        if (stock.getStock() >= quantity + quantityInCart) {
            return true;
        }
        constraintValidatorContext
                .buildConstraintViolationWithTemplate("Out of stock. Available: " + (stock.getStock() - quantityInCart))
                .addConstraintViolation();
        return false;
    }

    private Long findQuantityInCart(Long phoneId) {
        Cart cart = cartService.getCart();
        Optional<Long> optionalQuantity = cart.getItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .map(CartItem::getQuantity).findAny();
        return optionalQuantity.orElse(0L);
    }
}
