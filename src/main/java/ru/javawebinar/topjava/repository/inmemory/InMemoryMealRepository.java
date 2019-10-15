package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    // K - meal id, V - user id this meal belongs to
    private final Map<Integer, Integer> reference = new ConcurrentHashMap<>();

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal == null) return null;
        final int id;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            id = meal.getId();
            repository.put(id, meal);
            reference.put(id, userId);
            return meal;
        }
        // null if not found or meal does not belong to this user, when updated
        id = meal.getId();
        final Integer refId = getRefId(id);
        return (refId != null && refId == userId)
                ? repository.computeIfPresent(id, (mealId, oldMeal) -> meal)
                : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        // false if not found or meal does not belong to this user
        final Integer refId = getRefId(id);
        return refId != null && refId == userId
                && repository.remove(id) != null
                && reference.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        // null if not found
        final Integer refId = getRefId(id);
        return (refId != null && refId == userId) ? repository.get(id) : null;
    }

    @Override
    public List<Meal> getAll() {
        log.info("getAll");
        return repository.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByUser(int userId) {
        log.info("getAllByUser with userId = {}", userId);
        return reference.entrySet()
                .stream()
                .filter(entry -> (entry.getValue() == userId))
                .map((Function<Map.Entry<Integer, Integer>, Object>) entry ->
                        repository.get(entry.getKey()))
                .map(object -> (Meal) object)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByUserByDates(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllByUserByDates with userId = {}", userId);
        return reference.entrySet()
                .stream()
                .filter(entry -> (entry.getValue() == userId))
                .map((Function<Map.Entry<Integer, Integer>, Object>) entry ->
                        repository.get(entry.getKey()))
                .map(object -> (Meal) object)
                .filter(meal ->
                        DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Integer getRefId(int id) {
        return reference.get(id);
    }
}

