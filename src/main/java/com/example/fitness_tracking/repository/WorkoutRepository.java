package com.example.fitness_tracking.repository;

import com.example.fitness_tracking.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserUserId(Long userId);
    
    List<Workout> findByStatus(Workout.WorkoutStatus status);
    
    List<Workout> findByDurationLessThanEqual(Integer maxDuration);
    
    List<Workout> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT w FROM Workout w JOIN w.exercises e WHERE e.exerciseId = :exerciseId")
    List<Workout> findWorkoutsByExercise(@Param("exerciseId") Long exerciseId);
    
    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user.userId = :userId")
    long countWorkoutsByUser(@Param("userId") Long userId);
    
    List<Workout> findByDurationBetween(Integer minDuration, Integer maxDuration);
}