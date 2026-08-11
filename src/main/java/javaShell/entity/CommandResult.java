package javaShell.entity;

import java.nio.file.Path;

public record CommandResult(Path currentDirectory, boolean shouldExit) {

    public static CommandResult continueIn(Path currentDirectory) {
        return new CommandResult(currentDirectory, false);
    }

    public static CommandResult exitFrom(Path currentDirectory) {
        return new CommandResult(currentDirectory, true);
    }
}
