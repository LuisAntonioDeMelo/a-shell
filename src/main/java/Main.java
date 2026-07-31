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
                List<String> commands = Arrays.asList("type", "echo", "exit");
                boolean existComand = commands.stream().anyMatch(cmd -> cmd.equals(command.substring(5)));
                if(!existComand){
                    System.out.println(command.substring(5) + ": not found");
                }
                else {
                    System.out.println(command.substring(5) + " is a shell builtin");
                }
            }
            else if(command.startsWith("echo ")){
                System.out.println(command.substring(5));
            }
            else {
                System.out.println(command + ": command not found");
            }
        }
    }
}
