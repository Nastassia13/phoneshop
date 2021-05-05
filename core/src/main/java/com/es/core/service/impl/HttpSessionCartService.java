package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private PhoneDao phoneDao;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(CartItem cartItem) {
        Optional<CartItem> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getPhone().getId().equals(cartItem.getPhone().getId()))
                .findAny();
        if (!optionalCartItem.isPresent()) {
            cart.getItems().add(cartItem);
        } else {
            long quantityInCart = optionalCartItem.get().getQuantity();
            int index = findIndexById(cartItem.getPhone().getId());
            cart.getItems().set(index, new CartItem(cartItem.getPhone(), cartItem.getQuantity() + quantityInCart));
        }
        recalculateCart();
    }

    @Override
    public void update(List<CartItem> items) {
        for (CartItem item : items) {
            int index = findIndexById(item.getPhone().getId());
            cart.getItems().set(index, item);
        }
        recalculateCart();
    }

    @Override
    public void remove(Long phoneId) {
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getPhone().getId().equals(phoneId))
                .findAny()
                .get();
        cart.getItems().remove(cartItem);
        recalculateCart();
    }

    @Override
    public void clearCart() {
        cart.getItems().clear();
        recalculateCart();
    }

    private int findIndexById(Long phoneId) {
        return cart.getItems().stream().map(CartItem::getPhone)
                .map(Phone::getId)
                .collect(Collectors.toList())
                .indexOf(phoneId);
    }

    private void recalculateCart() {
        cart.setTotalCost(cart.getItems().stream().map(item -> {
            Optional<Phone> phone = phoneDao.get(item.getPhone().getId());
            if (!phone.isPresent()) {
                remove(item.getPhone().getId());
                return BigDecimal.ZERO;
            }
            return phone.get().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add));
        cart.setTotalQuantity(cart.getItems().stream().mapToLong(CartItem::getQuantity).sum());
    }
}
