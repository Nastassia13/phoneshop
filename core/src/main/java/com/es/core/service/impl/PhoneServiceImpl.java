package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;
import com.es.core.model.phone.Stock;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Resource
    private PhoneDao phoneDao;

    @Override
    public Optional<Phone> getPhone(Long phoneId) {
        return phoneDao.get(phoneId);
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortField sortField, SortOrder sortOrder) {
        return phoneDao.findAll(offset, limit, query, sortField, sortOrder);
    }

    @Override
    public int countPages(int limit, String query) {
        return phoneDao.countPages(limit, query);
    }

    @Override
    public void updateStock(Long phoneId, Long quantity) {
        phoneDao.updateStock(phoneId, quantity);
    }

    @Override
    public Stock findStock(Phone phone) {
        return phoneDao.findStock(phone);
    }
}
