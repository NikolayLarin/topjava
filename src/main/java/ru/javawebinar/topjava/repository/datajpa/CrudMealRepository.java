package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
//    @Modifying
//    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
//    int delete(@Param("id") int id, @Param("userId") int userId);
    int deleteByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserId(int userId, Sort sortDateTimeDesc);

    List<Meal> findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeIsLessThan(
            int userId, LocalDateTime startDate, LocalDateTime endDate, Sort sortDateTimeDesc);
}