import java.util.*;

public class Main {
    public static void main(String[] args) {
        Messenger M = new Messenger();
        PasswordChecker P = new PasswordChecker();
        Library L = new Library();

        Scanner sc = new Scanner(System.in);

        while (true) {
            int AorS = M.displayMainMenu();
            if (AorS == 1) { // ADMIN LOGIN
                while (true) {
                    String[] login = M.displayLoginInterface();
                    String userID = login[0];
                    String password = login[1];

                    if (userID.equals("")) break;

                    int validation = P.validateLogin("admin", userID, password);
                    if (validation == -1) {
                        M.displayInvalidUserID();
                    } else if (validation == 0) {
                        M.displayIncorrectPassword();
                    } else {
                        boolean adminLoggedIn = true;
                        while (adminLoggedIn) {
                            int choice = M.displayAdministratorMenu();
                            switch (choice) {
                                case 1 -> L.printAllMembers();
                                case 2 -> L.printSpecificMember(sc);
                                case 3 -> L.printAllBooks();
                                case 4 -> L.searchBook(sc);
                                case 5 -> L.addBookInteractive(sc);
                                case 6 -> L.modifyBook(sc);
                                case 7 -> L.deleteBook(sc);
                                case 8 -> L.deleteMember(sc);
                                case 9 -> L.registerMember(sc);
                                case 10 -> adminLoggedIn = false; // logout
                                default -> System.out.println("Invalid choice! Please try again.");
                            }
                        }
                        break;
                    }
                }
            } else if (AorS == 2) { // STUDENT LOGIN
                while (true) {
                    String[] login = M.displayLoginInterface();
                    String userID = login[0];
                    String password = login[1];

                    if (userID.equals("")) break;

                    int validation = P.validateLogin("student", userID, password);

                    if (validation == -1) {
                        M.displayInvalidUserID();
                    } else if (validation == 0) {
                        M.displayIncorrectPassword();
                    } else {
                        boolean studentLoggedIn = true;
                        while (studentLoggedIn) {
                            int choice = M.displayStudentMenu(userID);
                            switch (choice) {
                                case 1 -> L.searchBook(sc);
                                case 2 -> L.borrowBook(userID, sc);
                                case 3 -> L.returnBook(userID, sc);
                                case 4 -> {
                                    Member mem = L.searchMember(userID);
                                    if (mem != null) mem.printBorrowedBooks();
                                }
                                case 5 -> P.changePassword(userID);
                                case 6 -> studentLoggedIn = false; // logout
                                default -> System.out.println("Invalid choice! Please try again.");
                            }
                        }
                        break;
                    }
                }
            } else {
                break;
            }
            System.out.println();
        }

        // Save credentials before exit
        P.close();
        L.closeLibrary();
        sc.close();
    }
}
