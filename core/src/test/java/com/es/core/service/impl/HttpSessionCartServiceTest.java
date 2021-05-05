package com.es.core.service.impl;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@WebAppConfiguration
public class HttpSessionCartServiceTest {
    @Resource
    private Cart cart;
    @Resource
    private HttpSessionCartService service;
    private Long quantityInCart = 5L;

    @Before
    public void init() {
        Long phoneId = 1002L;
        service.addPhone(new CartItem(phoneId, quantityInCart));
    }

    @After
    public void destroy() {
        service.clearCart();
    }

    @Test
    public void testGetCart() {
        assertEquals(cart, service.getCart());
    }

    @Test
    public void testAddNotExist() {
        Long phoneId = 1001L;
        Long quantity = 3L;
        service.addPhone(new CartItem(phoneId, quantity));
        Long addedQuantity = findQuantityInCart(phoneId);
        assertEquals(quantity, addedQuantity);
        assertEquals(quantity+quantityInCart, cart.getTotalQuantity());
        assertEquals(2080, cart.getTotalCost().longValue());
    }

    @Test
    public void testAddExist() {
        Long phoneId = 1002L;
        Long quantity = 3L;
        service.addPhone(new CartItem(phoneId, quantity));
        Long addedQuantity = findQuantityInCart(phoneId);
        assertEquals(quantity+quantityInCart, addedQuantity.longValue());
        assertEquals(8, cart.getTotalQuantity());
        assertEquals(1600, cart.getTotalCost().longValue());
    }

    @Test
    public void testUpdate() {
        service.addPhone(new CartItem(1003L, 7L));
        List<CartItem> items = new ArrayList<>(Arrays.asList(
                new CartItem(1002L, 3L),
                new CartItem(1003L, 2L)));
        service.update(items);
        assertEquals(2, cart.getItems().size());
        assertEquals(5, cart.getTotalQuantity());
        assertEquals(1098, cart.getTotalCost().longValue());
    }

    @Test
    public void testRemove() {
        Long productId = 1002L;
        service.remove(productId);
        assertFalse(cart.getItems().stream().map(CartItem::getPhoneId)
                .collect(Collectors.toList())
                .contains(productId));
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(0, cart.getTotalCost().longValue());
    }

    @Test
    public void testCleanCart() {
        service.clearCart();
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(0, cart.getTotalCost().longValue());
    }

    private Long findQuantityInCart(Long phoneId) {
        return cart.getItems().stream()
                .filter(item -> item.getPhoneId().equals(phoneId))
                .findAny()
                .get()
                .getQuantity();
    }
}