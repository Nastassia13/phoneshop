package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.PhoneDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.phone.Stock;
import com.es.core.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
@PropertySource("classpath:application.properties")
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private HttpSessionCartService cartService;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order getOrder(String secureId) {
        return orderDao.getOrder(secureId);
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        cartService.clearCart();
        for (CartItem item : order.getItems()) {
            Stock stock = phoneDao.findStock(item.getPhone());
            phoneDao.updateStock(item.getPhone().getId(), stock.getStock() - item.getQuantity());
        }
    }

    @Override
    public void checkStock(Order order) throws OutOfStockException {
        for (CartItem item : order.getItems()) {
            Stock stock = phoneDao.findStock(item.getPhone());
            if (stock.getStock() < item.getQuantity()) {
                throw new OutOfStockException(item.getPhone(), item.getQuantity(), stock.getStock());
            }
        }
    }
}
