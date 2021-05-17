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

    @Test
    public void testGetOrder() {
        Order order = orderDao.getOrder("l123").get();
        assertNotNull(order);
        assertEquals(1L, order.getId().longValue());
        assertTrue(order.getItems().size() != 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOrderNullSecureId() {
        Order order = orderDao.getOrder(null).get();
    }

    @Test
    public void testSaveOrder() {
        Order order = createOrder();
        orderDao.save(order);
        assertEquals(2L, orderDao.getOrder("f567").get().getId().longValue());
        assertNotNull(orderDao.getOrder("f567").get().getSecureId());
        List<Long> phones = jdbcTemplate.queryForList(PHONE_BY_ORDER, Long.class, 2L);
        assertEquals(2, phones.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullOrder() {
        orderDao.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullFirstName() {
        Order order = createOrder();
        order.setFirstName(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullLastName() {
        Order order = createOrder();
        order.setLastName(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullPhone() {
        Order order = createOrder();
        order.setContactPhoneNo(null);
        orderDao.save(order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveOrderNullAddress() {
        Order order = createOrder();
        order.setDeliveryAddress(null);
        orderDao.save(order);
    }

    private Order createOrder() {
        Order order = new Order();
        order.setSecureId("f567");
        order.setSubtotal(BigDecimal.valueOf(300));
        order.setDeliveryPrice(BigDecimal.valueOf(50));
        order.setTotalPrice(BigDecimal.valueOf(350));
        order.setFirstName("aaa");
        order.setLastName("ppp");
        order.setDeliveryAddress("Minsk");
        order.setContactPhoneNo("58697");
        order.setStatus(OrderStatus.NEW);
        order.setItems(Arrays.asList(createCartItem(1002L, 5L), createCartItem(1004L, 3L)));
        return order;
    }

    private CartItem createCartItem(Long id, Long quantity) {
        Phone phone = new Phone();
        phone.setId(id);
        return new CartItem(phone, quantity);
    }
}
