package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.getEndExclusive;
import static ru.javawebinar.topjava.util.DateTimeUtil.getStartInclusive;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE_TIME_DESC = Sort.by(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(crudUserRepository.getOne(userId));
        if (meal.isNew()) {
            return crudMealRepository.save(meal);
        } else {
            Meal found = crudMealRepository.getOne(meal.getId());
            return found == null ? null : found.getUser().getId() == userId ?
                    crudMealRepository.save(meal) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
//        return crudMealRepository.delete(id, userId) == 1;
        return crudMealRepository.deleteByIdAndUserId(id, userId) == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudMealRepository.findById(id).orElse(null);
        return getMeal(meal, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        Meal meal = crudMealRepository.getWithUser(id, userId).orElse(null);
        return getMeal(meal, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserId(userId, SORT_DATE_TIME_DESC);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return crudMealRepository.findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeIsLessThan(
                userId, getStartInclusive(startDate), getEndExclusive(endDate), SORT_DATE_TIME_DESC);
    }

    private Meal getMeal(Meal meal, int userId) {
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }
}