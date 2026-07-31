import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        String path = System.getenv("PATH");
        String[] pathDirs = path.split(":");

        while (true) {
            System.out.print("$ ");
            String command = input.nextLine();
            if (command.equals("exit")) {
                break;
            }
            else if(command.equals("help")) {
                //nao implementado ainda
            }
            else if (command.startsWith("type")) {
                String cmdSubtype = command.substring(5);
                String output = "";

                List<String> commands = Arrays.asList("type", "echo", "exit", "grep");
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
            else if (command.startsWith("echo ")) {
                System.out.println(command.substring(5));
            }
            else if (command.startsWith("grep ")) {
                // System.out.println(command.substring(5));
            }
            else if(obterComandoPath(command) != null) {
                Process process = new ProcessBuilder(command).start();
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
        if(pathEvn == null) {
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
