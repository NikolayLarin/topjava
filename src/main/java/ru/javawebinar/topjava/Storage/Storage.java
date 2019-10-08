package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void save(Meal meal);

    Meal get(int id);

    void update(Meal meal);

    void delete(int id);

    List<Meal> getAll();
}