package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclusive;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    // K - userId, V:  K - mealId, V - meal with this mealId this meal belongs to user with userId
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        setAuthUserId(1);
        MealsUtil.MEALS.forEach(meal -> save(meal, authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal == null) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (repository.get(userId) == null) {
                repository.put(userId, new HashMap<Integer, Meal>() {{
                            put(meal.getId(), meal);
                        }}
                );
            } else {
                repository.get(userId).put(meal.getId(), meal);
            }
            return meal;
        }
        // null if not found or meal does not belong to this user, when updated
        return repository.get(userId).computeIfPresent(meal.getId(), new BiFunction<Integer, Meal, Meal>() {
            @Override
            public Meal apply(Integer id, Meal oldMeal) {
                return meal;
            }
        });
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        // false if not found or meal does not belong to this user
        Map<Integer, Meal> meals = getMealsMap(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        // null if not found
        Map<Integer, Meal> meals = getMealsMap(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll with userId = {}", userId);
        // empty if not found
        return getMeals(userId);
    }

    @Override
    public List<Meal> getAllByDates(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllByDates with userId = {}", userId);
        return getMeals(userId)
                .stream()
                .filter(meal -> isBetweenInclusive(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMealsMap(int userId) {
        return repository.get(userId);
    }

    private List<Meal> getMeals(int userId) {
        Map<Integer, Meal> meals = getMealsMap(userId);
        return meals != null ?
                meals.values()
                        .stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()) :
                new ArrayList<>();
    }

}