package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> mealWithExceedList =
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        List<UserMealWithExceed> mealWithExceedListFromStreams =
                getStreamFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getStreamFilteredWithExceeded(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceedList = mealList.stream()
                .map(userMeal ->
                        new UserMealWithExceed(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories()))
                .collect(Collectors.toList());

        Map<LocalDate, Integer> mealPerDayMap = new HashMap<>();
        userMealWithExceedList.forEach(userMealWithExceed ->
                mealPerDayMap.merge(userMealWithExceed.getDateTime().toLocalDate(),
                        userMealWithExceed.getCalories(), Integer::sum));

        userMealWithExceedList.stream().filter(userMealWithExceed ->
                mealPerDayMap.get(userMealWithExceed.getDateTime().toLocalDate()) > caloriesPerDay)
                .forEach(userMealWithExceed -> userMealWithExceed.setExceed(true));

        return userMealWithExceedList.stream()
                .filter(userMealWithExceed -> mealPerDayMap.get(
                        userMealWithExceed.getDateTime().toLocalDate()) > caloriesPerDay
                        && TimeUtil.isBetween(userMealWithExceed.getDateTime().toLocalTime(), startTime, endTime))
                .sorted().collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceedList0 = new ArrayList<>();
        for (UserMeal meal : mealList) {
            UserMealWithExceed userMealWithExceed =
                    new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories());
            userMealWithExceedList0.add(userMealWithExceed);
        }

        Map<LocalDate, Integer> mealPerDayMap = new HashMap<>();
        for (UserMealWithExceed userMealWithExceed : userMealWithExceedList0) {
            LocalDate ld = userMealWithExceed.getDateTime().toLocalDate();
            mealPerDayMap.merge(ld, userMealWithExceed.getCalories(), Integer::sum);
        }

        for (UserMealWithExceed userMealWithExceed : userMealWithExceedList0) {
            LocalDate ld = userMealWithExceed.getDateTime().toLocalDate();
            if (mealPerDayMap.get(ld) > caloriesPerDay) {
                userMealWithExceed.setExceed(true);
            }
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for (UserMealWithExceed userMealWithExceed : userMealWithExceedList0) {
            LocalDate ld = userMealWithExceed.getDateTime().toLocalDate();
            LocalTime lt = userMealWithExceed.getDateTime().toLocalTime();
            if (mealPerDayMap.get(ld) > caloriesPerDay && TimeUtil.isBetween(lt, startTime, endTime)) {
                userMealWithExceedList.add(userMealWithExceed);
            }
        }

        Collections.sort(userMealWithExceedList);
        return userMealWithExceedList;
    }
}