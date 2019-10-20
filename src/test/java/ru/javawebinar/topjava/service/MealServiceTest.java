package ru.javawebinar.topjava.service;

import org.junit.Assert;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.ALL;
import static ru.javawebinar.topjava.MealTestData.ALL_IN_2019_10_20;
import static ru.javawebinar.topjava.MealTestData.MEAL_1;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
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
        Meal newMeal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "new meal", 500);
        Meal created = service.create(newMeal, authUserId());
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicatedDateTime() {
        Meal duplicated = new Meal(null, MEAL_1.getDateTime(), "new " + MEAL_1.getDescription(), 50);
        service.create(duplicated, authUserId());

    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("new " + MEAL_1.getDescription());
        updated.setCalories(MEAL_1.getCalories() + 1);
        service.update(updated, authUserId());
        assertMatch(service.get(MEAL_1.getId(), authUserId()), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignMeal() {
        service.update(MEAL_1, authUserId() + 1);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        int size = service.getAll(authUserId()).size();
        service.delete(MEAL_1.getId(), authUserId());
        Assert.assertEquals(size - 1, service.getAll(authUserId()).size());
        service.get(MEAL_1.getId(), authUserId());
    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        service.delete(MEAL_1.getId(), authUserId() + 1);
    }

    @Test
    public void get() {
        Meal expected = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "завтрак", 500);
        Meal actual = service.create(expected, authUserId());
        expected.setId(actual.getId());
        assertMatch(service.get(actual.getId(), authUserId()), expected);
    }

    @Test(expected = NotFoundException.class)
    public void getForeignMeal() {
        service.get(MEAL_1.getId(), authUserId() + 1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(authUserId());
        for (int i = 0; i < all.size(); i++) {
            assertMatch(all.get(i), ALL.get(i));
        }
    }

    @Test
    public void getBetweenDates() {
        LocalDate ld = LocalDate.parse("2019-10-20");
        List<Meal> all = service.getBetweenDates(ld, ld, authUserId());
        for (int i = 0; i < all.size(); i++) {
            assertMatch(all.get(i), ALL_IN_2019_10_20.get(i));
        }
    }

    private void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}