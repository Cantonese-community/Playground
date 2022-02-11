import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class Cmd {
    public static void run(String[] command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        Scanner scanner = new Scanner(process.getInputStream());

        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }
}