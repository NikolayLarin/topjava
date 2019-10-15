package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int AuthUserId;

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static int authUserId() {
        return AuthUserId;
    }

    public static void setAuthUserId(int UserId) {
        AuthUserId = UserId;
    }
}