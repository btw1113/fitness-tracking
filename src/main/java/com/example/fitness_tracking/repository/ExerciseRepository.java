package com.example.fitness_tracking.repository;

import com.example.fitness_tracking.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByType(Exercise.ExerciseType type);
    
    List<Exercise> findByDifficulty(Exercise.Difficulty difficulty);
    
    List<Exercise> findByCaloriesPerMinuteGreaterThanEqual(Double minCalories);
    
    @Query("SELECT e FROM Exercise e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Exercise> findByNameContainingIgnoreCase(@Param("name") String name);
    
    List<Exercise> findByCaloriesPerMinuteBetween(Double minCalories, Double maxCalories);
    
    @Query("SELECT e FROM Exercise e ORDER BY e.caloriesPerMinute DESC")
    List<Exercise> findTopCalorieExercises();
}