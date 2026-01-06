// 2354093 李雪菲

package com.garfield;

import com.garfield.Commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

// This is the main command. Students will add subcommands to it.
@Command(
    name = "GarfieldTask",
    description = "A command-line tool to help Garfield manage his tasks. ",
    version = "GarfieldTask 1.0",
    mixinStandardHelpOptions = true,
    subcommands = {
        AddCommand.class,
        ListCommand.class,
        DoneCommand.class,
        RemoveCommand.class,
        TimerCommand.class,
        ExportCommand.class,
        ExitCommand.class
    }
)

public class GarfieldTaskCommand implements Runnable {
    // This class acts as a container for all other commands.
    // Students will need to pass the repository and other services
    // into the constructors of their real command classes.
    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}

