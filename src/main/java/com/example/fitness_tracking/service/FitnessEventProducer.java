package com.example.fitness_tracking.service;

import com.example.fitness_tracking.entity.FitnessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FitnessEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(FitnessEventProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${fitness.exchange.name:fitness-exchange}")
    private String exchangeName;

    @Value("${fitness.routing.key:fitness.event}")
    private String routingKey;

    public void sendFitnessEvent(FitnessEvent event) {
        try {
            logger.info("Sending fitness event to RabbitMQ: {}", event.getEventType());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
            logger.info("Successfully sent event: {} for user: {}", 
                       event.getEventType(), event.getUserName());
        } catch (Exception e) {
            logger.error("Failed to send fitness event: {}", event, e);
            throw new RuntimeException("Failed to send message to RabbitMQ", e);
        }
    }

    public void sendUserRegisteredEvent(Long userId, String userName) {
        FitnessEvent event = new FitnessEvent(
            "USER_REGISTERED", 
            userId, 
            userName, 
            "New user registered in fitness tracking system"
        );
        sendFitnessEvent(event);
    }

    public void sendWorkoutCreatedEvent(Long userId, String userName, String workoutName, Long workoutId) {
        FitnessEvent event = new FitnessEvent(
            "WORKOUT_CREATED",
            userId,
            userName,
            "User created new workout: " + workoutName,
            new WorkoutData(workoutId, workoutName)
        );
        sendFitnessEvent(event);
    }

    public void sendWorkoutCompletedEvent(Long userId, String userName, String workoutName, Integer duration) {
        FitnessEvent event = new FitnessEvent(
            "WORKOUT_COMPLETED",
            userId,
            userName,
            "User completed workout: " + workoutName + " (duration: " + duration + " min)",
            new WorkoutCompletionData(workoutName, duration)
        );
        sendFitnessEvent(event);
    }

    public void sendExerciseAddedEvent(String exerciseName, String exerciseType) {
        FitnessEvent event = new FitnessEvent(
            "EXERCISE_ADDED",
            null,
            "System",
            "New exercise added: " + exerciseName,
            new ExerciseData(exerciseName, exerciseType)
        );
        sendFitnessEvent(event);
    }

    // Допоміжні класи для даних
    private static class WorkoutData {
        public Long workoutId;
        public String workoutName;
        
        public WorkoutData(Long workoutId, String workoutName) {
            this.workoutId = workoutId;
            this.workoutName = workoutName;
        }

        @Override
        public String toString() {
            return "WorkoutData{workoutId=" + workoutId + ", workoutName='" + workoutName + "'}";
        }
    }

    private static class WorkoutCompletionData {
        public String workoutName;
        public Integer duration;
        
        public WorkoutCompletionData(String workoutName, Integer duration) {
            this.workoutName = workoutName;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "WorkoutCompletionData{workoutName='" + workoutName + "', duration=" + duration + "}";
        }
    }

    private static class ExerciseData {
        public String exerciseName;
        public String exerciseType;
        
        public ExerciseData(String exerciseName, String exerciseType) {
            this.exerciseName = exerciseName;
            this.exerciseType = exerciseType;
        }

        @Override
        public String toString() {
            return "ExerciseData{exerciseName='" + exerciseName + "', exerciseType='" + exerciseType + "'}";
        }
    }
}