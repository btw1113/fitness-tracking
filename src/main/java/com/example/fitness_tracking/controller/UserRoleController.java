package com.example.fitness_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-role")
@Tag(name = "4. User Functions", description = "API для звичайних користувачів")
@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "bearerAuth")
public class UserRoleController {

    @GetMapping("/profile")
    @Operation(summary = "Отримати свій профіль")
    public String getMyProfile() {
        return "My user profile";
    }

    @PutMapping("/profile")
    @Operation(summary = "Оновити свій профіль")
    public String updateMyProfile() {
        return "Profile updated successfully";
    }

    @GetMapping("/my-workouts")
    @Operation(summary = "Отримати мої тренування")
    public String getMyWorkouts() {
        return "List of my workouts";
    }

    @PostMapping("/workouts")
    @Operation(summary = "Створити своє тренування")
    public String createMyWorkout() {
        return "My workout created";
    }

    @GetMapping("/progress")
    @Operation(summary = "Отримати мій прогрес")
    public String getMyProgress() {
        return "My fitness progress";
    }

    @GetMapping("/exercises/available")
    @Operation(summary = "Отримати доступні вправи")
    public String getAvailableExercises() {
        return "Available exercises for users";
    }
}