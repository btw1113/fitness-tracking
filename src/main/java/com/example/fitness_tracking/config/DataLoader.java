package com.example.fitness_tracking.config;

import com.example.fitness_tracking.entity.*;
import com.example.fitness_tracking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Override
    public void run(String... args) {

        // Додаємо users з новими іменами
        User maksym = new User("Максим Іваненко", "maksym@fit.com", 28, 75.5, 180);
        maksym.setFitnessLevel(User.FitnessLevel.ADVANCED);
        maksym.setRegistrationDate(LocalDate.now().minusDays(90));
        userRepository.save(maksym);

        User sofia = new User("Софія Мельник", "sofia@fit.com", 25, 60.2, 165);
        sofia.setFitnessLevel(User.FitnessLevel.INTERMEDIATE);
        sofia.setRegistrationDate(LocalDate.now().minusDays(60));
        userRepository.save(sofia);

        User danylo = new User("Данило Коваль", "danylo@fit.com", 32, 85.0, 182);
        danylo.setFitnessLevel(User.FitnessLevel.BEGINNER);
        danylo.setRegistrationDate(LocalDate.now().minusDays(30));
        userRepository.save(danylo);

        User anna = new User("Анна Шевченко", "anna@fit.com", 22, 58.0, 170);
        anna.setFitnessLevel(User.FitnessLevel.INTERMEDIATE);
        anna.setRegistrationDate(LocalDate.now().minusDays(15));
        userRepository.save(anna);

        System.out.println("Створено 4 users");

        // Додаємо exercises
        Exercise pushUps = new Exercise("Віджимання", Exercise.ExerciseType.STRENGTH, "Груди, плечі, трицепс");
        pushUps.setDifficulty(Exercise.Difficulty.MEDIUM);
        pushUps.setCaloriesPerMinute(8.0);
        exerciseRepository.save(pushUps);

        Exercise running = new Exercise("Біг", Exercise.ExerciseType.CARDIO, "Ноги, серцево-судинна система");
        running.setDifficulty(Exercise.Difficulty.EASY);
        running.setCaloriesPerMinute(12.5);
        exerciseRepository.save(running);

        Exercise squats = new Exercise("Присідання", Exercise.ExerciseType.STRENGTH, "Ноги, сідниці");
        squats.setDifficulty(Exercise.Difficulty.MEDIUM);
        squats.setCaloriesPerMinute(6.0);
        exerciseRepository.save(squats);

        Exercise yoga = new Exercise("Йога", Exercise.ExerciseType.FLEXIBILITY, "Гнучкість, баланс");
        yoga.setDifficulty(Exercise.Difficulty.EASY);
        yoga.setCaloriesPerMinute(4.0);
        exerciseRepository.save(yoga);

        Exercise deadlift = new Exercise("Станова тяга", Exercise.ExerciseType.STRENGTH, "Спина, ноги");
        deadlift.setDifficulty(Exercise.Difficulty.HARD);
        deadlift.setCaloriesPerMinute(7.0);
        exerciseRepository.save(deadlift);

        Exercise swimming = new Exercise("Плавання", Exercise.ExerciseType.CARDIO, "Всі групи м'язів");
        swimming.setDifficulty(Exercise.Difficulty.MEDIUM);
        swimming.setCaloriesPerMinute(10.0);
        exerciseRepository.save(swimming);

        System.out.println("Створено 6 exercises");

        // Додаємо workouts
        Workout morningWorkout = new Workout("Ранкова тренування", 
            LocalDateTime.of(2024, 1, 15, 7, 0), 45);
        morningWorkout.setUser(maksym);
        morningWorkout.setStatus(Workout.WorkoutStatus.COMPLETED);
        morningWorkout.setNotes("Чудове тренування, добре самопочуття");
        workoutRepository.save(morningWorkout);
        morningWorkout.getExercises().add(pushUps);
        morningWorkout.getExercises().add(squats);
        workoutRepository.save(morningWorkout);

        Workout eveningRun = new Workout("Вечірній біг", 
            LocalDateTime.of(2024, 1, 16, 18, 30), 30);
        eveningRun.setUser(sofia);
        eveningRun.setStatus(Workout.WorkoutStatus.COMPLETED);
        eveningRun.setNotes("Біг у парку, приємна погода");
        workoutRepository.save(eveningRun);
        eveningRun.getExercises().add(running);
        workoutRepository.save(eveningRun);

        Workout weekendWorkout = new Workout("Вихідне тренування", 
            LocalDateTime.of(2024, 1, 20, 10, 0), 60);
        weekendWorkout.setUser(danylo);
        weekendWorkout.setStatus(Workout.WorkoutStatus.IN_PROGRESS);
        weekendWorkout.setNotes("Перше тренування з тренером");
        workoutRepository.save(weekendWorkout);
        weekendWorkout.getExercises().add(deadlift);
        weekendWorkout.getExercises().add(squats);
        workoutRepository.save(weekendWorkout);

        Workout yogaSession = new Workout("Ранкова йога", 
            LocalDateTime.of(2024, 1, 17, 6, 30), 40);
        yogaSession.setUser(anna);
        yogaSession.setStatus(Workout.WorkoutStatus.COMPLETED);
        yogaSession.setNotes("Заняття йогою для розслаблення");
        workoutRepository.save(yogaSession);
        yogaSession.getExercises().add(yoga);
        workoutRepository.save(yogaSession);

        Workout poolTraining = new Workout("Тренування в басейні", 
            LocalDateTime.of(2024, 1, 18, 16, 0), 50);
        poolTraining.setUser(maksym);
        poolTraining.setStatus(Workout.WorkoutStatus.PLANNED);
        poolTraining.setNotes("Плавання для витривалості");
        workoutRepository.save(poolTraining);
        poolTraining.getExercises().add(swimming);
        workoutRepository.save(poolTraining);

        System.out.println("Створено 5 workouts");

        System.out.println("\nТестові дані завантажено!");
        System.out.println("=".repeat(60));
        System.out.println("Статистика бази даних:");
        System.out.println("Користувачів: " + userRepository.count());
        System.out.println("Вправ: " + exerciseRepository.count());
        System.out.println("Тренувань: " + workoutRepository.count());
    }
}