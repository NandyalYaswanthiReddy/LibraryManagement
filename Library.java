import java.io.*;
import java.util.*;

public class Library {
    private List<Book> allBooks;
    private List<Member> allMembers;
    private Map<String, Book> allBooksMap;
    private Map<String, Member> allMembersMap;
    private Map<String, String> titleToIsbn;

    public Library() {
        allBooks = new ArrayList<>();
        allMembers = new ArrayList<>();
        allBooksMap = new HashMap<>();
        allMembersMap = new HashMap<>();
        titleToIsbn = new HashMap<>();

        loadBooksFromFile();
        loadMembersFromFile();
    }

    // --- File Handling ---
    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String title = line.trim();
                String author = br.readLine().trim();
                String[] details = br.readLine().trim().split(" ");
                String isbn = details[0];
                int available = Integer.parseInt(details[1]);
                int total = Integer.parseInt(details[2]);
                br.readLine(); // skip blank line

                Book b = new Book(isbn, title, author, total, available);
                addBook(b);
            }
        } catch (IOException e) {
            System.out.println("Failed to open books.txt");
        }
    }

    private void loadMembersFromFile() {
    try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // skip blank lines

            String[] parts = line.split(" ");
            if (parts.length < 3) {
                System.out.println("Skipping invalid member line: " + line);
                continue;
            }

            String id = parts[0];
            String name = parts[1];
            int limit = Integer.parseInt(parts[2]);

            Member m = new Member(name, id, limit);

            String booksLine = br.readLine(); // borrowed books line
            if (booksLine != null && !booksLine.trim().isEmpty()) {
                String[] bookParts = booksLine.trim().split(" ");
                for (int i = 0; i < bookParts.length; i += 2) {
                    String isbn = bookParts[i];
                    int count = Integer.parseInt(bookParts[i + 1]);
                    m.getBorrowedBooks().put(isbn, count);
                }
            }

            registerMember(m);
        }
    } catch (IOException e) {
        System.out.println("Failed to open members.txt");
    }
}


    private void saveBooksToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : allBooks) {
                bw.write(b.getTitle() + "\n");
                bw.write(b.getAuthor() + "\n");
                bw.write(b.getISBN() + " " + b.getCopiesAvailable() + " " + b.getTotalCopies() + "\n\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to books.txt");
        }
    }

    private void saveMembersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt"))) {
            for (Member m : allMembers) {
                bw.write(m.getMemberID() + " " + m.getName() + " " + m.getBorrowLimit() + "\n");
                for (Map.Entry<String, Integer> entry : m.getBorrowedBooks().entrySet()) {
                    if (entry.getValue() > 0) {
                        bw.write(entry.getKey() + " " + entry.getValue() + " ");
                    }
                }
                bw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to members.txt");
        }
    }

    // --- Book operations ---
    private boolean addBook(Book b) {
        if (allBooksMap.containsKey(b.getISBN())) {
            System.out.println("Book with same ISBN already exists.");
            return false;
        }
        allBooks.add(b);
        allBooksMap.put(b.getISBN(), b);
        titleToIsbn.put(b.getTitle(), b.getISBN());
        return true;
    }

    public void addBookInteractive(Scanner sc) {
        System.out.println("Enter ISBN:");
        String isbn = sc.nextLine();
        System.out.println("Enter Title:");
        String title = sc.nextLine();
        System.out.println("Enter Author:");
        String author = sc.nextLine();
        System.out.println("Enter number of copies:");
        int total = sc.nextInt();
        sc.nextLine();
        Book b = new Book(isbn, title, author, total, total);
        if (addBook(b)) System.out.println("Book added successfully!");
        else System.out.println("Failed to add book.");
    }

    public void searchBook(Scanner sc) {
        System.out.println("Enter Title or ISBN to search:");
        String input = sc.nextLine();
        Book b = searchBookByTitle(input);
        if (b == null) b = searchBookByIsbn(input);
        if (b == null) System.out.println("Book not found.");
        else System.out.println(b);
    }

    public Book searchBookByTitle(String title) {
        if (!titleToIsbn.containsKey(title)) return null;
        return allBooksMap.get(titleToIsbn.get(title));
    }

    public Book searchBookByIsbn(String isbn) {
        return allBooksMap.getOrDefault(isbn, null);
    }

    public void deleteBook(Scanner sc) {
        System.out.println("Enter Title of book to delete:");
        String title = sc.nextLine();
        Book b = searchBookByTitle(title);
        if (b == null) {
            System.out.println("Book not found!");
            return;
        }
        if (b.getCopiesAvailable() != b.getTotalCopies()) {
            System.out.println("Cannot delete book. Not all copies returned.");
            return;
        }
        allBooksMap.remove(b.getISBN());
        titleToIsbn.remove(b.getTitle());
        allBooks.remove(b);
        System.out.println("Book deleted successfully!");
    }

    public void modifyBook(Scanner sc) {
        System.out.println("Enter ISBN of book to modify:");
        String isbn = sc.nextLine();
        Book b = allBooksMap.get(isbn);
        if (b == null) {
            System.out.println("Book not found!");
            return;
        }
        System.out.println("Enter new Title:");
        String title = sc.nextLine();
        System.out.println("Enter new Author:");
        String author = sc.nextLine();
        System.out.println("Enter new Total Copies:");
        int total = sc.nextInt();
        sc.nextLine();
        b.setTitle(title);
        b.setAuthor(author);
        b.setTotalCopies(total);
        System.out.println("Book modified successfully!");
    }

    // --- Member operations ---
    private boolean registerMember(Member m) {
        if (allMembersMap.containsKey(m.getMemberID())) {
            System.out.println("Member already exists.");
            return false;
        }
        allMembers.add(m);
        allMembersMap.put(m.getMemberID(), m);
        return true;
    }

    public void registerMember(Scanner sc) {
        System.out.println("Enter Member ID:");
        String id = sc.nextLine();
        System.out.println("Enter Name:");
        String name = sc.nextLine();
        System.out.println("Enter borrow limit:");
        int limit = sc.nextInt();
        sc.nextLine();
        Member m = new Member(name, id, limit);
        if (registerMember(m)) System.out.println("Member registered successfully!");
        else System.out.println("Failed to register member.");
    }

    public void deleteMember(Scanner sc) {
        System.out.println("Enter Member ID to delete:");
        String id = sc.nextLine();
        Member m = allMembersMap.get(id);
        if (m == null) {
            System.out.println("Member not found!");
            return;
        }
        if (m.getBooksBorrowed() > 0) {
            System.out.println("Member still has borrowed books.");
            return;
        }
        allMembersMap.remove(id);
        allMembers.remove(m);
        System.out.println("Member deleted successfully!");
    }

    public Member searchMember(String memberId) {
        return allMembersMap.get(memberId);
    }

    // --- Borrow / Return ---
    public boolean borrowBook(String memberId, Scanner sc) {
        Member m = allMembersMap.get(memberId);
        if (m == null) {
            System.out.println("Member not found!");
            return false;
        }
        System.out.println("Enter ISBN to borrow:");
        String isbn = sc.nextLine();
        Book b = allBooksMap.get(isbn);
        if (b == null) {
            System.out.println("Book not found!");
            return false;
        }
        if (b.getCopiesAvailable() <= 0) {
            System.out.println("No copies available.");
            return false;
        }
        if (m.borrowBook(isbn)) {
            b.borrowBook();
            System.out.println("Book borrowed successfully!");
            return true;
        }
        return false;
    }

    public boolean returnBook(String memberId, Scanner sc) {
        Member m = allMembersMap.get(memberId);
        if (m == null) {
            System.out.println("Member not found!");
            return false;
        }
        System.out.println("Enter ISBN to return:");
        String isbn = sc.nextLine();
        Book b = allBooksMap.get(isbn);
        if (b == null) {
            System.out.println("Book not found!");
            return false;
        }
        if (m.returnBook(isbn)) {
            b.returnBook();
            System.out.println("Book returned successfully!");
            return true;
        }
        return false;
    }

    // --- Print Methods ---
    public void printAllBooks() {
        if (allBooks.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        for (Book b : allBooks) {
            System.out.println(b);
        }
    }

    public void printAllMembers() {
        if (allMembers.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        for (Member m : allMembers) {
            System.out.println(m);
        }
    }

    public void printSpecificMember(Scanner sc) {
        System.out.println("Enter Member ID to display:");
        String id = sc.nextLine();
        Member m = allMembersMap.get(id);
        if (m == null) System.out.println("Member not found!");
        else System.out.println(m);
    }

    // --- Save data before exit ---
    public void closeLibrary() {
        saveBooksToFile();
        saveMembersToFile();
    }
}
