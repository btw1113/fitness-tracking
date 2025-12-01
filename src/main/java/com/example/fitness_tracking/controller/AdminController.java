package com.example.fitness_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "2. Admin Only", description = "API доступне тільки адміністраторам")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @GetMapping("/stats")
    @Operation(summary = "Отримати статистику системи")
    public String getSystemStats() {
        return "System statistics: 100 users, 500 workouts, 50 exercises";
    }

    @GetMapping("/users")
    @Operation(summary = "Отримати всіх користувачів (адмінська версія)")
    public String getAllUsersAdmin() {
        return "All users list (admin version)";
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Видалити користувача")
    public String deleteUser(@PathVariable Long userId) {
        return "User " + userId + " deleted by admin";
    }

    @PostMapping("/users/reset-password/{userId}")
    @Operation(summary = "Скинути пароль користувача")
    public String resetUserPassword(@PathVariable Long userId) {
        return "Password reset for user " + userId;
    }
}