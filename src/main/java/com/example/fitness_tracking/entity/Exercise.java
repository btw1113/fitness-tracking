package com.example.fitness_tracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseId;

    @NotBlank(message = "Exercise name is required")
    @Size(min = 2, max = 100, message = "Exercise name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExerciseType type;

    @Size(max = 200, message = "Muscle groups cannot exceed 200 characters")
    @Column(name = "muscle_groups")
    private String muscleGroups;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @DecimalMin(value = "1.0", message = "Calories per minute must be at least 1.0")
    @Column(name = "calories_per_minute")
    private Double caloriesPerMinute;

    @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Workout> workouts = new ArrayList<>();

    public Exercise() {}

    public Exercise(String name, ExerciseType type, String muscleGroups) {
        this.name = name;
        this.type = type;
        this.muscleGroups = muscleGroups;
        this.difficulty = Difficulty.MEDIUM;
    }

    // Getters and Setters
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ExerciseType getType() { return type; }
    public void setType(ExerciseType type) { this.type = type; }
    public String getMuscleGroups() { return muscleGroups; }
    public void setMuscleGroups(String muscleGroups) { this.muscleGroups = muscleGroups; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public Double getCaloriesPerMinute() { return caloriesPerMinute; }
    public void setCaloriesPerMinute(Double caloriesPerMinute) { this.caloriesPerMinute = caloriesPerMinute; }
    public List<Workout> getWorkouts() { return workouts; }
    public void setWorkouts(List<Workout> workouts) { this.workouts = workouts; }

    public enum ExerciseType {
        STRENGTH,
        CARDIO,
        FLEXIBILITY,
        BALANCE,
        ENDURANCE
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        EXTREME
    }
}