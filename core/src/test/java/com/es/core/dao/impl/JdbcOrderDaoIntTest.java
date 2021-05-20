package com.es.core.dao.impl;

import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcOrderDaoIntTest {
    @Resource
    private JdbcOrderDao orderDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    private static final String PHONE_BY_ORDER = "select phoneId from phone2order where orderId = ?";
    private String orderSecureId = "f567";

    @Test
    public void testGetOrderTest() {
        Order order = orderDao.getOrder("l123");
        assertNotNull(order);
        assertEquals(1L, order.getId().longValue());
        assertTrue(order.getItems().size() != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderNullSecureIdTest() {
        Order order = orderDao.getOrder(null);
    }

    @Test
    public void testGetOrderByIdTest() {
        Order order = orderDao.getOrderById(1L);
        assertNotNull(order);
        assertEquals(1L, order.getId().longValue());
        assertTrue(order.getItems().size() != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderByIdNullSecureIdTest() {
        Order order = orderDao.getOrderById(null);
    }

    @Test
    public void testSaveOrderTest() {
        Order order = createOrder();
        orderDao.save(order);
        assertEquals(4L, orderDao.getOrder(orderSecureId).getId().longValue());
        assertNotNull(orderDao.getOrder(orderSecureId).getSecureId());
        List<Long> phones = jdbcTemplate.queryForList(PHONE_BY_ORDER, Long.class, 4L);
        assertEquals(2, phones.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullOrderTest() {
        orderDao.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullFirstNameTest() {
        Order order = createOrder();
        order.setFirstName(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullLastNameTest() {
        Order order = createOrder();
        order.setLastName(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullPhoneTest() {
        Order order = createOrder();
        order.setContactPhoneNo(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullAddressTest() {
        Order order = createOrder();
        order.setDeliveryAddress(null);
        orderDao.save(order);
    }

    @Test
    public void findAllOrdersTest() {
        List<Order> orders = orderDao.findAllOrders();
        assertNotNull(orders);
        assertEquals(3, orders.size());
    }

    @Test
    public void setStatusTest() {
        orderDao.setStatus(1L, OrderStatus.DELIVERED);
        Order order = orderDao.getOrderById(1L);
        assertEquals(OrderStatus.DELIVERED.toString(), order.getStatus().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setStatusOrderIdNullTest() {
        orderDao.setStatus(null, OrderStatus.DELIVERED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setStatusOrderStatusNullTest() {
        orderDao.setStatus(1L, null);
    }

    private Order createOrder() {
        Order order = new Order();
        order.setSecureId(orderSecureId);
        order.setSubtotal(BigDecimal.valueOf(300));
        order.setDeliveryPrice(BigDecimal.valueOf(50));
        order.setTotalPrice(BigDecimal.valueOf(350));
        order.setFirstName("aaa");
        order.setLastName("ppp");
        order.setDeliveryAddress("Minsk");
        order.setContactPhoneNo("58697");
        order.setStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(Arrays.asList(createCartItem(1002L, 5L), createCartItem(1004L, 3L)));
        return order;
    }

    private CartItem createCartItem(Long id, Long quantity) {
        Phone phone = new Phone();
        phone.setId(id);
        return new CartItem(phone, quantity);
    }
}
