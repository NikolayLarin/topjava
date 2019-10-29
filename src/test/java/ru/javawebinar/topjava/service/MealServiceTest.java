package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL1;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL2;
import static ru.javawebinar.topjava.MealTestData.MEAL3;
import static ru.javawebinar.topjava.MealTestData.MEAL4;
import static ru.javawebinar.topjava.MealTestData.MEAL5;
import static ru.javawebinar.topjava.MealTestData.MEAL6;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getCreated;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static String watcherLog = "tests execution results:";
    private static long totalTime;
    private static long totalPassed;
    private static long totalFailed;

    @Autowired
    private MealService service;

    @AfterClass
    public static void afterClass() {
        log.info("{}\n" +
                "\nTotal time for all tests: {} ms" +
                "\nTotal tests passed: {}" +
                "\nTotal tests failed: {}", watcherLog, totalTime, totalPassed, totalFailed);
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TestWatcher watcher = new TestWatcher() {
        private long startTime;

        @Override
        public void starting(Description d) {
            startTime = System.currentTimeMillis();
            log.info("Test {}() started!", d.getMethodName());

        }

        @Override
        public void succeeded(Description d) {
            log.info("Test {}(): passed!", d.getMethodName());
            watcherLog += "\n\nTest " + d.getMethodName() + "() in " + d.getClassName() + ":\n - passed";
            totalPassed += 1;
        }

        @Override
        public void failed(Throwable e, Description d) {
            log.error("Test {}(): failed!", d.getMethodName());
            watcherLog += "\nTest " + d.getMethodName() + "() in " + d.getClassName() + ":\n - failed with " + e;
            totalFailed += 1;
        }

        @Override
        public void finished(Description description) {
            long spentTime = System.currentTimeMillis() - startTime;
            log.info("Test {}(): spent time = {} ms", description.getMethodName(), spentTime);
            watcherLog += "\n - spent time = " + spentTime + " ms";
            totalTime += spentTime;
        }
    };

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + 1);
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void createDuplicatedDateTime() {
        thrown.expect(DataIntegrityViolationException.class);
        thrown.expectMessage("could not execute statement");
        service.create(
                new Meal(null, MEAL1.getDateTime(), "new " + MEAL1.getDescription(), 50),
                USER_ID);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + 1);
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}