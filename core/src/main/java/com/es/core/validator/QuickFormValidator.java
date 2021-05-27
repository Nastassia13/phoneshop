package com.es.core.validator;

import com.es.core.dao.PhoneDao;
import com.es.core.forms.QuickForm;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class QuickFormValidator implements Validator {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;
    private static final String PHONE_NOT_FOUND = "phone.notFound";
    private static final String QUANTITY_IS_NEGATIVE = "quantity.negative";
    private static final String QUANTITY_IS_STRING = "quantity.string";
    private static final String QUANTITY_OVER_STOCK = "quantity.stock";

    @Override
    public boolean supports(Class<?> aClass) {
        return QuickForm.class.equals(aClass);
    }

    @Override
    public void validate(Object objectForValidating, Errors errors) {
        QuickForm quickForm = (QuickForm) objectForValidating;
        for (int i = 0; i < quickForm.getPhoneModels().size(); i++) {
            validateNext(errors, quickForm, i);
        }
    }

    private void validateNext(Errors errors, QuickForm quickForm, int i) {
        if (quickForm.getPhoneModels().get(i) != null && !quickForm.getPhoneModels().get(i).isEmpty()) {
            checkModel(errors, quickForm, i);
        }
    }

    private void checkModel(Errors errors, QuickForm quickForm, int i) {
        Optional<Phone> phone = phoneDao.get(quickForm.getPhoneModels().get(i));
        if (phone.isPresent()) {
            checkQuantity(errors, quickForm, i, phone.get());
        } else {
            errors.rejectValue("phoneModels[" + i + "]", PHONE_NOT_FOUND, "Product not found.");
        }
    }

    private void checkQuantity(Errors errors, QuickForm quickForm, int i, Phone phone) {
        long quantity;
        try {
            quantity = Long.parseLong(quickForm.getQuantities().get(i));
            if (quantity <= 0) {
                errors.rejectValue("quantities[" + i + "]", QUANTITY_IS_NEGATIVE, "Enter number more than 0.");
            }
            checkStock(phone.getId(), quantity, errors, i);
        } catch (NumberFormatException e) {
            errors.rejectValue("quantities[" + i + "]", QUANTITY_IS_STRING, "Enter integer number.");
        }
    }

    private void checkStock(Long phoneId, Long quantity, Errors errors, int i) {
        phoneDao.get(phoneId).ifPresent(phone -> {
            Stock stock = phoneDao.findStock(phone);
            Long quantityInCart = findQuantityInCart(phoneId);
            if (stock.getStock() < quantity + quantityInCart) {
                errors.rejectValue("quantities[" + i + "]", QUANTITY_OVER_STOCK,
                        "Out of stock. Available: " + (stock.getStock() - quantityInCart) + ".");
            }
        });
    }

    private Long findQuantityInCart(Long phoneId) {
        Cart cart = cartService.getCart();
        Optional<Long> optionalQuantity = cart.getItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .map(CartItem::getQuantity).findAny();
        return optionalQuantity.orElse(0L);
    }
}
