package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void clear();

    void update(Meal meal);

    void save(Meal meal);

    Meal get(int id);

    List<Meal> getAll();

    void delete(int id);

    int size();
}