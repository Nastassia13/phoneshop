package com.es.core.service;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> getPhone(Long phoneId);

    List<Phone> findAll(int offset, int limit, String query, SortField sortField, SortOrder sortOrder);

    int countPages(int limit, String query);

    void updateStock(Long phoneId, Long quantity);
}
