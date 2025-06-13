import java.io.*;
import java.util.*;

public class MCQTestAutomation {
    public static void main(String[] args) {
        // Simulate a user
        User testUser = new User("testuser", "password", "Test User", "test@example.com");

        // Create a scanner with predefined answers for the test questions
        // For simplicity, answer all questions with option 1 (index 0)
        StringBuilder inputBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            inputBuilder.append("1\n");
        }
        Scanner scanner = new Scanner(new ByteArrayInputStream(inputBuilder.toString().getBytes()));

        // Start the test
        MCQTest.startTest(testUser, scanner);

        // After test, read results.txt to verify if the result was saved
        try (BufferedReader br = new BufferedReader(new FileReader("results.txt"))) {
            String line;
            boolean foundUser = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("User: " + testUser.getUsername())) {
                    foundUser = true;
                    System.out.println("Test result found for user: " + testUser.getUsername());
                    break;
                }
            }
            if (!foundUser) {
                System.out.println("Test result NOT found for user: " + testUser.getUsername());
            }
        } catch (IOException e) {
            System.out.println("Error reading results.txt: " + e.getMessage());
        }
    }
}
