import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Member {
    private String memberID;
    private String name;
    private Map<String, Integer> borrowedBooks;
    private int borrowLimit;

    // Default constructor
    public Member() {
        this.borrowedBooks = new HashMap<>();
        this.borrowLimit = 3; // default limit
        this.name = "";
        this.memberID = "";
    }

    // Parameterized constructor
    public Member(String name, String id, int limit) {
        this.name = name;
        this.memberID = id;
        this.borrowLimit = limit;
        this.borrowedBooks = new HashMap<>();
    }

    // Getters
    public String getMemberID() {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public Map<String, Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getBooksBorrowed() {
        int count = 0;
        for (int value : borrowedBooks.values()) {
            count += value;
        }
        return count;
    }

    // Borrow a book
    public boolean borrowBook(String isbn) {
        if (borrowLimit <= 0) {
            System.out.println("Invalid request! Borrow limit exceeded. You cannot borrow more books.\n");
            return false;
        } else {
            borrowedBooks.put(isbn, borrowedBooks.getOrDefault(isbn, 0) + 1);
            borrowLimit--;
            return true;
        }
    }

    // Return a book
    public boolean returnBook(String isbn) {
        if (!borrowedBooks.containsKey(isbn) || borrowedBooks.get(isbn) == 0) {
            System.out.println("Invalid request! Book not borrowed.");
            return false;
        } else {
            borrowedBooks.put(isbn, borrowedBooks.get(isbn) - 1);
            borrowLimit++;
            return true;
        }
    }

    // Print borrowed books
    public void printBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("\n" + name + " has not borrowed any books\n");
            return;
        }
        System.out.printf("%-30s %-15s\n", "\nISBN of Book Borrowed", "No. of Copies");
        for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.printf("%-30s %-15d\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println();
    }

    // Print member details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Member ID: ").append(memberID).append("\n");
        sb.append("Name     : ").append(name).append("\n");
        sb.append("Borrow Limit: ").append(borrowLimit).append("\n");
        sb.append("Borrowed Books: \n");
        for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append("ISBN: ").append(entry.getKey())
                  .append(" | Copies: ").append(entry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    // Input member details
    public void inputFromUser() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter The Name Of The Student: ");
        this.name = sc.nextLine();
        System.out.println("Enter The Admission no. : ");
        this.memberID = sc.nextLine();
        System.out.println("Enter Borrow Limit : ");
        while (true) {
            if (sc.hasNextInt()) {
                int limit = sc.nextInt();
                if (limit > 0) {
                    this.borrowLimit = limit;
                    break;
                }
            }
            System.out.println("Invalid input! Please enter a positive integer for borrow limit: ");
            sc.nextLine(); // clear invalid input
        }
    }

    // Equality check
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Member)) return false;
        Member other = (Member) obj;
        return this.memberID.equals(other.memberID);
    }

    @Override
    public int hashCode() {
        return memberID.hashCode();
    }
}
