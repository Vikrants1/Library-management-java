import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
    private int recordCounter = 1;

    // -------------------- Book Operations --------------------

    public boolean addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            System.out.println("  ✗ A book with ISBN " + book.getIsbn() + " already exists.");
            return false;
        }
        books.put(book.getIsbn(), book);
        System.out.println("  ✓ Book added: " + book.getTitle());
        return true;
    }

    public boolean removeBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("  ✗ Book not found with ISBN: " + isbn);
            return false;
        }
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            System.out.println("  ✗ Cannot remove: some copies are currently borrowed.");
            return false;
        }
        books.remove(isbn);
        System.out.println("  ✓ Book removed: " + book.getTitle());
        return true;
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> searchBooksByTitle(String keyword) {
        String kw = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        String kw = author.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    // -------------------- Member Operations --------------------

    public boolean registerMember(Member member) {
        if (members.containsKey(member.getMemberId())) {
            System.out.println("  ✗ Member ID " + member.getMemberId() + " already exists.");
            return false;
        }
        members.put(member.getMemberId(), member);
        System.out.println("  ✓ Member registered: " + member.getName());
        return true;
    }

    public boolean removeMember(String memberId) {
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("  ✗ Member not found: " + memberId);
            return false;
        }
        if (!member.getBorrowedBookIsbns().isEmpty()) {
            System.out.println("  ✗ Cannot remove: member has unreturned books.");
            return false;
        }
        members.remove(memberId);
        System.out.println("  ✓ Member removed: " + member.getName());
        return true;
    }

    public Optional<Member> findMemberById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // -------------------- Borrow / Return Operations --------------------

    public boolean borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("  ✗ Member not found: " + memberId);
            return false;
        }
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("  ✗ Book not found: " + isbn);
            return false;
        }
        if (!member.canBorrow()) {
            System.out.println("  ✗ Member has reached the maximum borrow limit (3 books).");
            return false;
        }
        if (member.hasBorrowed(isbn)) {
            System.out.println("  ✗ Member has already borrowed this book.");
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("  ✗ No copies available for: " + book.getTitle());
            return false;
        }
        book.borrowCopy();
        member.borrowBook(isbn);
        String recordId = "REC" + String.format("%04d", recordCounter++);
        borrowRecords.add(new BorrowRecord(recordId, memberId, isbn));
        System.out.println("  ✓ Book borrowed successfully! Due in 14 days.");
        return true;
    }

    public boolean returnBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("  ✗ Member not found: " + memberId);
            return false;
        }
        if (!member.hasBorrowed(isbn)) {
            System.out.println("  ✗ This member has not borrowed that book.");
            return false;
        }
        Book book = books.get(isbn);
        if (book == null) {
            System.out.println("  ✗ Book record not found: " + isbn);
            return false;
        }
        book.returnCopy();
        member.returnBook(isbn);
        borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId) && r.getIsbn().equals(isbn) && !r.isReturned())
                .findFirst()
                .ifPresent(BorrowRecord::markReturned);
        System.out.println("  ✓ Book returned successfully!");
        return true;
    }

    // -------------------- Reports --------------------

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public List<BorrowRecord> getActiveRecords() {
        return borrowRecords.stream()
                .filter(r -> !r.isReturned())
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecords.stream()
                .filter(r -> !r.isReturned() && r.isOverdue())
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> getMemberHistory(String memberId) {
        return borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }
}
