import java.io.File;
import java.nio.file.Files;
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
        Path diretorioAtual = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

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
                System.out.println(command.substring(5));
            }
            else if (command.startsWith("grep")) {
                // System.out.println(command.substring(5));
            }
            else if (obterComandoPath(cmdArray[0]) != null) {
                Process process = Runtime.getRuntime().exec(cmdArray);
                process.getInputStream().transferTo(System.out);
                process.waitFor();
            }
            else if (command.startsWith("pwd")) {
                String pwd = Paths.get("").toAbsolutePath().toString();
                System.out.println(pwd);
            }
            else if (command.startsWith("cd")) {
                String result = command.substring(3);
                File file = new File(result);
                if(file.isDirectory() && file.isAbsolute()) {
                    diretorioAtual = Paths.get(file.getCanonicalPath()).toAbsolutePath().normalize();
                    System.out.println(diretorioAtual);
                }else {
                    System.out.println("cd :" + result + ": No such file or director");
                }
            }
        }
    }

    //verifica se o caminho é existente
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
