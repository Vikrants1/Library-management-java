import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phone;
    private List<String> borrowedBookIsbns;

    private static final int MAX_BORROW_LIMIT = 3;

    public Member(String memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBookIsbns = new ArrayList<>();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<String> getBorrowedBookIsbns() { return borrowedBookIsbns; }

    public boolean canBorrow() {
        return borrowedBookIsbns.size() < MAX_BORROW_LIMIT;
    }

    public boolean borrowBook(String isbn) {
        if (canBorrow() && !borrowedBookIsbns.contains(isbn)) {
            borrowedBookIsbns.add(isbn);
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn) {
        return borrowedBookIsbns.remove(isbn);
    }

    public boolean hasBorrowed(String isbn) {
        return borrowedBookIsbns.contains(isbn);
    }

    @Override
    public String toString() {
        return String.format("[ID: %s] %s | Email: %s | Phone: %s | Books Borrowed: %d/%d",
                memberId, name, email, phone, borrowedBookIsbns.size(), MAX_BORROW_LIMIT);
    }
}
