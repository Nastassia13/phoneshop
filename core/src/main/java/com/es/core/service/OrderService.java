package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {
    Order getOrder(String secureId);

    Order getOrderById(Long id);

    Order createOrder(Cart cart);

    void placeOrder(Order order);

    void checkStock(Order order) throws OutOfStockException;

    List<Order> findAllOrders();

    void setStatus(Long orderId, OrderStatus orderStatus);
}
