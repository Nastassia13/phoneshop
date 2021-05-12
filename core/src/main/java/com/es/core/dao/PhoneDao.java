package com.es.core.dao;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;
import com.es.core.model.phone.Stock;

import java.lang.reflect.Field;
import java.util.*;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit, String query, SortField sortField, SortOrder sortOrder);

    int countPages(int limit, String query);

    Stock findStock(Phone phone);

    default Map<String, Object> convertPhoneToMap(Phone phone) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = phone.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(phone));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return map;
    }
}
