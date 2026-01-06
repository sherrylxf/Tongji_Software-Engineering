// 2354093 李雪菲

package com.garfield.model;

import java.time.LocalDateTime;

/*
 * SimpleTask: a basic to-do item.
 */
public class SimpleTask extends Task {
    // a basic to-do item
    private boolean completed;

    public SimpleTask() {
        super();
        this.completed = false;
    }

    public SimpleTask(int id, String title, LocalDateTime createdAt, Status status, Priority priority) {
        super(id, title, createdAt, status, priority);
        this.completed = false;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String describe() {
        return "";
    }
}

