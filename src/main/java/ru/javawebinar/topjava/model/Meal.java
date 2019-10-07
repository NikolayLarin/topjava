package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Meal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private int id;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(dateTime, description, calories, -1);
    }

    public Meal(LocalDateTime dateTime, String description, int calories, int id) {
        Objects.requireNonNull(dateTime, "dateTime can't be null");
        Objects.requireNonNull(description, "description can't be null");
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}