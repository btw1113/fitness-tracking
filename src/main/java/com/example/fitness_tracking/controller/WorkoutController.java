package com.example.fitness_tracking.controller;

import com.example.fitness_tracking.entity.Workout;
import com.example.fitness_tracking.entity.Exercise;
import com.example.fitness_tracking.repository.WorkoutRepository;
import com.example.fitness_tracking.service.FitnessEventProducer;
import com.example.fitness_tracking.repository.UserRepository;
import com.example.fitness_tracking.repository.ExerciseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Workouts", description = "API для управління тренуваннями")
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private FitnessEventProducer fitnessEventProducer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping
    @Operation(summary = "Отримати всі тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    @GetMapping("/count")
    @Operation(summary = "Отримати кількість тренувань")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Long> getWorkoutsCount() {
        long count = workoutRepository.count();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати тренування по ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable Long id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Отримати тренування користувача")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Workout> getWorkoutsByUser(@PathVariable Long userId) {
        return workoutRepository.findByUserUserId(userId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Отримати тренування по статусу")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Workout> getWorkoutsByStatus(@PathVariable Workout.WorkoutStatus status) {
        return workoutRepository.findByStatus(status);
    }

    @GetMapping("/duration/{maxDuration}")
    @Operation(summary = "Отримати тренування за тривалістю")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Workout> getWorkoutsByMaxDuration(@PathVariable Integer maxDuration) {
        return workoutRepository.findByDurationLessThanEqual(maxDuration);
    }

    @PostMapping
    @Operation(summary = "Створити нове тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Workout> createWorkout(@Valid @RequestBody Workout workout, @RequestParam Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    workout.setUser(user);
                    if (workout.getStatus() == null) {
                        workout.setStatus(Workout.WorkoutStatus.PLANNED);
                    }
                    Workout savedWorkout = workoutRepository.save(workout);
                    fitnessEventProducer.sendWorkoutCreatedEvent(
                    user.getUserId(), 
                    user.getName(), 
                    savedWorkout.getName(),
                    savedWorkout.getWorkoutId()
);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedWorkout);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Повністю оновити тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Workout> updateWorkout(@PathVariable Long id, @Valid @RequestBody Workout workoutDetails) {
        return workoutRepository.findById(id)
                .map(workout -> {
                    workout.setName(workoutDetails.getName());
                    workout.setDateTime(workoutDetails.getDateTime());
                    workout.setDuration(workoutDetails.getDuration());
                    workout.setStatus(workoutDetails.getStatus());
                    workout.setNotes(workoutDetails.getNotes());
                    return ResponseEntity.ok(workoutRepository.save(workout));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
@Operation(summary = "Оновити статус тренування")
@PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
public ResponseEntity<Workout> updateWorkoutStatus(@PathVariable Long id, @RequestParam Workout.WorkoutStatus status) {
    return workoutRepository.findById(id)
            .map(workout -> {
                Workout.WorkoutStatus oldStatus = workout.getStatus();
                workout.setStatus(status);
                Workout updatedWorkout = workoutRepository.save(workout);
                
                if (status == Workout.WorkoutStatus.COMPLETED && oldStatus != Workout.WorkoutStatus.COMPLETED) {
                    fitnessEventProducer.sendWorkoutCompletedEvent(
                        workout.getUser().getUserId(),
                        workout.getUser().getName(),
                        workout.getName(),
                        workout.getDuration()
                    );
                }
                
                return ResponseEntity.ok(updatedWorkout);
            })
            .orElse(ResponseEntity.notFound().build());
}

    @PostMapping("/{workoutId}/exercises/{exerciseId}")
    @Operation(summary = "Додати вправу до тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Workout> addExerciseToWorkout(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
        Optional<Workout> workoutOpt = workoutRepository.findById(workoutId);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (workoutOpt.isPresent() && exerciseOpt.isPresent()) {
            Workout workout = workoutOpt.get();
            Exercise exercise = exerciseOpt.get();

            if (!workout.getExercises().contains(exercise)) {
                workout.getExercises().add(exercise);
                Workout savedWorkout = workoutRepository.save(workout);
                return ResponseEntity.ok(savedWorkout);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}")
    @Operation(summary = "Видалити вправу з тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Workout> removeExerciseFromWorkout(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
        Optional<Workout> workoutOpt = workoutRepository.findById(workoutId);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (workoutOpt.isPresent() && exerciseOpt.isPresent()) {
            Workout workout = workoutOpt.get();
            Exercise exercise = exerciseOpt.get();

            if (workout.getExercises().contains(exercise)) {
                workout.getExercises().remove(exercise);
                Workout savedWorkout = workoutRepository.save(workout);
                return ResponseEntity.ok(savedWorkout);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити тренування")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        return workoutRepository.findById(id)
                .map(workout -> {
                    workoutRepository.delete(workout);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}