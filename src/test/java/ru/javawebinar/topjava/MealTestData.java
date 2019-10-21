package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static final int MEAL_ID = START_SEQ + 2;

    public static final Meal USER_MEAL_1 = new Meal(MEAL_ID, parse("2019-10-20T10:00"), "завтрак", 500);
    private static final Meal USER_MEAL_2 = new Meal(MEAL_ID + 1, parse("2019-10-20T13:00"), "обед", 1000);
    private static final Meal USER_MEAL_3 = new Meal(MEAL_ID + 2, parse("2019-10-20T19:00"), "ужин", 500);
    private static final Meal USER_MEAL_4 = new Meal(MEAL_ID + 3, parse("2019-10-21T10:00"), "завтрак", 500);
    private static final Meal USER_MEAL_5 = new Meal(MEAL_ID + 4, parse("2019-10-21T13:00"), "обед", 1000);
    private static final Meal USER_MEAL_6 = new Meal(MEAL_ID + 5, parse("2019-10-21T19:00"), "ужин", 510);

    private static final Meal ADMIN_MEAL_7 = new Meal(MEAL_ID + 6, parse("2019-10-21T10:00"), "завтрак", 500);
    private static final Meal ADMIN_MEAL_8 = new Meal(MEAL_ID + 7, parse("2019-10-21T13:00"), "обед", 1000);
    private static final Meal ADMIN_MEAL_9 = new Meal(MEAL_ID + 8, parse("2019-10-21T19:00"), "ужин", 500);


    public static final List<Meal> ALL_USER_MEALS = Arrays.asList(
            USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

    public static final List<Meal> ALL_ADMIN_MEALS = Arrays.asList(ADMIN_MEAL_9, ADMIN_MEAL_8, ADMIN_MEAL_7);

    public static final List<Meal> ALL_IN_2019_10_20 = Arrays.asList(USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
