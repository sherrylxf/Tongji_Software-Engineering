// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.model.DeadlineTask;
import com.garfield.model.InMemoryGarfieldTaskRepository;
import com.garfield.model.Task;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Command(
    name = "list",
    description = "显示任务列表"
)
public class ListCommand implements Runnable {
    
    @Option(names = {"--status", "-s"}, description = "按状态筛选 (OPEN/DONE)")
    private Task.Status status;
    
    @Option(names = {"--priority", "-p"}, description = "按优先级筛选 (LOW/MEDIUM/HIGH)")
    private Task.Priority priority;
    
    private InMemoryGarfieldTaskRepository repository;
    
    public void setRepository(InMemoryGarfieldTaskRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void run() {
        try {
            List<Task> tasks;

            if (status != null) {
                tasks = repository.findByStatus(status);
            } else if (priority != null) {
                tasks = repository.findByPriority(priority);
            } else {
                tasks = repository.findAll();
            }
            
            if (tasks.isEmpty()) {
                System.out.println("没有找到任务。");
                return;
            }
            
            System.out.println("\n 任务列表 :");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            for (Task task : tasks) {
                String statusIcon = task.getStatus() == Task.Status.DONE ? "✓" : "x";
                String priorityColor = getPriorityColor(task.getPriority());
                
                System.out.println("\n[" + statusIcon + "] T-" + task.getId() + " | " + task.getTitle());
                System.out.println("    优先级: " + priorityColor + task.getPriority());
                System.out.println("    状态: " + task.getStatus());
                System.out.println("    创建时间: " + task.getCreatedAt().format(formatter));
                
                if (task instanceof DeadlineTask) {
                    DeadlineTask dt = (DeadlineTask) task;
                    if (dt.getDeadline() != null) {
                        System.out.println("截止时间: " + dt.getDeadline().format(formatter));
                    }
                }
            }
            
            System.out.println("\n共 " + tasks.size() + " 个任务");
            System.out.println("\n");
            
        } catch (Exception e) {
            System.err.println("显示任务列表时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getPriorityColor(Task.Priority priority) {
        return switch (priority) {
            case HIGH -> "高";
            case MEDIUM -> "中";
            case LOW -> "低";
        };
    }
}

