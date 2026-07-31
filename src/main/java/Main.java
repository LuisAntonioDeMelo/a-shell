import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        while (!command.equals("exit")) {
            System.out.print("$ ");

            command = input.nextLine();
            if(command.equals("exit")){
                break;
            }
            System.out.println(command + ": command not found");
        }
        System.out.println(command);
    }
}
