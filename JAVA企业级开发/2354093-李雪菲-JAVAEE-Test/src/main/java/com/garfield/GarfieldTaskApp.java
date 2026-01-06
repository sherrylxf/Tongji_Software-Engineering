// 2354093 李雪菲

package com.garfield;

import com.garfield.Commands.*;
import com.garfield.Export.TaskExporter;
import com.garfield.Timer.PomodoroTimer;
import com.garfield.model.InMemoryGarfieldTaskRepository;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GarfieldTaskApp {

    public static void main(String[] args) {
        // --- Create Picocli Command Hub ---
        InMemoryGarfieldTaskRepository repository = new InMemoryGarfieldTaskRepository();
        PomodoroTimer timer = new PomodoroTimer();
        TaskExporter exporter = new TaskExporter();

        // --- Register Subcommands ---
        GarfieldTaskCommand mainCommand = new GarfieldTaskCommand();
        CommandLine cmd = new CommandLine(mainCommand);

        injectDependencies(cmd, repository, timer, exporter);

        ExitCommand exitCommand = getExitCommand(cmd);

        printWelcomeMessage();

        // --- Start REPL (Read-Evaluate-Print Loop) ---
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("garfield> ");
                String line = scanner.nextLine();

                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                String trimmedLine = line.trim();

                if ("exit".equalsIgnoreCase(trimmedLine) || "quit".equalsIgnoreCase(trimmedLine)) {
                    System.out.println("Goodbye!");
                    break;
                }

                // 处理 help 命令
                if ("help".equalsIgnoreCase(trimmedLine)) {
                    System.out.println("Executing: help");
                    cmd.usage(System.out);
                    System.out.println();
                    continue;
                }

                try {
                    // Use a RegEx-based parser to correctly handle quoted arguments
                    String[] arguments = smartSplit(trimmedLine);

                    // Print the parsed arguments for debugging
                    System.out.println("Executing: " + String.join(" ", arguments));

                    if (exitCommand != null) {
                        exitCommand.reset();
                    }

                    // Let Picocli parse and execute the command
                    // works perfectly with quoted strings
                    cmd.execute(arguments);

                    if (exitCommand != null && exitCommand.shouldExit()) {
                        System.out.println("Goodbye! (Garfield 要去睡觉了 💤)");
                        break;
                    }

                } catch (Exception e) {
                    System.err.println("An unexpected error occurred: " + e.getMessage());
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("发生意外错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            timer.shutdown();
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("欢迎使用 Garfield Task Manager\n");
        System.out.println("输入 '--help' 查看所有可用命令");
        System.out.println("输入 'exit' 或 'quit' 退出程序");
        System.out.println();
    }

    private static void injectDependencies(
            CommandLine cmd,
            InMemoryGarfieldTaskRepository repository,
            PomodoroTimer timer,
            TaskExporter exporter) {

        injectRepository(cmd, "add", repository);
        injectRepository(cmd, "list", repository);
        injectRepository(cmd, "done", repository);
        injectRepository(cmd, "remove", repository);

        CommandLine timerCmd = cmd.getSubcommands().get("timer");
        if (timerCmd != null) {
            TimerCommand timerCommand = (TimerCommand) timerCmd.getCommand();
            timerCommand.setTimer(timer);
        }

        CommandLine exportCmd = cmd.getSubcommands().get("export");
        if (exportCmd != null) {
            ExportCommand exportCommand = (ExportCommand) exportCmd.getCommand();
            exportCommand.setRepository(repository);
            exportCommand.setExporter(exporter);
        }
    }

    private static void injectRepository(
            CommandLine cmd,
            String commandName,
            InMemoryGarfieldTaskRepository repository) {
        
        CommandLine subCmd = cmd.getSubcommands().get(commandName);
        if (subCmd != null) {
            Object command = subCmd.getCommand();
            try {
                // 使用反射调用 setRepository 方法
                command.getClass()
                       .getMethod("setRepository", InMemoryGarfieldTaskRepository.class)
                       .invoke(command, repository);
            } catch (Exception e) {
                System.err.println("警告: 无法为命令 '" + commandName + "' 注入 repository");
            }
        }
    }

    private static ExitCommand getExitCommand(CommandLine cmd) {
        CommandLine exitCmd = cmd.getSubcommands().get("exit");
        if (exitCmd != null) {
            return (ExitCommand) exitCmd.getCommand();
        }
        return null;
    }

    /**
     * A robust command-line parser that handles quotes.
     * Students DO NOT need to modify this.
     * @param line The raw input line.
     * @return A String array of arguments.
     */
    private static String[] smartSplit(String line) {
        List<String> args = new ArrayList<>();
        // This regex matches quoted strings or non-space sequences
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|'([^']*)'|[^\\s]+");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Add content within double quotes
                args.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Add content within single quotes
                args.add(matcher.group(2));
            } else {
                // Add unquoted argument
                args.add(matcher.group());
            }
        }
        return args.toArray(new String[0]);
    }
}