package com.es.core.dao.impl;

import com.es.core.model.phone.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcPhoneDaoIntTest {
    @Resource
    private JdbcPhoneDao phoneDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    private static final String COUNT_ROWS_IN_PHONE = "select count(*) from phones";
    private static final String LAST_PHONE_ID = "select max(id) from phones";
    private static final String COLOR_BY_PHONE = "select colorId from phone2color where phoneId = ?";

    @Test
    public void testGetPhoneNotExistTest() {
        Long id = 1010L;
        Optional<Phone> actualPhone = phoneDao.get(id);
        assertFalse(actualPhone.isPresent());
    }

    @Test
    public void testGetPhoneWithOneColorTest() {
        Long id = 1000L;
        Optional<Phone> actualPhone = phoneDao.get(id);
        assertTrue(actualPhone.isPresent());
        assertEquals(id, actualPhone.get().getId());
        assertEquals(1, actualPhone.get().getColors().size());
        assertEquals(1000L, actualPhone.get().getColors().iterator().next().getId().longValue());
    }

    @Test
    public void testGetPhoneWithManyColorsTest() {
        Long id = 1001L;
        Optional<Phone> actualPhone = phoneDao.get(id);
        assertTrue(actualPhone.isPresent());
        assertEquals(id, actualPhone.get().getId());
        assertEquals(2, actualPhone.get().getColors().size());
        List<Long> colorsId = actualPhone.get().getColors().stream().map(Color::getId).collect(Collectors.toList());
        assertTrue(colorsId.contains(1000L));
        assertTrue(colorsId.contains(1001L));
    }

    @Test
    public void testGetPhoneWithoutColorTest() {
        Long id = 1003L;
        Optional<Phone> actualPhone = phoneDao.get(id);
        assertTrue(actualPhone.isPresent());
        assertEquals(id, actualPhone.get().getId());
        assertTrue(actualPhone.get().getColors().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPhoneNullTest() {
        Long id = null;
        Optional<Phone> actualPhone = phoneDao.get(id);
    }

    @Test
    public void testSavePhoneNotExistWithoutColorsTest() {
        Phone phone = createPhone();
        long countBeforeSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        phoneDao.save(phone);
        long countAfterSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        assertEquals(1, countAfterSaving - countBeforeSaving);
        Long phoneId = jdbcTemplate.queryForObject(LAST_PHONE_ID, Long.class);
        assertNotNull(phoneId);
        List<Long> colorIds = jdbcTemplate.query(COLOR_BY_PHONE, new SingleColumnRowMapper<>(), phoneId);
        assertEquals(0, colorIds.size());
    }

    @Test
    public void testSavePhoneNotExistWithColorsTest() {
        Long colorIdFirst = 1001L;
        Long colorIdSecond = 1002L;
        Phone phone = createPhone();
        setColorsToPhone(phone, colorIdFirst, colorIdSecond);
        long countBeforeSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        phoneDao.save(phone);
        long countAfterSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        assertEquals(1, countAfterSaving - countBeforeSaving);
        Long phoneId = jdbcTemplate.queryForObject(LAST_PHONE_ID, Long.class);
        assertNotNull(phoneId);
        List<Long> colorIds = jdbcTemplate.query(COLOR_BY_PHONE, new SingleColumnRowMapper<>(), phoneId);
        assertEquals(2, colorIds.size());
        assertTrue(colorIds.contains(colorIdFirst));
        assertTrue(colorIds.contains(colorIdSecond));
    }

    @Test
    public void testSavePhoneExistWithoutColorsTest() {
        Phone phone = createPhone();
        Long phoneId = 1002L;
        phone.setId(phoneId);
        long countBeforeSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        phoneDao.save(phone);
        long countAfterSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        assertEquals(0, countAfterSaving - countBeforeSaving);
        List<Long> colorIds = jdbcTemplate.query(COLOR_BY_PHONE, new SingleColumnRowMapper<>(), phoneId);
        assertEquals(0, colorIds.size());
    }

    @Test
    public void testSavePhoneExistWithColorsTest() {
        Long colorIdFirst = 1001L;
        Long colorIdSecond = 1002L;
        Phone phone = createPhone();
        Long phoneId = 1002L;
        phone.setId(phoneId);
        setColorsToPhone(phone, colorIdFirst, colorIdSecond);
        long countBeforeSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        phoneDao.save(phone);
        long countAfterSaving = jdbcTemplate.queryForObject(COUNT_ROWS_IN_PHONE, Long.class);
        assertEquals(0, countAfterSaving - countBeforeSaving);
        List<Long> colorIds = jdbcTemplate.query(COLOR_BY_PHONE, new SingleColumnRowMapper<>(), phoneId);
        assertEquals(2, colorIds.size());
        assertTrue(colorIds.contains(colorIdFirst));
        assertTrue(colorIds.contains(colorIdSecond));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSavePhoneNullTest() {
        Phone phone = null;
        phoneDao.save(phone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSavePhoneWithNullBrandOrModelTest() {
        Phone phone = new Phone();
        phoneDao.save(phone);
    }

    @Test
    public void testFindAllByIdAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 4, "", SortField.ID, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1004L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByIdDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 4, "", SortField.ID, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1004L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllWithOffsetTest() {
        List<Phone> phones = phoneDao.findAll(2, 2, "", SortField.ID, SortOrder.ASC);
        assertEquals(2, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1004L, phones.get(1).getId().longValue());
    }

    @Test
    public void testFindAllWithBigLimitTest() {
        List<Phone> phones = phoneDao.findAll(2, 10, "", SortField.ID, SortOrder.ASC);
        assertEquals(2, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1004L, phones.get(1).getId().longValue());
    }

    @Test
    public void testFindAllWithBigOffsetTest() {
        List<Phone> phones = phoneDao.findAll(6, 10, "", SortField.ID, SortOrder.ASC);
        assertTrue(phones.isEmpty());
    }

    @Test
    public void testFindAllSortByModelAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.MODEL, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByModelDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.MODEL, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1003L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByBrandAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.BRAND, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1004L, phones.get(0).getId().longValue());
        assertEquals(1003L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByBrandDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.BRAND, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1004L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByDisplaySizeAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.DISPLAY_SIZE, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1002L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByDisplaySizeDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.DISPLAY_SIZE, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1002L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByPriceAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.PRICE, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1002L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllSortByPriceDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "", SortField.PRICE, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1002L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByModelAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.MODEL, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByModelDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.MODEL, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1003L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByBrandAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.BRAND, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1004L, phones.get(0).getId().longValue());
        assertEquals(1003L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByBrandDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.BRAND, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1003L, phones.get(0).getId().longValue());
        assertEquals(1004L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByDisplaySizeAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.DISPLAY_SIZE, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1002L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByDisplaySizeDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.DISPLAY_SIZE, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1002L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByPriceAscTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.PRICE, SortOrder.ASC);
        assertEquals(4, phones.size());
        assertEquals(1002L, phones.get(0).getId().longValue());
        assertEquals(1001L, phones.get(3).getId().longValue());
    }

    @Test
    public void testFindAllByQuerySortByPriceDescTest() {
        List<Phone> phones = phoneDao.findAll(0, 5, "1 g", SortField.PRICE, SortOrder.DESC);
        assertEquals(4, phones.size());
        assertEquals(1001L, phones.get(0).getId().longValue());
        assertEquals(1002L, phones.get(3).getId().longValue());
    }

    @Test
    public void testCountPagesWithoutQueryTest() {
        int pages = phoneDao.countPages(2, "");
        assertEquals(2, pages);
    }

    @Test
    public void testCountPagesWithQueryTest() {
        int pages = phoneDao.countPages(2, "g");
        assertEquals(1, pages);
    }

    @Test
    public void testFindStockTest() {
        Phone phone = new Phone();
        phone.setId(1001L);
        Stock stock = phoneDao.findStock(phone);
        assertNotNull(stock);
        assertEquals(1001L, stock.getPhone().getId().longValue());
        assertEquals(11, stock.getStock().intValue());
        assertEquals(0, stock.getReserved().intValue());
    }

    @Test
    public void testFindStockNullTest() {
        Phone phone = new Phone();
        phone.setId(1000L);
        Stock stock = phoneDao.findStock(phone);
        assertEquals(1000L, stock.getPhone().getId().longValue());
        assertEquals(0, stock.getStock().intValue());
        assertEquals(0, stock.getReserved().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindStockPhoneNullTest() {
        Stock stock = phoneDao.findStock(null);
    }

    @Test
    public void updateStockTest() {
        Phone phone = new Phone();
        phone.setId(1001L);
        phoneDao.updateStock(phone.getId(), 5L);
        assertEquals(5L, phoneDao.findStock(phone).getStock().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateStockPhoneIdNullTest() {
        Phone phone = new Phone();
        phoneDao.updateStock(phone.getId(), 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateStockQuantityNullTest() {
        Phone phone = new Phone();
        phone.setId(1001L);
        phoneDao.updateStock(phone.getId(), null);
    }

    private Phone createPhone() {
        Phone phone = new Phone();
        String brand = "ARCHOS";
        String model = "ARCHOS 102";
        phone.setBrand(brand);
        phone.setModel(model);
        return phone;
    }

    private void setColorsToPhone(Phone phone, Long colorIdFirst, Long colorIdSecond) {
        Set<Color> colors = new HashSet<>(Arrays.asList(
                new Color(colorIdFirst, "White"),
                new Color(colorIdSecond, "Yellow")));
        phone.setColors(colors);
    }
}
