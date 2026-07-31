import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        System.out.println(command + ": command not found");
        while (!command.equals("exit")) {
            System.out.print("$ ");
            command = input.nextLine();
            System.out.println(command + ": command not found");
        }
        if(input.equals("exit")) {
            System.exit(0);
        }
        System.out.println(command);
    }
}
