// 2354093 李雪菲

package com.garfield.Commands;

import picocli.CommandLine.Command;

@Command(
    name = "exit",
    description = "退出应用程序"
)
public class ExitCommand implements Runnable {
    
    private boolean shouldExit = false;
    
    @Override
    public void run() {
        shouldExit = true;
        System.out.println("Exit!");
    }
    
    public boolean shouldExit() {
        return shouldExit;
    }
    
    public void reset() {
        shouldExit = false;
    }
}

