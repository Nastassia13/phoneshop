package com.es.core.service.impl;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@WebAppConfiguration
public class OrderServiceImplTest {
    @Resource
    private OrderServiceImpl orderService;
    @Resource
    private Cart cart;

    @Test
    public void getOrderTest() {
        String secureId = "l123";
        assertEquals(1L, orderService.getOrder(secureId).getId().longValue());
    }

    @Test
    public void createOrderTest() {
        assertEquals(cart.getItems(), orderService.createOrder(cart).getItems());
    }

    @Test
    public void placeOrderTest() {
        Order order = orderService.createOrder(cart);
        setFields(order);
        orderService.placeOrder(order);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void checkStockTest() throws OutOfStockException {
        Order order = createOrder(5L);
        orderService.checkStock(order);
    }

    @Test(expected = OutOfStockException.class)
    public void checkStockTestOutOfStock() throws OutOfStockException {
        Order order = createOrder(20L);
        orderService.checkStock(order);
    }

    private Order createOrder(Long quantity) {
        Order order = new Order();
        order.setItems(new ArrayList<>());
        Phone phone = new Phone();
        phone.setId(1001L);
        order.getItems().add(new CartItem(phone, quantity));
        return order;
    }

    private void setFields(Order order) {
        order.setSecureId("f567");
        order.setSubtotal(BigDecimal.valueOf(300));
        order.setDeliveryPrice(BigDecimal.valueOf(50));
        order.setTotalPrice(BigDecimal.valueOf(350));
        order.setFirstName("aaa");
        order.setLastName("ppp");
        order.setDeliveryAddress("Minsk");
        order.setContactPhoneNo("58697");
        order.setStatus(OrderStatus.NEW);
    }
}