import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// For demonstration of hashing - replace with a real library like BCrypt
import java.security.MessageDigest;

public class Login {
    private static List<User> users = new ArrayList<>();
    private static final String USER_FILE = "users.txt";

    static {
        loadUsers();
    }

    private static void loadUsers() {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Skip header line and empty lines
                if (line.trim().isEmpty() || line.startsWith("username,password_hash,name,email")) continue;
                
                if (parts.length >= 4) { // Use >= 4 for robustness
                    users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
             }
        } catch (IOException e) {
            // File may not exist on first run
        }
    }

    private static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            pw.println("username,password_hash,name,email"); // Indicate it's a hash
            for (User user : users) {
                // Save the hash, not the plain password
                pw.println(user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," + user.getEmail());            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public static User login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username) && verifyHash(password, user.getPassword())) {
                 System.out.println("Login successful! Welcome, " + user.getName() + ".");
                return user;
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public static void register(Scanner scanner) {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists.");
                return;
            }
        }
        System.out.print("Choose a password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        String hashedPassword = mockHashPassword(password);
        users.add(new User(username, hashedPassword, name, email));
        System.out.println("Registration successful! You can now login.");
    }

    public static void updateUser(User user) {
        // Called after profile/password update
        // Update the user in the list before saving
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
        saveUsers();
    }

    // --- Mock Hashing Methods (Replace with a real library like BCrypt) ---
    // These are INSECURE and for demonstration purposes only.
    public static String mockHashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public static boolean verifyHash(String plainPassword, String hashedPassword) {
        // In a real app, use BCrypt.checkpw(plainPassword, hashedPassword)
        return mockHashPassword(plainPassword).equals(hashedPassword);
    }
} 