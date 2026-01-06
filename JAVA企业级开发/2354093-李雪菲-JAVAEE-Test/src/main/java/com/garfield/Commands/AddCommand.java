// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.model.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Command(
    name = "add",
    description = "添加新任务"
)
public class AddCommand implements Runnable {
    
    @Option(names = {"--title", "-t"}, required = true, description = "任务标题")
    private String title;
    
    @Option(names = {"--priority", "-p"}, description = "任务优先级 (LOW/MEDIUM/HIGH)，默认: MEDIUM")
    private Task.Priority priority = Task.Priority.MEDIUM;
    
    @Option(names = {"--deadline", "-d"}, description = "截止时间 (yyyy-MM-dd HH:mm)")
    private String deadline;
    
    private InMemoryGarfieldTaskRepository repository;
    
    public void setRepository(InMemoryGarfieldTaskRepository repository) {
        this.repository = repository;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new TaskException("任务标题不能为空");
            }
            
            Task task;
            LocalDateTime createdAt = LocalDateTime.now();
            
            if (deadline != null && !deadline.trim().isEmpty()) {
                DeadlineTask deadlineTask = new DeadlineTask();
                deadlineTask.setTitle(title.trim());
                deadlineTask.setPriority(priority);
                deadlineTask.setStatus(Task.Status.OPEN);
                deadlineTask.setCreatedAt(createdAt);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                try {
                    LocalDateTime deadlineTime = LocalDateTime.parse(deadline.trim(), formatter);
                    deadlineTask.setDeadline(deadlineTime);
                } catch (DateTimeParseException e) {
                    System.err.println("错误: 截止时间格式不正确， yyyy-MM-dd HH:mm 格式");
                    return;
                }
                
                task = deadlineTask;
            } else {
                SimpleTask simpleTask = new SimpleTask();
                simpleTask.setTitle(title.trim());
                simpleTask.setPriority(priority);
                simpleTask.setStatus(Task.Status.OPEN);
                simpleTask.setCreatedAt(createdAt);
                
                task = simpleTask;
            }
            
            repository.save(task);
            System.out.println("任务已添加: T-" + task.getId() + " - " + task.getTitle());
            
        } catch (TaskException e) {
            System.err.println("错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("添加任务时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

