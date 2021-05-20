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

public class OrderResultSetExtractor implements ResultSetExtractor<Order> {
    @Override
    public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Order order = new Order();
        Map<Long, CartItem> cartItemMap = new HashMap<>();
        while (resultSet.next()) {
            doNext(order, resultSet, cartItemMap);
        }
        order.setItems(new ArrayList<>(cartItemMap.values()));
        return order;
    }

    private void doNext(Order order, ResultSet resultSet, Map<Long, CartItem> cartItemMap) throws SQLException {
        if (order.getId() == null) {
            setFieldValues(order, resultSet);
        }
        Long phoneId = resultSet.getLong("phoneId");
        if (!cartItemMap.containsKey(phoneId)) {
            cartItemMap.put(phoneId,
                    new CartItem(createPhone(phoneId, resultSet), resultSet.getLong("quantity")));
        }
        Color color = findColor(resultSet);
        if (color.getCode() != null) {
            cartItemMap.get(phoneId).getPhone().getColors().add(color);
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
        order.setId(resultSet.getLong("id"));
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
    }

    private Color findColor(ResultSet resultSet) throws SQLException, DataAccessException {
        Long colorId = resultSet.getLong("colorId");
        String colorCode = resultSet.getString("code");
        return new Color(colorId, colorCode);
    }
}