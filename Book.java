import java.util.ArrayList;
import java.util.Scanner;

public class Book {
    private String isbn;
    private int copiesAvailable;
    private int totalCopies;
    private ArrayList<String> reservedUsers = new ArrayList<>();

    private String title;
    private String author;

    // Default constructor
    public Book() {
        this.title = "";
        this.author = "";
        this.isbn = "";
        this.copiesAvailable = 0;
        this.totalCopies = 0;
    }

    // Parameterized constructor
    public Book(String isbn, String title, String author, int totalCopies, int copiesAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.copiesAvailable = copiesAvailable;
    }

    // Copy constructor
    public Book(Book reference, String newIsbn) {
        this.title = reference.title;
        this.author = reference.author;
        this.isbn = newIsbn;
        this.copiesAvailable = reference.copiesAvailable;
        this.totalCopies = reference.totalCopies;
    }

    // Getters
    public String getISBN() {
        return isbn;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public void setTitle(String title) {
    this.title = title;
}

public void setAuthor(String author) {
    this.author = author;
}

public void setTotalCopies(int totalCopies) {
    // Adjust available copies accordingly
    int diff = totalCopies - this.totalCopies;
    this.totalCopies = totalCopies;
    this.copiesAvailable += diff; // maintain consistency
    if (copiesAvailable < 0) copiesAvailable = 0;
}
    // Borrow a book
    public boolean borrowBook() {
        if (copiesAvailable <= 0) {
            System.out.println("Invalid request! Copy of book not available");
            return false;
        }
        copiesAvailable--;
        return true;
    }

    // Return a book
    public boolean returnBook() {
        if (copiesAvailable >= totalCopies) {
            System.out.println("Invalid request! You do not contain this book");
            return false;
        }
        copiesAvailable++;
        return true;
    }

    // Update copies
    public void updateCopies(int count) {
        if (totalCopies + count < 0 || copiesAvailable + count < 0) {
            System.out.println("Invalid request! Count becomes negative");
            return;
        }
        totalCopies += count;
        copiesAvailable += count;
    }

    // Modify book details interactively
    public void modifyBook() {
        printDetails();
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter new details for the book:");
        System.out.print("Enter new title: ");
        this.title = sc.nextLine();
        System.out.print("Enter new author: ");
        this.author = sc.nextLine();
    }

    // Print book details
    public void printDetails() {
        System.out.println("\nBOOK DETAILS :");
        System.out.println("Book ISBN        : " + isbn);
        System.out.println("Book Title       : " + title);
        System.out.println("Book Author      : " + author);
        System.out.println("Copies Available : " + copiesAvailable + "/" + totalCopies);
    }

    // Read book from input
    public void readFromInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter The Name Of The Book: ");
        this.title = sc.nextLine();
        System.out.print("Enter The Author's Name: ");
        this.author = sc.nextLine();
        System.out.print("Enter valid ISBN: ");
        this.isbn = sc.nextLine();
        System.out.print("Total Copies: ");
        while (true) {
            try {
                this.totalCopies = Integer.parseInt(sc.nextLine());
                if (this.totalCopies > 0) break;
                System.out.print("Invalid input! Please enter a positive integer for total copies: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a positive integer for total copies: ");
            }
        }
        this.copiesAvailable = this.totalCopies;
    }

    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;
        Book other = (Book) obj;
        return this.isbn.equals(other.isbn);
    }

    // toString for printing
    @Override
    public String toString() {
        return "ISBN: " + isbn + " | Title: " + title + " | Author: " + author +
               " | Available: " + copiesAvailable + "/" + totalCopies;
    }
}
