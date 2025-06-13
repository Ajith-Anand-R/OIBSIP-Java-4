import java.util.Scanner;

public class Profile {
    public static void updateProfile(User user, Scanner scanner) {
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        System.out.print("Enter new email (leave blank to keep current): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        System.out.println("Profile updated successfully.");
        Login.updateUser(user);
    }

    public static void updatePassword(User user, Scanner scanner) {
        System.out.print("Enter current password: ");
        String current = scanner.nextLine();
        // In a real app, verify the hash: if (!Login.verifyHash(current, user.getPassword())) {
        if (!user.getPassword().equals(current)) { // Still using plain text comparison here
            if (!Login.verifyHash(current, user.getPassword())) {
                System.out.println("Incorrect current password.");
                return;
            }
        }
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        String hashedNewPass = Login.mockHashPassword(newPass);
        user.setPassword(hashedNewPass);
        System.out.println("Password updated successfully.");
        Login.updateUser(user);
    }
} 