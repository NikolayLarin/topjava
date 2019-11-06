package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        UserTestData.assertMatch(getUser(USER_ID), USER);
        MealTestData.assertMatch(getMeals(USER_ID), MEALS);
    }

    @Test
    public void getWithEmptyMeals() {
        User newUser = service.create(new User(
                null, "New", "new@gmail.com", "newPass",
                1555, false, new Date(), Collections.singleton(Role.ROLE_USER)));
        final Integer id = newUser.getId();
        UserTestData.assertMatch(getUser(id), newUser);
        MealTestData.assertMatch(getMeals(id), Collections.emptyList());
    }

    private User getUser(Integer id) {
        final User withMeals = service.getWithMeals(id);
        return withMeals;
    }

    private List<Meal> getMeals(Integer id) {
        final List<Meal> meals = service.getWithMeals(id).getMeals();
        return meals;
    }
}