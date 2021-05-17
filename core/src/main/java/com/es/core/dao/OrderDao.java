package com.es.core.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    void save(Order order);

    Optional<Order> getOrder(String secureId);

    Optional<Order> getOrderById(Long id);

    List<Order> findAllOrders();

    void setStatus(Long orderId, OrderStatus orderStatus);
}
