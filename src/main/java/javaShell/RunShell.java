package javaShell;

import javaShell.entity.Command;
import javaShell.entity.CommandResult;
import javaShell.entity.Type;
import javaShell.strategy.CommandStrategy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class RunShell {

    public static void run() throws Exception {
        new RunShell().start();
    }

    public void start() throws Exception {
        Scanner input = new Scanner(System.in);
        Path currentDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

        while (true) {
            System.out.print("$ ");
            if (!input.hasNextLine()) {
                break;
            }

            Command command = new Command(input.nextLine());
            if (command.isEmpty()) {
                continue;
            }

            Optional<Type> type = command.getType();
            if (type.isPresent()) {
                CommandResult result = type.orElseThrow().execute(command, currentDirectory);
                currentDirectory = result.currentDirectory();
                if (result.shouldExit()) {
                    break;
                }
            } else {
                executeExternal(command, currentDirectory);
            }
        }
    }

    private void executeExternal(Command command, Path currentDirectory) throws Exception {
        if (CommandStrategy.findExecutable(command.getName()) == null) {
            System.out.println(command.getName() + ": command not found");
            return;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command.getArguments());
        processBuilder.directory(currentDirectory.toFile());
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out);
        process.waitFor();
    }
}
