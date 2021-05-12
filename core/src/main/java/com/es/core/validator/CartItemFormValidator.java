package com.es.core.validator;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Component
public class CartItemFormValidator implements Validator {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;
    private static final String QUANTITY_IS_NEGATIVE = "quantity.negative";
    private static final String QUANTITY_IS_STRING = "quantity.string";
    private static final String QUANTITY_OVER_STOCK = "quantity.stock";

    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemForm.class.equals(aClass);
    }

    @Override
    public void validate(Object objectForValidating, Errors errors) {
        CartItemForm cartItemForm = (CartItemForm) objectForValidating;
        long quantity;
        try {
            quantity = Long.parseLong(cartItemForm.getQuantity());
            if (quantity <= 0) {
                errors.rejectValue("quantity", QUANTITY_IS_NEGATIVE, "Enter number more than 0.");
            }
            Long phoneId = Long.parseLong(cartItemForm.getPhoneId());
            checkStock(phoneId, quantity, findQuantityInCart(phoneId), errors);
        } catch (NumberFormatException e) {
            errors.rejectValue("quantity", QUANTITY_IS_STRING, "Enter integer number.");
        }
    }

    private Long findQuantityInCart(Long phoneId) {
        Cart cart = cartService.getCart();
        int index = cart.getItems().stream()
                .map(CartItem::getPhone)
                .map(Phone::getId)
                .collect(Collectors.toList())
                .indexOf(phoneId);
        return index != -1 ? cart.getItems().get(index).getQuantity() : 0L;
    }

    private void checkStock(Long phoneId, Long quantity, Long quantityInCart, Errors errors) {
        phoneDao.get(phoneId).ifPresent(phone -> {
            Stock stock = phoneDao.findStock(phone);
            if (stock.getStock() < quantity + quantityInCart) {
                errors.rejectValue("quantity", QUANTITY_OVER_STOCK, "Out of stock. Available: " + (stock.getStock() - quantityInCart) + ".");
            }
        });
    }
}
