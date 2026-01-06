// 2354093 李雪菲
package com.garfield.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDateTime;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)


@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleTask.class, name = "simple"),
    @JsonSubTypes.Type(value = DeadlineTask.class, name = "deadline")
})

/*
– Create an abstract superclass Task that contains: id, title, createdAt, status (enum: OPEN/DONE),
priority (enum: LOW/MEDIUM/HIGH) and any attribute that might be used.
– Define an abstract method describe() that each subclass must override to present its details.
 */
public abstract class Task {
    
    public enum Status {
        OPEN,
        DONE
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
    
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private Status status;
    private Priority priority;

    public Task() {
    }

    public Task(int id, String title, LocalDateTime createdAt, Status status, Priority priority) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.status = status;
        this.priority = priority;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public abstract String describe();
}





