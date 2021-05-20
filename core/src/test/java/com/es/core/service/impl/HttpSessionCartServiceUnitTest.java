package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceUnitTest {
    private static final Long PHONE_ID_1 = 1L;
    private static final Long PHONE_ID_2 = 2L;
    private static final Long PHONE_ID_3 = 3L;
    private static final Long PHONE_ID_4 = 4L;

    private static final Long QUANTITY_1 = 5L;
    private static final Long QUANTITY_2 = 1L;
    private static final Long QUANTITY_3 = 3L;
    private static final Long QUANTITY_4 = 2L;

    private static final BigDecimal PRICE_1 = BigDecimal.valueOf(200);
    private static final BigDecimal PRICE_2 = BigDecimal.valueOf(150);
    private static final BigDecimal PRICE_3 = BigDecimal.valueOf(300);
    private static final BigDecimal PRICE_4 = BigDecimal.valueOf(100);

    private List<CartItem> cartItems;

    @Mock
    private Cart cart;
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Mock
    private Phone phone3;
    @Mock
    private Phone phone4;

    @InjectMocks
    private HttpSessionCartService cartService = new HttpSessionCartService();

    @Before
    public void init() {
        when(phone1.getId()).thenReturn(PHONE_ID_1);
        when(phone2.getId()).thenReturn(PHONE_ID_2);
        when(phone3.getId()).thenReturn(PHONE_ID_3);
        when(phone4.getId()).thenReturn(PHONE_ID_4);
        when(phone1.getPrice()).thenReturn(PRICE_1);
        when(phone2.getPrice()).thenReturn(PRICE_2);
        when(phone3.getPrice()).thenReturn(PRICE_3);
        when(phone4.getPrice()).thenReturn(PRICE_4);
        when(phoneDao.get(PHONE_ID_1)).thenReturn(Optional.of(phone1));
        when(phoneDao.get(PHONE_ID_2)).thenReturn(Optional.of(phone2));
        when(phoneDao.get(PHONE_ID_3)).thenReturn(Optional.of(phone3));
        when(phoneDao.get(PHONE_ID_4)).thenReturn(Optional.of(phone4));
        cartItems = new ArrayList<>(Arrays.asList(
                new CartItem(phone1, QUANTITY_1),
                new CartItem(phone2, QUANTITY_2),
                new CartItem(phone3, QUANTITY_3)));
        when(cart.getItems()).thenReturn(cartItems);
        when(cart.getTotalCost()).thenReturn(BigDecimal.valueOf(2050));
        when(cart.getTotalQuantity()).thenReturn(9L);
    }

    @Test
    public void testGetCartTest() {
        Cart cartActual = cartService.getCart();
        assertEquals(cart, cartActual);
        assertEquals(9L, cartActual.getTotalQuantity().longValue());
        assertEquals(BigDecimal.valueOf(2050), cartActual.getTotalCost());
    }

    @Test
    public void testAddPhoneNotExistTest() {
        CartItem cartItem = new CartItem(phone4, QUANTITY_4);
        cartService.addPhone(cartItem);
        assertTrue(cartService.getCart().getItems().contains(cartItem));
        assertEquals(4, cartService.getCart().getItems().size());
        assertEquals(11, cartService.getCart().getTotalQuantity() + QUANTITY_4);
        assertEquals(BigDecimal.valueOf(2250),
                cartService.getCart().getTotalCost().add(PRICE_4.multiply(BigDecimal.valueOf(QUANTITY_4))));
    }

    @Test
    public void testAddExistTest() {
        CartItem cartItem = new CartItem(phone3, QUANTITY_4);
        cartService.addPhone(cartItem);
        assertEquals(3, cartService.getCart().getItems().size());
        assertEquals(11, cartService.getCart().getTotalQuantity() + QUANTITY_4);
        assertEquals(BigDecimal.valueOf(2650),
                cartService.getCart().getTotalCost().add(PRICE_3.multiply(BigDecimal.valueOf(QUANTITY_4))));
    }

    @Test
    public void testUpdateTest() {
        List<CartItem> items = new ArrayList<>(Arrays.asList(
                new CartItem(phone1, QUANTITY_4),
                new CartItem(phone2, QUANTITY_3),
                new CartItem(phone3, QUANTITY_1)));
        cartService.update(items);
        assertEquals(3, cartService.getCart().getItems().size());
        assertEquals(QUANTITY_4, cartService.getCart().getItems().get(0).getQuantity());
        assertEquals(QUANTITY_3, cartService.getCart().getItems().get(1).getQuantity());
        assertEquals(QUANTITY_1, cartService.getCart().getItems().get(2).getQuantity());
        assertEquals(10L, calculateTotalZeroQuantity() + QUANTITY_4 + QUANTITY_3 + QUANTITY_1);
        assertEquals(BigDecimal.valueOf(2350), calculateTotalZeroCost().add(PRICE_1.multiply(BigDecimal.valueOf(QUANTITY_4)))
                .add(PRICE_2.multiply(BigDecimal.valueOf(QUANTITY_3)))
                .add(PRICE_3.multiply(BigDecimal.valueOf(QUANTITY_1))));
    }

    @Test
    public void testRemoveTest() {
        cartService.remove(PHONE_ID_1);
        assertFalse(cart.getItems().stream().map(CartItem::getPhone)
                .collect(Collectors.toList())
                .contains(phone1));
        assertEquals(4, cart.getTotalQuantity() - QUANTITY_1);
        assertEquals(BigDecimal.valueOf(1050),
                cart.getTotalCost().subtract(PRICE_1.multiply(BigDecimal.valueOf(QUANTITY_1))));
    }

    @Test
    public void testCleanCartTest() {
        cartService.clearCart();
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0L, calculateTotalZeroQuantity().longValue());
        assertEquals(BigDecimal.ZERO, calculateTotalZeroCost());
    }

    private Long calculateTotalZeroQuantity() {
        return cart.getTotalQuantity() - QUANTITY_1 - QUANTITY_2 - QUANTITY_3;
    }

    private BigDecimal calculateTotalZeroCost() {
        return cart.getTotalCost().subtract(PRICE_1.multiply(BigDecimal.valueOf(QUANTITY_1)))
                .subtract(PRICE_2.multiply(BigDecimal.valueOf(QUANTITY_2)))
                .subtract(PRICE_3.multiply(BigDecimal.valueOf(QUANTITY_3)));
    }
}