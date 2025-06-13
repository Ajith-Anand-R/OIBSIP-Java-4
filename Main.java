import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Aptitude Test App ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            // System.out.println("3. Export Users Table");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    User user = Login.login(scanner);
                    if (user != null) {
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n--- Main Menu ---");
                            System.out.println("1. Take Test");
                            System.out.println("2. Update Profile/Password");
                            System.out.println("3. Logout");
                            System.out.print("Choose an option: ");
                            String mainChoice = scanner.nextLine();
                            switch (mainChoice) {
                                case "1":
                                    MCQTest.startTest(user, scanner);
                                    break;
                                case "2":
                                    System.out.println("1. Update Profile");
                                    System.out.println("2. Update Password");
                                    System.out.print("Choose an option: ");
                                    String profChoice = scanner.nextLine();
                                    if (profChoice.equals("1")) {
                                        Profile.updateProfile(user, scanner);
                                    } else if (profChoice.equals("2")) {
                                        Profile.updatePassword(user, scanner);
                                    } else {
                                        System.out.println("Invalid option.");
                                    }
                                    break;
                                case "3":
                                    System.out.println("Logged out successfully.");
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Invalid option. Try again.");
                            }
                        }
                    }
                    break;
                case "2":
                    Login.register(scanner);
                    break;
                // case "3":
                //     exportUsersTable();
                //     break;
                case "3":
                    System.out.println("Exiting. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showAllUsers() {
        System.out.println("\n---------------- USERS ----------------");
        System.out.printf("%-12s | %-10s | %-12s | %-20s\n", "Username", "Password", "Name", "Email");
        System.out.println("---------------------------------------------------------------");
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    System.out.printf("%-12s | %-10s | %-12s | %-20s\n",
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.txt: " + e.getMessage());
        }
        System.out.println("---------------------------------------------------------------\n");
    }

    private static void exportUsersTable() {
        String border = "+------------+----------+---------+----------------------+";
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"));
             PrintWriter pw = new PrintWriter(new FileWriter("users_table.txt"))) {
            pw.println(border);
            pw.println("| Username   | Password | Name    | Email                |");
            pw.println(border);
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                pw.printf("| %-10s | %-8s | %-7s | %-20s |%n",
                    parts.length>0?parts[0].trim():"",
                    parts.length>1?parts[1].trim():"",
                    parts.length>2?parts[2].trim():"",
                    parts.length>3?parts[3].trim():"");
            }
            pw.println(border);
            System.out.println("\nExported users_table.txt in neat table format!\n");
        } catch (IOException e) {
            System.out.println("Error exporting users table: " + e.getMessage());
        }
    }
} 