package com.example.fitness_tracking.controller;

import com.example.fitness_tracking.entity.User;
import com.example.fitness_tracking.repository.UserRepository;
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
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API для управління користувачами")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FitnessEventProducer fitnessEventProducer;

    @GetMapping
    @Operation(summary = "Отримати всіх користувачів")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/count")
    @Operation(summary = "Отримати кількість користувачів")
    public ResponseEntity<Long> getUsersCount() {
        long count = userRepository.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати користувача по ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Отримати користувача по email")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fitness-level/{level}")
    @Operation(summary = "Отримати користувачів по рівню фітнесу")
    public List<User> getUsersByFitnessLevel(@PathVariable User.FitnessLevel level) {
        return userRepository.findByFitnessLevel(level);
    }

    @PostMapping
    @Operation(summary = "Створити нового користувача")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("Error", "Email already exists")
                    .build();
        }
        User savedUser = userRepository.save(user);
        fitnessEventProducer.sendUserRegisteredEvent(savedUser.getUserId(), savedUser.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити користувача")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getEmail().equals(userDetails.getEmail()) &&
                            userRepository.existsByEmail(userDetails.getEmail())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<User>build();
                    }
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setAge(userDetails.getAge());
                    user.setWeight(userDetails.getWeight());
                    user.setHeight(userDetails.getHeight());
                    user.setFitnessLevel(userDetails.getFitnessLevel());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/fitness-level")
    @Operation(summary = "Оновити рівень фітнесу")
    public ResponseEntity<User> updateFitnessLevel(@PathVariable Long id, @RequestParam User.FitnessLevel fitnessLevel) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFitnessLevel(fitnessLevel);
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити користувача")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
}