// 2354093 李雪菲

package com.garfield.Commands;

import com.garfield.Timer.PomodoroTimer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "timer",
    description = "启动番茄钟计时器"
)
public class TimerCommand implements Runnable {
    
    @Option(names = {"--work", "-w"}, description = "工作时长，默认: 25")
    private int workSeconds = 25;
    
    @Option(names = {"--break", "-b"}, description = "休息时长，默认: 5")
    private int breakSeconds = 5;
    
    @Option(names = {"--cycles", "-c"}, description = "循环时长，默认: 2")
    private int cycles = 2;
    
    private PomodoroTimer timer;
    
    public void setTimer(PomodoroTimer timer) {
        this.timer = timer;
    }
    
    @Override
    public void run() {
        try {
            if (workSeconds <= 0 || breakSeconds <= 0 || cycles <= 0) {
                System.err.println("错误: 时间参数必须为正数");
                return;
            }
            
            System.out.println("启动番茄钟计时器:");
            System.out.println("工作时长: " + workSeconds + " 秒");
            System.out.println("休息时长: " + breakSeconds + " 秒");
            System.out.println("循环次数: " + cycles);
            
            timer.startTimer(workSeconds, breakSeconds, cycles);
            
        } catch (Exception e) {
            System.err.println("启动计时器时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

