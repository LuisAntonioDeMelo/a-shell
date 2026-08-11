package javaShell.entity;

import javaShell.strategy.CommandStrategy;

import java.util.Arrays;
import java.util.Optional;

public enum Type {
    EXIT("exit", CommandStrategy.exit()),
    ECHO("echo", CommandStrategy.echo()),
    TYPE("type", CommandStrategy.type()),
    PWD("pwd", CommandStrategy.pwd()),
    CD("cd", CommandStrategy.cd());

    private final String commandName;
    private final CommandStrategy strategy;

    Type(String commandName, CommandStrategy strategy) {
        this.commandName = commandName;
        this.strategy = strategy;
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandResult execute(Command command, java.nio.file.Path currentDirectory) {
        return strategy.execute(command, currentDirectory);
    }

    public static Optional<Type> from(String commandName) {
        return Arrays.stream(values())
                .filter(type -> type.commandName.equals(commandName))
                .findFirst();
    }
}
