import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryService service = new LibraryService();

    public static void main(String[] args) {
        seedData();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     LIBRARY MANAGEMENT SYSTEM        ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowMenu();
                case 4 -> reportsMenu();
                case 0 -> {
                    System.out.println("\n  Goodbye! Thank you for using the Library System.");
                    running = false;
                }
                default -> System.out.println("  ✗ Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    // -------------------- Menus --------------------

    private static void printMainMenu() {
        System.out.println("\n──────────────────────────────────────");
        System.out.println("  MAIN MENU");
        System.out.println("  1. Book Management");
        System.out.println("  2. Member Management");
        System.out.println("  3. Borrow / Return");
        System.out.println("  4. Reports");
        System.out.println("  0. Exit");
        System.out.println("──────────────────────────────────────");
    }

    private static void bookMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── BOOK MANAGEMENT ──");
            System.out.println("  1. Add Book");
            System.out.println("  2. Remove Book");
            System.out.println("  3. Search by Title");
            System.out.println("  4. Search by Author");
            System.out.println("  5. List All Books");
            System.out.println("  6. List Available Books");
            System.out.println("  0. Back");
            int choice = readInt("  Choice: ");
            switch (choice) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> searchByTitle();
                case 4 -> searchByAuthor();
                case 5 -> listAllBooks();
                case 6 -> listAvailableBooks();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid option.");
            }
        }
    }

    private static void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── MEMBER MANAGEMENT ──");
            System.out.println("  1. Register Member");
            System.out.println("  2. Remove Member");
            System.out.println("  3. View Member Details");
            System.out.println("  4. List All Members");
            System.out.println("  0. Back");
            int choice = readInt("  Choice: ");
            switch (choice) {
                case 1 -> registerMember();
                case 2 -> removeMember();
                case 3 -> viewMember();
                case 4 -> listAllMembers();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid option.");
            }
        }
    }

    private static void borrowMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── BORROW / RETURN ──");
            System.out.println("  1. Borrow a Book");
            System.out.println("  2. Return a Book");
            System.out.println("  0. Back");
            int choice = readInt("  Choice: ");
            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid option.");
            }
        }
    }

    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── REPORTS ──");
            System.out.println("  1. All Borrow Records");
            System.out.println("  2. Active Borrows");
            System.out.println("  3. Overdue Books");
            System.out.println("  4. Member Borrow History");
            System.out.println("  0. Back");
            int choice = readInt("  Choice: ");
            switch (choice) {
                case 1 -> printList("All Borrow Records", service.getAllBorrowRecords());
                case 2 -> printList("Active Borrows", service.getActiveRecords());
                case 3 -> printList("Overdue Books", service.getOverdueRecords());
                case 4 -> memberHistory();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid option.");
            }
        }
    }

    // -------------------- Book Actions --------------------

    private static void addBook() {
        System.out.println("\n  -- Add New Book --");
        String isbn = readString("  ISBN: ");
        String title = readString("  Title: ");
        String author = readString("  Author: ");
        String genre = readString("  Genre: ");
        int copies = readInt("  Number of Copies: ");
        service.addBook(new Book(isbn, title, author, genre, copies));
    }

    private static void removeBook() {
        String isbn = readString("\n  Enter ISBN to remove: ");
        service.removeBook(isbn);
    }

    private static void searchByTitle() {
        String keyword = readString("\n  Enter title keyword: ");
        printList("Search Results", service.searchBooksByTitle(keyword));
    }

    private static void searchByAuthor() {
        String author = readString("\n  Enter author name: ");
        printList("Search Results", service.searchBooksByAuthor(author));
    }

    private static void listAllBooks() {
        printList("All Books", service.getAllBooks());
    }

    private static void listAvailableBooks() {
        printList("Available Books", service.getAvailableBooks());
    }

    // -------------------- Member Actions --------------------

    private static void registerMember() {
        System.out.println("\n  -- Register New Member --");
        String id = readString("  Member ID: ");
        String name = readString("  Name: ");
        String email = readString("  Email: ");
        String phone = readString("  Phone: ");
        service.registerMember(new Member(id, name, email, phone));
    }

    private static void removeMember() {
        String id = readString("\n  Enter Member ID to remove: ");
        service.removeMember(id);
    }

    private static void viewMember() {
        String id = readString("\n  Enter Member ID: ");
        service.findMemberById(id).ifPresentOrElse(
                m -> {
                    System.out.println("\n  " + m);
                    List<String> isbns = m.getBorrowedBookIsbns();
                    if (isbns.isEmpty()) {
                        System.out.println("  No books currently borrowed.");
                    } else {
                        System.out.println("  Currently borrowed ISBNs: " + String.join(", ", isbns));
                    }
                },
                () -> System.out.println("  ✗ Member not found.")
        );
    }

    private static void listAllMembers() {
        printList("All Members", service.getAllMembers());
    }

    // -------------------- Borrow/Return Actions --------------------

    private static void borrowBook() {
        System.out.println("\n  -- Borrow Book --");
        String memberId = readString("  Member ID: ");
        String isbn = readString("  Book ISBN: ");
        service.borrowBook(memberId, isbn);
    }

    private static void returnBook() {
        System.out.println("\n  -- Return Book --");
        String memberId = readString("  Member ID: ");
        String isbn = readString("  Book ISBN: ");
        service.returnBook(memberId, isbn);
    }

    private static void memberHistory() {
        String memberId = readString("\n  Enter Member ID: ");
        printList("Borrow History for " + memberId, service.getMemberHistory(memberId));
    }

    // -------------------- Helpers --------------------

    private static <T> void printList(String title, List<T> items) {
        System.out.println("\n  ── " + title + " ──");
        if (items.isEmpty()) {
            System.out.println("  (No records found)");
        } else {
            items.forEach(item -> System.out.println("  " + item));
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid number.");
            }
        }
    }

    // -------------------- Seed Data --------------------

    private static void seedData() {
        service.addBook(new Book("978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee", "Fiction", 3));
        service.addBook(new Book("978-0-7432-7356-5", "1984", "George Orwell", "Dystopian", 2));
        service.addBook(new Book("978-0-14-028329-7", "Of Mice and Men", "John Steinbeck", "Fiction", 2));
        service.addBook(new Book("978-0-06-093546-9", "To Kill a Mockingbird Special Edition", "Harper Lee", "Fiction", 1));
        service.addBook(new Book("978-0-7432-7357-2", "Animal Farm", "George Orwell", "Satire", 4));

        service.registerMember(new Member("M001", "Alice Johnson", "alice@email.com", "9876543210"));
        service.registerMember(new Member("M002", "Bob Smith", "bob@email.com", "9123456780"));
        service.registerMember(new Member("M003", "Carol White", "carol@email.com", "9988776655"));
    }
}
