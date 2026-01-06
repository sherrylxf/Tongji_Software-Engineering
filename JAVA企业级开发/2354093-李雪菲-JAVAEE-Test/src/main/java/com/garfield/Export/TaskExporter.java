// 2354093 李雪菲

package com.garfield.Export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.garfield.model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
Implement a class named TaskExporter that handles serializing and saving tasks.
 */
public class TaskExporter {
    private final ObjectMapper objectMapper;

    public TaskExporter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /*
    This class should have a method exportTasks(List<Task> tasks, String filename) that takes the
current list of tasks and a filename.
     */
    public void exportTasks(List<Task> tasks, String filename) {
        FileWriter writer = null;
        try {
            String jsonString = objectMapper.writeValueAsString(tasks);

            File file = new File(filename);

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            writer = new FileWriter(file);
            writer.write(jsonString);
            writer.flush();
            
            System.out.println("任务已成功导出到文件: " + filename);
            
        } catch (IOException e) {
            System.err.println("导出任务时发生错误: " + e.getMessage());
            e.printStackTrace();

            throw new RuntimeException("无法导出任务到文件: " + filename, e);
            
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("关闭文件写入器时发生错误: " + e.getMessage());
                }
            }
        }
    }
}
