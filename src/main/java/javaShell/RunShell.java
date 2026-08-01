package javaShell;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RunShell {

    public void start() throws  Exception {
        Scanner input = new Scanner(System.in);
        String path = System.getenv("PATH");
        String[] pathDirs = path.split(File.pathSeparator);
        Path currentDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();


        while (true) {
            System.out.print("$ ");
            String command = input.nextLine();
            String[] cmdArray = command.split(" ");
            if (command.equals("exit")) {
                break;
            }
            else if (command.equals("help")) {
                //nao implementado ainda
            }
            else if (command.equals("ls")) {
                // nao implementado
            }
            else if (command.startsWith("type")) {
                String cmdSubtype = command.substring(5);
                String output = "";

                List<String> commands = Arrays.asList("type", "echo", "exit", "grep", "pwd", "cd");
                boolean existComand = commands.stream().anyMatch(cmd -> cmd.equals(cmdSubtype));

                if (!existComand) {
                    for (int i = 0; i < pathDirs.length; i++) {
                        File file = new File(pathDirs[i], cmdSubtype);
                        if (file.exists() && file.canExecute()) {
                            output = cmdSubtype + " is " + file.getAbsolutePath();
                        }
                    }
                    if (output.isEmpty()) {
                        output = cmdSubtype + ": not found";
                    }
                } else {
                    output = cmdSubtype + " is a shell builtin";
                }
                System.out.println(output);
            }
            else if (command.startsWith("echo")) {
                String cmd = command.substring(5);
                if(cmd.startsWith("'")) {
                    if(cmd.endsWith("'")){
                        cmd.replaceAll(" ", "");
                    }
                    System.out.println(cmd.replaceAll("\'",""));
                }else {
                    System.out.println(command.substring(5));
                }
            }
            else if (command.startsWith("grep")) {
                // System.out.println(command.substring(5));
            }
            else if (command.startsWith("pwd")) {
                System.out.println(currentDirectory);
            }
            else if (command.equals("cd") || command.startsWith("cd")) {
                String destination = command.equals("cd") ? System.getenv("HOME") : command.substring(3);
                Path target = Paths.get(destination);
                if (!target.isAbsolute()) {
                    target = currentDirectory.resolve(target);
                }
                if(command.substring(3).equals("~")) {
                    destination = System.getenv("HOME");
                    target = Paths.get(destination);
                    target = currentDirectory.resolve(target);
                }
                target = target.normalize();

                if (Files.isDirectory(target)) {
                    currentDirectory = target.toAbsolutePath();
                } else {
                    System.out.println("cd: " + destination + ": No such file or directory");
                }
            }
            else if (obterComandoPath(cmdArray[0]) != null) {
                ProcessBuilder processBuilder = new ProcessBuilder(cmdArray);
                processBuilder.directory(currentDirectory.toFile());
                Process process = processBuilder.start();
                process.getInputStream().transferTo(System.out);
                process.waitFor();
            }
            else {
                System.out.println(command + ": command not found");
            }
        }
    }

    private static String obterComandoPath(String command) {
        String pathEvn = System.getenv("PATH");
        if (pathEvn == null) {
            return null;
        }
        String[] pathDirs = pathEvn.split(File.pathSeparator);
        for (String pathDir : pathDirs) {
            File file = new File(pathDir, command);
            if (file.exists() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

}
