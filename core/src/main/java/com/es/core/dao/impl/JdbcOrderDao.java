package com.es.core.dao.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderResultSetExtractor;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.utils.Converter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JdbcOrderDao implements OrderDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SimpleJdbcInsert jdbcInsertOrder;
    @Resource
    private SimpleJdbcInsert jdbcInsertPhone2Order;
    private static final String ORDER_BY_SECURE_ID = "select * from orders o left join phone2order p2o on o.id = p2o.orderId " +
            "left join phones p on p2o.phoneId = p.id left join phone2color p2c on p.id = p2c.phoneId " +
            "left join colors c on p2c.colorId = c.id where o.secureId = ?";
    private static final String ORDER_BY_ID = "select * from orders o left join phone2order p2o on o.id = p2o.orderId " +
            "left join phones p on p2o.phoneId = p.id left join phone2color p2c on p.id = p2c.phoneId " +
            "left join colors c on p2c.colorId = c.id where o.id = ?";
    private static final String ALL_ORDERS = "select * from orders o left join phone2order p2o on o.id = p2o.orderId " +
            "left join phones p on p2o.phoneId = p.id left join phone2color p2c on p.id = p2c.phoneId " +
            "left join colors c on p2c.colorId = c.id";
    private static final String UPDATE_ORDER_STATUS = "update orders set status = ? where id = ?";

    @Override
    public void save(Order order) {
        if (!checkParameters(order)) {
            throw new IllegalArgumentException("Order or field is null!");
        }
        Map<String, Object> parameters = new Converter().convertObjectToMap(order);
        Long orderId = jdbcInsertOrder.executeAndReturnKey(parameters).longValue();
        insertPhones(orderId, order.getItems());
    }

    @Override
    public Optional<Order> getOrder(String secureId) {
        if (secureId == null) {
            throw new IllegalArgumentException("SecureId is null!");
        }
        return jdbcTemplate.query(ORDER_BY_SECURE_ID, new OrderResultSetExtractor(), secureId).stream().findAny();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is null!");
        }
        return jdbcTemplate.query(ORDER_BY_ID, new OrderResultSetExtractor(), id).stream().findAny();
    }

    @Override
    public List<Order> findAllOrders() {
        return jdbcTemplate.query(ALL_ORDERS, new OrderResultSetExtractor());
    }

    @Override
    public void setStatus(Long orderId, OrderStatus orderStatus) {
        jdbcTemplate.update(UPDATE_ORDER_STATUS, orderStatus.toString(), orderId);
    }

    private boolean checkParameters(Order order) {
        if (order == null) {
            return false;
        }
        return order.getFirstName() != null && order.getLastName() != null
                && order.getDeliveryAddress() != null && order.getContactPhoneNo() != null;
    }

    private void insertPhones(Long orderId, List<CartItem> items) {
        List<Long> phoneIds = items.stream().map(CartItem::getPhone)
                .map(Phone::getId)
                .collect(Collectors.toList());
        List<Long> quantities = items.stream().map(CartItem::getQuantity)
                .collect(Collectors.toList());
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        for (int i = 0; i < phoneIds.size(); i++) {
            parameters.put("phoneId", phoneIds.get(i));
            parameters.put("quantity", quantities.get(i));
            jdbcInsertPhone2Order.execute(parameters);
        }
    }
}
