package com.es.core.dao;

import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {
    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Order> orders = new HashMap<>();
        Map<Long, Phone> phoneMap = new LinkedHashMap<>();
        Map<Long, Long> quantityMap = new LinkedHashMap<>();
        Long prevId = null;
        while (resultSet.next()) {
            prevId = doNext(resultSet, orders, phoneMap, quantityMap, prevId);
        }
        if (orders.containsKey(prevId)) {
            orders.get(prevId).setItems(findCartItems(phoneMap, quantityMap));
        }
        return new ArrayList<>(orders.values());
    }

    private Long doNext(ResultSet resultSet, Map<Long, Order> orders, Map<Long, Phone> phoneMap,
                        Map<Long, Long> quantityMap, Long prevId) throws SQLException {
        Long orderId = resultSet.getLong("id");
        checkPrevId(orders, phoneMap, quantityMap, prevId, orderId);
        Order order = orders.computeIfAbsent(orderId, id -> createOrder(orderId, resultSet));
        addColorToPhone(resultSet, phoneMap, quantityMap);
        prevId = order.getId();
        return prevId;
    }

    private void checkPrevId(Map<Long, Order> orders, Map<Long, Phone> phoneMap, Map<Long, Long> quantityMap, Long prevId, Long orderId) {
        if (prevId != null && !prevId.equals(orderId)) {
            orders.get(prevId).setItems(findCartItems(phoneMap, quantityMap));
            phoneMap.clear();
            quantityMap.clear();
        }
    }

    private Order createOrder(Long id, ResultSet resultSet) {
        Order order = new Order();
        try {
            order.setId(id);
            setFieldValues(order, resultSet);
            order.setItems(new ArrayList<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    private void addColorToPhone(ResultSet resultSet, Map<Long, Phone> phoneMap, Map<Long, Long> quantityMap) throws SQLException {
        Long phoneId = resultSet.getLong("phoneId");
        quantityMap.put(phoneId, resultSet.getLong("quantity"));
        Phone phone = phoneMap.computeIfAbsent(phoneId, id -> createPhone(id, resultSet));
        Color color = findColor(resultSet);
        if (color.getCode() != null) {
            phone.getColors().add(color);
        }
    }

    private Phone createPhone(Long phoneId, ResultSet resultSet) {
        Phone phone = new Phone();
        try {
            phone.setId(phoneId);
            setFieldValues(phone, resultSet);
            phone.setColors(new HashSet<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return phone;
    }

    private void setFieldValues(Phone phone, ResultSet resultSet) throws SQLException, DataAccessException {
        phone.setBrand(resultSet.getString("brand"));
        phone.setModel(resultSet.getString("model"));
        phone.setPrice(resultSet.getBigDecimal("price"));
        phone.setDisplaySizeInches(resultSet.getBigDecimal("displaySizeInches"));
        phone.setWeightGr(resultSet.getInt("weightGr"));
        phone.setLengthMm(resultSet.getBigDecimal("lengthMm"));
        phone.setWidthMm(resultSet.getBigDecimal("widthMm"));
        phone.setHeightMm(resultSet.getBigDecimal("heightMm"));
        phone.setAnnounced(resultSet.getDate("announced"));
        phone.setDeviceType(resultSet.getString("deviceType"));
        phone.setOs(resultSet.getString("os"));
        phone.setDisplayResolution(resultSet.getString("displayResolution"));
        phone.setPixelDensity(resultSet.getInt("pixelDensity"));
        phone.setDisplayTechnology(resultSet.getString("displayTechnology"));
        phone.setBackCameraMegapixels(resultSet.getBigDecimal("backCameraMegapixels"));
        phone.setFrontCameraMegapixels(resultSet.getBigDecimal("frontCameraMegapixels"));
        phone.setRamGb(resultSet.getBigDecimal("ramGb"));
        phone.setInternalStorageGb(resultSet.getBigDecimal("internalStorageGb"));
        phone.setBatteryCapacityMah(resultSet.getInt("batteryCapacityMah"));
        phone.setTalkTimeHours(resultSet.getBigDecimal("talkTimeHours"));
        phone.setStandByTimeHours(resultSet.getBigDecimal("standByTimeHours"));
        phone.setBluetooth(resultSet.getString("bluetooth"));
        phone.setPositioning(resultSet.getString("positioning"));
        phone.setImageUrl(resultSet.getString("imageUrl"));
        phone.setDescription(resultSet.getString("description"));
    }

    private void setFieldValues(Order order, ResultSet resultSet) throws SQLException, DataAccessException {
        order.setSecureId(resultSet.getString("secureId"));
        order.setSubtotal(resultSet.getBigDecimal("subtotal"));
        order.setDeliveryPrice(resultSet.getBigDecimal("deliveryPrice"));
        order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
        order.setFirstName(resultSet.getString("firstName"));
        order.setLastName(resultSet.getString("lastName"));
        order.setDeliveryAddress(resultSet.getString("deliveryAddress"));
        order.setContactPhoneNo(resultSet.getString("contactPhoneNo"));
        order.setAdditionalInformation(resultSet.getString("additionalInformation"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setOrderDate(resultSet.getTimestamp("orderDate").toLocalDateTime());
    }

    private Color findColor(ResultSet resultSet) throws SQLException, DataAccessException {
        Long colorId = resultSet.getLong("colorId");
        String colorCode = resultSet.getString("code");
        return new Color(colorId, colorCode);
    }

    private List<CartItem> findCartItems(Map<Long, Phone> phoneMap, Map<Long, Long> quantityMap) {
        List<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<Long, Phone> entry : phoneMap.entrySet()) {
            cartItems.add(new CartItem(entry.getValue(), quantityMap.get(entry.getKey())));
        }
        return cartItems;
    }
}