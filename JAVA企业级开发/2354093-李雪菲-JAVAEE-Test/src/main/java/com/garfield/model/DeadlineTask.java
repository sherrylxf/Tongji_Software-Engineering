// 2354093 李雪菲

package com.garfield.model;

import java.time.LocalDateTime;

/*
 * DeadlineTask: a task with a deadline (using java.time.LocalDateTime), which supports
the display of the time remaining or "Overdue" if passed.
 */
public class DeadlineTask extends Task{
    private LocalDateTime deadline;
    private LocalDateTime now;
    private long timeRemaining;
    private boolean overdue;

    public DeadlineTask() {
        super();
        this.now = LocalDateTime.now();
        this.timeRemaining = 0;
        this.overdue = false;
    }

    public DeadlineTask(int id, String title, LocalDateTime createdAt, Status status, Priority priority) {
        super(id, title, createdAt, status, priority);
        this.deadline = null;
        this.now = LocalDateTime.now();
        this.timeRemaining = 0;
        this.overdue = false;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    @Override
    public String describe() {
        return "";
    }
}
