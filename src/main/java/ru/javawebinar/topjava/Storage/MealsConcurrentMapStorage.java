package ru.javawebinar.topjava.Storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsConcurrentMapStorage implements Storage {
    private ConcurrentHashMap<Integer, Meal> map = new ConcurrentHashMap<>();

    private final AtomicInteger atomicCounter = new AtomicInteger();

    @Override
    public void clear() {
        map.clear();
        atomicCounter.set(0);
    }

    @Override
    public void update(Meal meal) {
        map.put(meal.getId(), meal);
    }

    @Override
    public void save(Meal meal) {
        meal.setId(atomicCounter.incrementAndGet());
        map.put(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        return map.get(id);

    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(int id) {
        map.remove(id);
    }

    @Override
    public int size() {
        return map.size();
    }
}