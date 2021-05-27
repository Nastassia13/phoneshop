package com.es.core.dao.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.dao.PhoneResultSetExtractor;
import com.es.core.dao.StockRowMapper;
import com.es.core.model.phone.*;
import com.es.core.utils.Converter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String PHONE_BY_ID = "select * from phones p left join phone2color p2c on p.id = p2c.phoneId left join colors c on p2c.colorId = c.id where p.id = ?";
    private static final String ALL_PHONES = "select * from (select * from phones left join stocks on id = phoneId where stock > 0 and price is not null %s%s%s) p left join phone2color p2c on p.id = p2c.phoneId left join colors c on p2c.colorId = c.id";
    private static final String UPDATE_PHONE = "update phones set brand = ?, model = ?, price = ?, displaySizeInches = ?, weightGr = ?, lengthMm = ?, widthMm = ?, heightMm = ?, announced = ?, deviceType = ?, os = ?, displayResolution = ?, pixelDensity = ?, displayTechnology = ?, backCameraMegapixels = ?, frontCameraMegapixels = ?, ramGb = ?, internalStorageGb = ?, batteryCapacityMah = ?, talkTimeHours = ?, standByTimeHours = ?, bluetooth = ?, positioning = ?, imageUrl = ?, description = ? where id = ?";
    private static final String DELETE_PHONE2COLOR = "delete from phone2color where phoneId = ?";
    private static final String COUNT_PHONES = "select count(*) from phones left join stocks on id = phoneId where stock > 0 and price is not null %s";
    private static final String GET_STOCK = "select * from stocks where phoneId = ?";
    private static final String UPDATE_STOCK = "update stocks set stock = ? where phoneId = ?";
    private static final String PHONE_BY_MODEL = "select * from phones p left join phone2color p2c on p.id = p2c.phoneId left join colors c on p2c.colorId = c.id where p.model = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SimpleJdbcInsert jdbcInsertPhone;
    @Resource
    private SimpleJdbcInsert jdbcInsertPhone2Color;

    @Override
    public Optional<Phone> get(final Long key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null!");
        }
        return jdbcTemplate.query(PHONE_BY_ID, new PhoneResultSetExtractor(), key).stream().findAny();
    }

    @Override
    public Optional<Phone> get(String model) {
        if (model == null) {
            throw new IllegalArgumentException("Model is null!");
        }
        return jdbcTemplate.query(PHONE_BY_MODEL, new PhoneResultSetExtractor(), model).stream().findAny();
    }

    @Override
    public void save(final Phone phone) {
        checkParameters(phone);
        Optional<Phone> optionalPhone = Optional.empty();
        if (phone.getId() != null) {
            optionalPhone = get(phone.getId());
        }
        if (optionalPhone.isPresent()) {
            updatePhone(phone);
        } else {
            insertPhone(phone);
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortField sortField, SortOrder sortOrder) {
        String sqlQuery = createSqlQueryAllPhones(offset, limit, query, sortField, sortOrder);
        return jdbcTemplate.query(sqlQuery, new PhoneResultSetExtractor());
    }

    @Override
    public int countPages(int limit, String query) {
        String sqlQuery = String.format(COUNT_PHONES, createSqlQueryLike(query));
        long countPhones = jdbcTemplate.queryForObject(sqlQuery, Long.class);
        return (int) Math.ceil((double) countPhones / limit);
    }

    @Override
    public Stock findStock(Phone phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone is null!");
        }
        Optional<Stock> stock = jdbcTemplate.query(GET_STOCK, new StockRowMapper(), phone.getId()).stream().findAny();
        if (!stock.isPresent()) {
            stock = Optional.of(new Stock());
            stock.get().setStock(0L);
            stock.get().setReserved(0L);
        }
        stock.get().setPhone(phone);
        return stock.get();
    }

    @Override
    public void updateStock(Long phoneId, Long quantity) {
        if (phoneId == null) {
            throw new IllegalArgumentException("PhoneId is null!");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity is null!");
        }
        jdbcTemplate.update(UPDATE_STOCK, quantity, phoneId);
    }

    private void checkParameters(Phone phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone is null!");
        }
        if (phone.getBrand() == null) {
            throw new IllegalArgumentException("Brand is null!");
        }
        if (phone.getModel() == null) {
            throw new IllegalArgumentException("Model is null!");
        }
    }

    private void updatePhone(Phone phone) {
        jdbcTemplate.update(UPDATE_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(), phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(), phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(), phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(), phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(), phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(), phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription(), phone.getId());
        jdbcTemplate.update(DELETE_PHONE2COLOR, phone.getId());
        insertColors(phone.getId(), phone.getColors());
    }

    private void insertPhone(Phone phone) {
        jdbcInsertPhone.withTableName("phones").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new Converter().convertObjectToMap(phone);
        Long phoneId = jdbcInsertPhone.executeAndReturnKey(parameters).longValue();
        insertColors(phoneId, phone.getColors());
    }

    private void insertColors(Long phoneId, Set<Color> colors) {
        jdbcInsertPhone2Color.withTableName("phone2color");
        List<Long> colorIds = colors.stream()
                .map(Color::getId)
                .collect(Collectors.toList());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("phoneId", phoneId);
        for (Long colorId : colorIds) {
            parameters.put("colorId", colorId);
            jdbcInsertPhone2Color.execute(parameters);
        }
    }

    private String createSqlQueryAllPhones(int offset, int limit, String query, SortField sortField, SortOrder sortOrder) {
        String sqlSort = (sortField != null && sortOrder != null) ? " order by " + sortField + " " + sortOrder : "";
        String sqlLimit = " limit " + limit + " offset " + offset;
        return String.format(ALL_PHONES, createSqlQueryLike(query), sqlSort, sqlLimit);
    }

    private String createSqlQueryLike(String query) {
        String sqlLike = "and (lower(model) like lower('";
        String[] words = query == null ? new String[]{""} : query.split("\\s");
        sqlLike += "%" + words[0];
        for (int i = 1; i < words.length; i++) {
            sqlLike += "%') or lower(model) like lower('%" + words[i];
        }
        sqlLike += "%'))";
        return sqlLike;
    }
}
