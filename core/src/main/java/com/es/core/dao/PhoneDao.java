package com.es.core.dao;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;
import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    Optional<Phone> get(String model);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit, String query, SortField sortField, SortOrder sortOrder);

    int countPages(int limit, String query);

    Stock findStock(Phone phone);

    void updateStock(Long phoneId, Long quantity);
}
