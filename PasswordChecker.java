import java.io.*;
import java.util.*;

public class PasswordChecker {
    private static Map<String, String> userCredentials = new HashMap<>();
    private static Map<String, String> adminCredentials = new HashMap<>();

    public PasswordChecker() {
        loadCredentialsFromFile();
    }

    private void loadCredentialsFromFile() {
    File file = new File("passwords.txt");
    if (!file.exists()) {
        try {
            file.createNewFile(); // create empty file if missing
        } catch (IOException e) {
            System.err.println("Failed to create passwords.txt");
            return;
        }
    }

    try (Scanner sc = new Scanner(file)) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue; // skip empty lines

            String[] parts = line.split("\\s+");
            if (parts.length != 3) continue; // skip invalid lines

            String type = parts[0];
            String uid = parts[1];
            String pass = parts[2];

            if ("student".equalsIgnoreCase(type)) {
                userCredentials.put(uid, pass);
            } else if ("admin".equalsIgnoreCase(type)) {
                adminCredentials.put(uid, pass);
            }
        }
    } catch (FileNotFoundException e) {
        System.err.println("Failed to open passwords.txt");
    }
}


    public void close() {
        writeCredentialsToFile();
    }

    public int validateUserLogin(String memId, String password) {
        if (!userCredentials.containsKey(memId)) {
            return -1;
        }
        return userCredentials.get(memId).equals(password) ? 1 : 0;
    }

    public int validateAdminLogin(String adminId, String password) {
        if (!adminCredentials.containsKey(adminId)) {
            return -1;
        }
        return adminCredentials.get(adminId).equals(password) ? 1 : 0;
    }

    public int validateLogin(String type, String userId, String password) {
        if ("student".equals(type) && userCredentials.containsKey(userId)) {
            return validateUserLogin(userId, password);
        } else if ("admin".equals(type) && adminCredentials.containsKey(userId)) {
            return validateAdminLogin(userId, password);
        }
        return -1; // User ID not found
    }

    public boolean changePassword(String userID) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter old password (or type exit to cancel): ");
        String oldPassword = sc.next();
        if (oldPassword.equals("exit")) {
            System.out.println("Password change cancelled.\n");
            return false;
        }

        System.out.print("Enter new password: ");
        String newPassword = sc.next();
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.next();

        if (!userCredentials.containsKey(userID)) {
            System.out.println("User not found.\n");
            return false;
        }

        if (!userCredentials.get(userID).equals(oldPassword)) {
            System.out.println("Old password is incorrect.\n");
        } else if (!newPassword.equals(confirmPassword)) {
            System.out.println("New passwords do not match.\n");
        } else if (newPassword.equals(oldPassword)) {
            System.out.println("New password cannot be the same as the old password.\n");
        } else {
            userCredentials.put(userID, newPassword);
            System.out.println("Password changed successfully.\n");
            return true;
        }
        return false;
    }

    private void writeCredentialsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("passwords.txt"))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.println("student " + entry.getKey() + " " + entry.getValue());
            }
            for (Map.Entry<String, String> entry : adminCredentials.entrySet()) {
                writer.println("admin " + entry.getKey() + " " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Failed to open passwords.txt for writing");
        }
    }
}
