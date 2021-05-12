package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;

public interface OrderService {
    Order getOrder(String secureId);

    Order createOrder(Cart cart);

    void placeOrder(Order order);

    void checkStock(Order order) throws OutOfStockException;
}
