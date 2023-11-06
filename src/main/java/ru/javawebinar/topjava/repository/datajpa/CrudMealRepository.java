package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Meal m WHERE m.id=:id AND m.user_id=:userId", nativeQuery = true)
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE meal SET description=:description, calories=:calories, date_time=:dateTime WHERE id=:id AND user_id=:user_id",
            nativeQuery = true)
    Meal save(String description, int calories, LocalDateTime dateTime, int id, int userId);

    @Query(value = "SELECT * FROM Meal m WHERE user_id=:userId AND m.date_time>=:startDateTime AND m.date_time<:endDateTime ORDER BY m.date_time DESC",
            nativeQuery = true)
    List<Meal> findAllBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime,
                                      @Param("endDateTime") LocalDateTime endDateTime,
                                      @Param("userId") int userId);
}
