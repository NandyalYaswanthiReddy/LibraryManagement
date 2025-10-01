import java.util.Scanner;

public class Messenger {
    private Scanner sc;

    public Messenger() {
        sc = new Scanner(System.in);
    }

    public int displayMainMenu() {
        System.out.println("MAIN MENU\n");
        System.out.println("1. ADMINISTRATOR LOGIN\n");
        System.out.println("2. STUDENT LOGIN\n");
        System.out.print("Please Select Your Option (1-2): ");
        return sc.nextInt();
    }

    // In Messenger.java
public String[] displayLoginInterface() {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n\t\tLOGIN INTERFACE\n");
    System.out.print("Enter User Id and Password (separated by space) or '0' to return: ");
    String line = sc.nextLine().trim();
    if (line.equals("0")) return new String[]{"", ""};

    String[] parts = line.split("\\s+");
    if (parts.length != 2) return new String[]{"", ""}; // invalid input
    return parts; // parts[0]=uid, parts[1]=password
}



    public void displayInvalidUserID() {
        System.out.println("\nInvalid UserID");
        System.out.println("Please register yourself");
    }

    public void displayIncorrectPassword() {
        System.out.println("Incorrect Password. Login again with correct credentials");
    }

    public int displayAdministratorMenu() {
        System.out.println("\n\t\tADMINISTRATOR MENU");
        System.out.println("1. DISPLAY ALL STUDENT RECORD");
        System.out.println("2. DISPLAY SPECIFIC STUDENT RECORD");
        System.out.println("3. DISPLAY ALL BOOKS");
        System.out.println("4. DISPLAY SPECIFIC BOOK");
        System.out.println("5. ADD BOOK");
        System.out.println("6. MODIFY BOOK");
        System.out.println("7. DELETE BOOK");
        System.out.println("8. DELETE STUDENT RECORD");
        System.out.println("9. CREATE STUDENT RECORD");
        System.out.println("10. LOGOUT");
        System.out.print("Enter Your Choice (1-10): ");
        return sc.nextInt();
    }

    public int displayStudentMenu(String userID) {
        System.out.println("User: " + userID);
        System.out.println("1. Search Book");
        System.out.println("2. Borrow Book");
        System.out.println("3. Return Book");
        System.out.println("4. View Borrowed Books");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.print("Enter Your Choice (1-6): ");
        return sc.nextInt();
    }

    public void displayBookNotFound() {
        System.out.println("Book Not Found");
    }
}

