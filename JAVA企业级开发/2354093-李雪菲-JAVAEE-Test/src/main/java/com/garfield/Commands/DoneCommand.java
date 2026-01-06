// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.model.InMemoryGarfieldTaskRepository;
import com.garfield.model.Task;
import com.garfield.model.TaskException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "done",
    description = "标记任务为已完成"
)
public class DoneCommand implements Runnable {
    
    @Option(names = {"--id", "-i"}, required = true, description = "任务ID (1001)")
    private String taskId;
    
    private InMemoryGarfieldTaskRepository repository;
    
    public void setRepository(InMemoryGarfieldTaskRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void run() {
        try {
            Task task = repository.findById(taskId);
            task.setStatus(Task.Status.DONE);
            repository.update(task);
            
            System.out.println("任务 " + taskId + " 已标记为完成");
            
        } catch (TaskException e) {
            System.err.println("错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("标记任务完成时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

