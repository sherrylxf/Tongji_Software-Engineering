// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.model.InMemoryGarfieldTaskRepository;
import com.garfield.model.TaskException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "remove",
    description = "删除任务"
)
public class RemoveCommand implements Runnable {
    
    @Option(names = {"--id", "-i"}, required = true, description = "任务ID (例如: T-1001)")
    private String taskId;
    
    private InMemoryGarfieldTaskRepository repository;
    
    public void setRepository(InMemoryGarfieldTaskRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void run() {
        try {
            repository.deleteById(taskId);
            System.out.println("任务 " + taskId + " 已删除");
            
        } catch (TaskException e) {
            System.err.println("错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("删除任务时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

