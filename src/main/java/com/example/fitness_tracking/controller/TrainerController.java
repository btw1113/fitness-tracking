package com.example.fitness_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainer")
@Tag(name = "3. Trainer Functions", description = "API для тренерів та адміністраторів")
@PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class TrainerController {

    @GetMapping("/workouts")
    @Operation(summary = "Отримати всі тренування")
    public String getAllWorkouts() {
        return "All workouts list for trainers";
    }

    @GetMapping("/workouts/user/{userId}")
    @Operation(summary = "Отримати тренування конкретного користувача")
    public String getUserWorkouts(@PathVariable Long userId) {
        return "Workouts for user " + userId;
    }

    @PostMapping("/exercises")
    @Operation(summary = "Створити нову вправу")
    public String createExercise() {
        return "New exercise created by trainer";
    }

    @PostMapping("/workouts/assign/{userId}")
    @Operation(summary = "Призначити тренування користувачу")
    public String assignWorkoutToUser(@PathVariable Long userId) {
        return "Workout assigned to user " + userId;
    }

    @GetMapping("/clients")
    @Operation(summary = "Отримати список клієнтів тренера")
    public String getTrainerClients() {
        return "Trainer's clients list";
    }
}