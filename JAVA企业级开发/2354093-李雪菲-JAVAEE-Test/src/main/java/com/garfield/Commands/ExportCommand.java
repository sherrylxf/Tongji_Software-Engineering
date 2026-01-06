// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.Export.TaskExporter;
import com.garfield.model.InMemoryGarfieldTaskRepository;
import com.garfield.model.Task;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;

@Command(
    name = "export",
    description = "导出任务到JSON文件"
)
public class ExportCommand implements Runnable {
    
    @Option(names = {"--file", "-f"}, required = true, description = "输出文件名 (xxx.json)")
    private String filename;
    
    private InMemoryGarfieldTaskRepository repository;
    private TaskExporter exporter;
    
    public void setRepository(InMemoryGarfieldTaskRepository repository) {
        this.repository = repository;
    }
    
    public void setExporter(TaskExporter exporter) {
        this.exporter = exporter;
    }
    
    @Override
    public void run() {
        try {
            List<Task> tasks = repository.findAll();
            
            if (tasks.isEmpty()) {
                System.out.println("没有任务可以导出。");
                return;
            }
            
            exporter.exportTasks(tasks, filename);
            
        } catch (Exception e) {
            System.err.println("导出任务时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

