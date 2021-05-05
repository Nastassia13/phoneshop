package com.es.core.service;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;

import java.util.List;

public interface CartService {
    Cart getCart();

    void addPhone(CartItem item);

    void update(List<CartItem> items);

    void remove(Long phoneId);

    void clearCart();
}
