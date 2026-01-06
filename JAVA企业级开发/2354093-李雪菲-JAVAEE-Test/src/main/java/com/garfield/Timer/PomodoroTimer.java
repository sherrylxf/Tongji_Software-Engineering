package com.garfield.Timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
Implement a class named PomodoroTimer that encapsulates the timer logic.
 */
public class PomodoroTimer {
    
    private ScheduledExecutorService scheduler;

    public void startTimer(int workSeconds, int breakSeconds, int cycles) {
        // single thread scheduled executor
        scheduler = Executors.newSingleThreadScheduledExecutor();

        Thread timerThread = new Thread(() -> {
            runPomodoroTimer(workSeconds, breakSeconds, cycles);
        });
        
        timerThread.setDaemon(false);
        timerThread.start();
    }

    private void runPomodoroTimer(int workSeconds, int breakSeconds, int cycles) {
        int currentCycle = 0;
        
        while (currentCycle < cycles) {
            currentCycle++;
            
            // work
            System.out.println("\n========================================");
            System.out.println("开始工作阶段 " + currentCycle + "/" + cycles);
            System.out.println("========================================");
            runSession(workSeconds, "工作");
            System.out.println("工作阶段 " + currentCycle + " 结束！");
            
            // break
            if (currentCycle < cycles) {
                System.out.println("\n========================================");
                System.out.println("开始休息时间");
                System.out.println("========================================");
                runSession(breakSeconds, "休息");
                System.out.println("休息时间结束！");
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("所有番茄钟循环完成！");
        System.out.println("========================================\n");
        
        // close scheduler
        shutdown();
    }

    private void runSession(int seconds, String sessionType) {
        AtomicInteger remainingSeconds = new AtomicInteger(seconds);

        Runnable countdownTask = new Runnable() {
            @Override
            public void run() {
                int remaining = remainingSeconds.getAndDecrement();
                if (remaining > 0) {
                    System.out.println("[" + sessionType + "] 剩余时间: " + remaining + " 秒");
                }
            }
        };

        scheduler.scheduleAtFixedRate(countdownTask, 0, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.err.println("计时器被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        scheduler.shutdownNow();

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
