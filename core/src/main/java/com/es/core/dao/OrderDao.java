package com.es.core.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderDao {
    void save(Order order);

    Order getOrder(String secureId);

    Order getOrderById(Long id);

    List<Order> findAllOrders();

    void setStatus(Long orderId, OrderStatus orderStatus);
}
