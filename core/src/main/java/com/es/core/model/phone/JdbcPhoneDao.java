package com.es.core.model.phone;

import com.es.core.exception.ArgumentIsNullException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String PHONE_BY_ID = "select * from phones p left join phone2color p2c on p.id = p2c.phoneId left join colors c on p2c.colorId = c.id where p.id = ?";
    private static final String ALL_PHONES = "select * from (select * from phones limit ? offset ?) p left join phone2color p2c on p.id = p2c.phoneId left join colors c on p2c.colorId = c.id";
    private static final String UPDATE_PHONE = "update phones set brand = ?, model = ?, price = ?, displaySizeInches = ?, weightGr = ?, lengthMm = ?, widthMm = ?, heightMm = ?, announced = ?, deviceType = ?, os = ?, displayResolution = ?, pixelDensity = ?, displayTechnology = ?, backCameraMegapixels = ?, frontCameraMegapixels = ?, ramGb = ?, internalStorageGb = ?, batteryCapacityMah = ?, talkTimeHours = ?, standByTimeHours = ?, bluetooth = ?, positioning = ?, imageUrl = ?, description = ? where id = ?";
    private static final String DELETE_PHONE2COLOR = "delete from phone2color where phoneId = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;

    public Optional<Phone> get(final Long key) {
        return jdbcTemplate.query(PHONE_BY_ID, new PhoneResultSetExtractor(), key).stream().findAny();
    }

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

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(ALL_PHONES, new PhoneResultSetExtractor(), limit, offset);
    }

    private void checkParameters(Phone phone) {
        if (phone == null) {
            throw new ArgumentIsNullException("Phone is null!");
        }
        if (phone.getBrand() == null) {
            throw new ArgumentIsNullException("Brand is null!");
        }
        if (phone.getModel() == null) {
            throw new ArgumentIsNullException("Model is null!");
        }
    }

    private void updatePhone(Phone phone) {
        jdbcTemplate.update(UPDATE_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(), phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(), phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(), phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(), phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(), phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(), phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription(), phone.getId());
        jdbcTemplate.update(DELETE_PHONE2COLOR, phone.getId());
        insertColors(phone.getId(), phone.getColors());
    }

    private void insertPhone(Phone phone) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("phones")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = convertPhoneToMap(phone);
        Long phoneId = jdbcInsert.executeAndReturnKey(parameters).longValue();
        insertColors(phoneId, phone.getColors());
    }

    private Map<String, Object> convertPhoneToMap(Phone phone) {
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

    private void insertColors(Long phoneId, Set<Color> colors) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("phone2color");
        List<Long> colorIds = colors.stream()
                .map(Color::getId)
                .collect(Collectors.toList());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("phoneId", phoneId);
        for (Long colorId : colorIds) {
            parameters.put("colorId", colorId);
            jdbcInsert.execute(parameters);
        }
    }
}
