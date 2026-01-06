// 2354093 李雪菲

package com.garfield.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
– Provide a concrete implementation InMemoryGarfieldTaskRepository by utilizing Map<String,
Task>.
 */
public class InMemoryGarfieldTaskRepository implements GarfieldTaskRepository{
    private final Map<String, Task> tasks = new HashMap<>();
    private int nextId = 1001;
    
    @Override
    public void save(Task task) {
        if (task.getId() == 0) {
            task.setId(nextId++);
        }
        String taskId = "T-" + task.getId();
        tasks.put(taskId, task);
    }

    @Override
    public Task findById(String id) throws TaskException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskException("任务未找到: " + id);
        }
        return task;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteById(String id) throws TaskException {
        if (!tasks.containsKey(id)) {
            throw new TaskException("任务未找到: " + id);
        }
        tasks.remove(id);
    }

    @Override
    public List<Task> findByStatus(Task.Status status) {
        return tasks.values().stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    public List<Task> findByPriority(Task.Priority priority) {
        return tasks.values().stream()
            .filter(task -> task.getPriority() == priority)
            .collect(Collectors.toList());
    }

    @Override
    public void update(Task task) throws TaskException {
        String taskId = "T-" + task.getId();
        if (!tasks.containsKey(taskId)) {
            throw new TaskException("任务未找到: " + taskId);
        }
        tasks.put(taskId, task);
    }
}
