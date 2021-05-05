package com.es.core.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PhoneResultSetExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Phone> phoneMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            Long phoneId = resultSet.getLong("id");
            Phone phone = phoneMap.computeIfAbsent(phoneId, id -> createPhone(id, resultSet));
            Color color = findColor(resultSet);
            if (color.getCode() != null) {
                phone.getColors().add(color);
            }
        }
        return new ArrayList<>(phoneMap.values());
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

    private Color findColor(ResultSet resultSet) throws SQLException, DataAccessException {
        Long colorId = resultSet.getLong("colorId");
        String colorCode = resultSet.getString("code");
        return new Color(colorId, colorCode);
    }
}