package com.example.fitness_tracking.service;

import com.example.fitness_tracking.entity.FitnessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FitnessEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(FitnessEventConsumer.class);

    @RabbitListener(queues = "${fitness.queue.name:fitness-events}")
    public void receiveMessage(FitnessEvent event) {
        try {
            logger.info("=== FITNESS EVENT RECEIVED ===");
            logger.info("Type: {}", event.getEventType());
            logger.info("User: {} (ID: {})", event.getUserName(), event.getUserId());
            logger.info("Description: {}", event.getDescription());
            logger.info("Timestamp: {}", event.getTimestamp());
            logger.info("Data: {}", event.getData());
            logger.info("=== END OF EVENT ===");

            processEvent(event);

        } catch (Exception e) {
            logger.error("Error processing fitness event: {}", event, e);
            throw e;
        }
    }

    private void processEvent(FitnessEvent event) {
        switch (event.getEventType()) {
            case "USER_REGISTERED":
                logger.info("Processing new user registration: {}", event.getUserName());
                break;
            case "WORKOUT_CREATED":
                logger.info("Processing new workout creation by user: {}", event.getUserName());
                break;
            case "WORKOUT_COMPLETED":
                logger.info("Processing workout completion: {}", event.getDescription());
                break;
            case "EXERCISE_ADDED":
                logger.info("Processing new exercise addition");
                break;
            default:
                logger.info("Processing unknown event type: {}", event.getEventType());
        }
    }

    @RabbitListener(queues = "${fitness.dlq.name:fitness-events-dlq}")
    public void processFailedMessage(FitnessEvent event) {
        logger.error("=== PROCESSING FAILED MESSAGE FROM DLQ ===");
        logger.error("Failed event: {}", event);
        logger.error("This message will NOT be retried");
        logger.error("=== END OF FAILED MESSAGE ===");
    }
}