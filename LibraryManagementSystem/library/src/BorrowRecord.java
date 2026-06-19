import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowRecord {
    private String recordId;
    private String memberId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BorrowRecord(String recordId, String memberId, String isbn) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.isbn = isbn;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(LOAN_PERIOD_DAYS);
        this.returned = false;
    }

    public String getRecordId() { return recordId; }
    public String getMemberId() { return memberId; }
    public String getIsbn() { return isbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return returned; }

    public void markReturned() {
        this.returned = true;
        this.returnDate = LocalDate.now();
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        String status = returned
                ? "Returned on " + returnDate.format(FORMATTER)
                : (isOverdue() ? "OVERDUE (Due: " + dueDate.format(FORMATTER) + ")" : "Active (Due: " + dueDate.format(FORMATTER) + ")");

        return String.format("[Record: %s] Member: %s | ISBN: %s | Borrowed: %s | Status: %s",
                recordId, memberId, isbn, borrowDate.format(FORMATTER), status);
    }
}
