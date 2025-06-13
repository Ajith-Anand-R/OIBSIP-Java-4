import java.io.*;
import java.util.*;

public class FullAppTestAutomation {
    public static void main(String[] args) {
        try {
            // Clean up users.txt and results.txt for fresh test
            new PrintWriter("users.txt").close();
            new PrintWriter("results.txt").close();

            Scanner scanner;
            String inputSequence;

            // 1. Register a new user
            inputSequence = "testuser\npassword\nTest User\ntest@example.com\n";
            scanner = new Scanner(new ByteArrayInputStream(inputSequence.getBytes()));
            Login.register(scanner);

            // 2. Login with the new user
            inputSequence = "testuser\npassword\n";
            scanner = new Scanner(new ByteArrayInputStream(inputSequence.getBytes()));
            User user = Login.login(scanner);
            if (user == null) {
                System.out.println("Login failed during automated test.");
                return;
            }

            // 3. Update profile
            inputSequence = "New Name\nnewemail@example.com\n";
            scanner = new Scanner(new ByteArrayInputStream(inputSequence.getBytes()));
            Profile.updateProfile(user, scanner);

            // 4. Update password
            inputSequence = "password\nnewpassword\n";
            scanner = new Scanner(new ByteArrayInputStream(inputSequence.getBytes()));
            Profile.updatePassword(user, scanner);

            // 5. Login again with new password
            inputSequence = "testuser\nnewpassword\n";
            scanner = new Scanner(new ByteArrayInputStream(inputSequence.getBytes()));
            user = Login.login(scanner);
            if (user == null) {
                System.out.println("Login with new password failed during automated test.");
                return;
            }

            // 6. Take the test - answer all questions with option 1
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                answers.append("1\n");
            }
            scanner = new Scanner(new ByteArrayInputStream(answers.toString().getBytes()));
            MCQTest.startTest(user, scanner);

            // 7. Verify results.txt contains the test result for the user
            boolean foundResult = false;
            try (BufferedReader br = new BufferedReader(new FileReader("results.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("User: " + user.getUsername())) {
                        foundResult = true;
                        break;
                    }
                }
            }
            if (foundResult) {
                System.out.println("Test result found for user: " + user.getUsername());
            } else {
                System.out.println("Test result NOT found for user: " + user.getUsername());
            }

        } catch (Exception e) {
            System.out.println("Exception during automated test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
