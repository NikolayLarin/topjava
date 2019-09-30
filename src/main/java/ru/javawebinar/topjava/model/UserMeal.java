package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserMeal implements Comparable<UserMeal> {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public int compareTo(UserMeal o) {
        int cmp = dateTime.toLocalDate().compareTo(o.getDateTime().toLocalDate());
        return cmp != 0 ? cmp : dateTime.toLocalTime().compareTo(o.getDateTime().toLocalTime());
    }

    @Override
    public String toString() {
        return dateTime.toLocalDate().toString() +
                ", " + dateTime.toLocalDate().toString() +
                ", " + dateTime.toLocalTime().toString() +
                ", " + calories +
                " cal, " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMeal userMeal = (UserMeal) o;
        return calories == userMeal.calories &&
                Objects.equals(dateTime, userMeal.dateTime) &&
                Objects.equals(description, userMeal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description, calories);
    }
}