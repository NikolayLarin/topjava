package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static final int MEAL_ID = START_SEQ + 2;

    public static final Meal MEAL_1;
    public static final List<Meal> ALL;
    public static final List<Meal> ALL_IN_2019_10_20;

    private static final Meal MEAL_2;
    private static final Meal MEAL_3;
    private static final Meal MEAL_4;
    private static final Meal MEAL_5;
    private static final Meal MEAL_6;

    static {
        MEAL_1 = new Meal(MEAL_ID, LocalDateTime.parse("2019-10-20T10:00"), "завтрак", 500);
        MEAL_2 = new Meal(MEAL_ID + 1, LocalDateTime.parse("2019-10-20T13:00"), "обед", 1000);
        MEAL_3 = new Meal(MEAL_ID + 2, LocalDateTime.parse("2019-10-20T19:00"), "ужин", 500);
        MEAL_4 = new Meal(MEAL_ID + 3, LocalDateTime.parse("2019-10-21T10:00"), "завтрак", 500);
        MEAL_5 = new Meal(MEAL_ID + 4, LocalDateTime.parse("2019-10-21T13:00"), "обед", 1000);
        MEAL_6 = new Meal(MEAL_ID + 5, LocalDateTime.parse("2019-10-21T19:00"), "ужин", 510);

        ALL = Arrays.asList(MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5, MEAL_6);
        sortDescOrder(ALL);

        ALL_IN_2019_10_20 = Arrays.asList(MEAL_1, MEAL_2, MEAL_3);
        sortDescOrder(ALL_IN_2019_10_20);
    }

    private static void sortDescOrder(List<Meal> list) {
        list.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }
}
