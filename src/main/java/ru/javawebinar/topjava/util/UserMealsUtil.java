package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> mealWithExceedList =
                getCyclesFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> mealWithExceedListFromStreams =
                getStreamFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> mealWithExceedListOnePass =
                getCyclesFilteredWithExceededOnePass(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealWithExceedList.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getStreamFilteredWithExceeded(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getCyclesFilteredWithExceeded(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            caloriesPerDayMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceedList.add(new UserMealWithExceed(
                        userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealWithExceedList;
    }

    public static List<UserMealWithExceed> getCyclesFilteredWithExceededOnePass(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        Map<LocalDateTime, UserMeal> timeFilteredMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            caloriesPerDayMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            final LocalDateTime ldt = userMeal.getDateTime();
            if (TimeUtil.isBetween(ldt.toLocalTime(), startTime, endTime)) {
                timeFilteredMap.put(ldt, userMeal);
            }
        }

        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();
        for (LocalDateTime ldt : timeFilteredMap.keySet()) {
            final UserMeal userMeal = timeFilteredMap.get(ldt);
            mealWithExceedList.add(new UserMealWithExceed(
                    ldt, userMeal.getDescription(), userMeal.getCalories(),
                    caloriesPerDayMap.get(ldt.toLocalDate()) > caloriesPerDay));
        }
        return mealWithExceedList;
    }
}