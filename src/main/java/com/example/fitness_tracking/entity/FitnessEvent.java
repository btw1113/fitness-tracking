package com.example.fitness_tracking.entity;

import java.time.LocalDateTime;

public class FitnessEvent {
    private String eventType;
    private Long userId;
    private String userName;
    private String description;
    private LocalDateTime timestamp;
    private Object data;

    public FitnessEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public FitnessEvent(String eventType, Long userId, String userName, String description) {
        this();
        this.eventType = eventType;
        this.userId = userId;
        this.userName = userName;
        this.description = description;
    }

    public FitnessEvent(String eventType, Long userId, String userName, String description, Object data) {
        this(eventType, userId, userName, description);
        this.data = data;
    }

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    @Override
    public String toString() {
        return "FitnessEvent{" +
                "eventType='" + eventType + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}