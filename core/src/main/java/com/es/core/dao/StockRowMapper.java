package com.es.core.dao;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockRowMapper implements RowMapper<Stock> {
    @Override
    public Stock mapRow(ResultSet resultSet, int i) throws SQLException {
        Stock stock = new Stock();
        Phone phone = new Phone();
        phone.setId(resultSet.getLong("phoneId"));
        stock.setPhone(phone);
        stock.setStock(resultSet.getInt("stock"));
        stock.setReserved(resultSet.getInt("reserved"));
        return stock;
    }
}
