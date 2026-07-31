import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String command = input.nextLine();
            if(command.equals("exit")){
                break;
            }
            else if(command.startsWith("type")) {
                String cmdSubtype = command.substring(5);
                List<String> commands = Arrays.asList("type", "echo", "exit", "grep");
                boolean existComand = commands.stream().anyMatch(cmd -> cmd.equals(cmdSubtype));
                boolean existtype = false;
                String path = System.getenv("PATH");
                String[] pathDirs = path.split(":");
//                for(String dir : pathDirs){
//                    File dirFile = new File(dir, existComand);
//                    if(dirFile.isDirectory()){
//                        System.out.println( cmdSubtype + " is " + dirFile.getAbsolutePath());
//                    }
                for(int i = 0; i < pathDirs.length; i++){
                 File file = new File(pathDirs[i], cmdSubtype);
                 if(file.exists() && file.canExecute()){
                     System.out.println(cmdSubtype + " is " + file.getAbsolutePath());
                 }
                }
                if(existComand){
                    System.out.println(cmdSubtype + " is a shell builtin");
                }else {
                    System.out.println( cmdSubtype+ ": not found");
                }

            }
            else if(command.startsWith("echo ")){
                System.out.println(command.substring(5));
            }
            else if(command.startsWith("grep ")){
               // System.out.println(command.substring(5));

            }
            else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
