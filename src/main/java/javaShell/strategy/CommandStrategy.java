package javaShell.strategy;

import javaShell.entity.Command;
import javaShell.entity.CommandResult;
import javaShell.entity.Type;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@FunctionalInterface
public interface CommandStrategy {

    CommandResult execute(Command command, Path currentDirectory);

    static CommandStrategy exit() {
        return (command, currentDirectory) -> CommandResult.exitFrom(currentDirectory);
    }

    static CommandStrategy echo() {
        return (command, currentDirectory) -> {
            System.out.println(String.join(" ", command.getParameters()));
            return CommandResult.continueIn(currentDirectory);
        };
    }

    static CommandStrategy pwd() {
        return (command, currentDirectory) -> {
            System.out.println(currentDirectory);
            return CommandResult.continueIn(currentDirectory);
        };
    }

    static CommandStrategy cd() {
        return (command, currentDirectory) -> {
            String[] parameters = command.getParameters();
            String destination = parameters.length == 0 || parameters[0].equals("~")
                    ? System.getenv("HOME")
                    : parameters[0];
            Path target = Paths.get(destination);
            if (!target.isAbsolute()) {
                target = currentDirectory.resolve(target);
            }
            target = target.normalize().toAbsolutePath();

            if (Files.isDirectory(target)) {
                return CommandResult.continueIn(target);
            }
            System.out.println("cd: " + destination + ": No such file or directory");
            return CommandResult.continueIn(currentDirectory);
        };
    }

    static CommandStrategy type() {
        return (command, currentDirectory) -> {
            String[] parameters = command.getParameters();
            if (parameters.length == 0) {
                return CommandResult.continueIn(currentDirectory);
            }

            String commandName = parameters[0];
            Type.from(commandName).ifPresentOrElse(
                    type -> System.out.println(commandName + " is a shell builtin"),
                    () -> {
                        String executable = findExecutable(commandName);
                        System.out.println(executable == null
                                ? commandName + ": not found"
                                : commandName + " is " + executable);
                    });
            return CommandResult.continueIn(currentDirectory);
        };
    }

    static String findExecutable(String commandName) {
        String path = System.getenv("PATH");
        if (path == null) {
            return null;
        }
        for (String directory : path.split(File.pathSeparator)) {
            File executable = new File(directory, commandName);
            if (executable.isFile() && executable.canExecute()) {
                return executable.getAbsolutePath();
            }
        }
        return null;
    }
}
