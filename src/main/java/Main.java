import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        System.out.println(command + ": command not found");
    }
}
