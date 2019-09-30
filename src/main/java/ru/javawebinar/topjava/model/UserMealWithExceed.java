package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserMealWithExceed implements Comparable<UserMealWithExceed> {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private boolean exceed;

    public UserMealWithExceed(LocalDateTime dateTime, String description, int calories, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public UserMealWithExceed(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public void setExceed(boolean exceed) {
        this.exceed = exceed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }

    @Override
    public int compareTo(UserMealWithExceed o) {
        int cmp = dateTime.toLocalDate().compareTo(o.getDateTime().toLocalDate());
        return cmp != 0 ? cmp : dateTime.toLocalTime().compareTo(o.getDateTime().toLocalTime());
    }

    @Override
    public String toString() {
        return "calorie limit per day exceeded: " + exceed +
                ", " + dateTime.toLocalDate().toString() +
                ", " + dateTime.toLocalTime().toString() +
                ", " + calories +
                " cal, " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMealWithExceed that = (UserMealWithExceed) o;
        return calories == that.calories &&
                exceed == that.exceed &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description, calories, exceed);
    }
}