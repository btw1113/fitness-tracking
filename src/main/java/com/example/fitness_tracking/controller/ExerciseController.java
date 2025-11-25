package com.example.fitness_tracking.controller;

import com.example.fitness_tracking.entity.Exercise;
import com.example.fitness_tracking.repository.ExerciseRepository;
import com.example.fitness_tracking.service.FitnessEventProducer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
@Tag(name = "Exercises", description = "API для управління вправами")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private FitnessEventProducer fitnessEventProducer;

    @GetMapping
    @Operation(summary = "Отримати всі вправи")
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @GetMapping("/count")
    @Operation(summary = "Отримати кількість вправ")
    public ResponseEntity<Long> getExercisesCount() {
        long count = exerciseRepository.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати вправу по ID")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Отримати вправи по типу")
    public List<Exercise> getExercisesByType(@PathVariable Exercise.ExerciseType type) {
        return exerciseRepository.findByType(type);
    }

    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "Отримати вправи по складності")
    public List<Exercise> getExercisesByDifficulty(@PathVariable Exercise.Difficulty difficulty) {
        return exerciseRepository.findByDifficulty(difficulty);
    }

    @GetMapping("/calories/{minCalories}")
    @Operation(summary = "Отримати вправи з мінімальною кількістю калорій")
    public List<Exercise> getExercisesByMinCalories(@PathVariable Double minCalories) {
        return exerciseRepository.findByCaloriesPerMinuteGreaterThanEqual(minCalories);
    }

    @PostMapping
    @Operation(summary = "Створити нову вправу")
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) {
        Exercise savedExercise = exerciseRepository.save(exercise);
        fitnessEventProducer.sendExerciseAddedEvent(

        savedExercise.getName(), 
        savedExercise.getType().toString()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExercise);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити вправу")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Long id, @Valid @RequestBody Exercise exerciseDetails) {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setName(exerciseDetails.getName());
                    exercise.setType(exerciseDetails.getType());
                    exercise.setMuscleGroups(exerciseDetails.getMuscleGroups());
                    exercise.setDifficulty(exerciseDetails.getDifficulty());
                    exercise.setCaloriesPerMinute(exerciseDetails.getCaloriesPerMinute());
                    return ResponseEntity.ok(exerciseRepository.save(exercise));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/calories")
    @Operation(summary = "Оновити калорії вправи")
    public ResponseEntity<Exercise> updateExerciseCalories(@PathVariable Long id, @RequestParam Double calories) {
        if (calories <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setCaloriesPerMinute(calories);
                    return ResponseEntity.ok(exerciseRepository.save(exercise));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити вправу")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exerciseRepository.delete(exercise);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}