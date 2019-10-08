package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final int id;

    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess, int id) {
        Objects.requireNonNull(dateTime, "dateTime can't be null");
        Objects.requireNonNull(description, "description can't be null");
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.id = id;
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

    public boolean isExcess() {
        return excess;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}