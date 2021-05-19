package com.es.core.dao;

import com.es.core.model.order.Order;

public interface OrderDao {
    void save(Order order);

    Order getOrder(String secureId);
}
