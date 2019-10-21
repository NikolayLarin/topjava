package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.parse;
import static ru.javawebinar.topjava.MealTestData.ALL_ADMIN_MEALS;
import static ru.javawebinar.topjava.MealTestData.ALL_IN_2019_10_20;
import static ru.javawebinar.topjava.MealTestData.ALL_USER_MEALS;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_1;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-jdbc-repository.xml",
        "classpath:spring/spring-web-service.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;


    @Test
    public void create() {
        Meal created = service.create(
                new Meal(LocalDateTime.parse("0000-01-01T00:01"), "New Age meal", 50), USER_ID);
        List<Meal> expected = new ArrayList<>(ALL_USER_MEALS);
        expected.add(created);
        assertMatch(service.getAll(USER_ID), expected);
        assertMatch(service.getAll(ADMIN_ID), ALL_ADMIN_MEALS);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicatedDateTime() {
        service.create(
                new Meal(null, USER_MEAL_1.getDateTime(), "new " + USER_MEAL_1.getDescription(), 50),
                USER_ID);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("new description");
        updated.setCalories(501);
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_1.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignMeal() {
        service.update(USER_MEAL_1, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        List<Meal> expected = new ArrayList<>(ALL_USER_MEALS);
        expected.remove(USER_MEAL_1);
        assertMatch(service.getAll(USER_ID), expected);
        assertMatch(service.getAll(ADMIN_ID), ALL_ADMIN_MEALS);
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        service.delete(USER_MEAL_1.getId(), ADMIN_ID);
    }

    @Test
    public void get() {
        assertMatch(service.get(USER_MEAL_1.getId(), USER_ID), USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getForeignMeal() {
        service.get(USER_MEAL_1.getId(), ADMIN_ID);
    }


    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), ALL_USER_MEALS);
        assertMatch(service.getAll(ADMIN_ID), ALL_ADMIN_MEALS);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(parse("2019-10-20"), parse("2019-10-20"), USER_ID), ALL_IN_2019_10_20);
    }
}