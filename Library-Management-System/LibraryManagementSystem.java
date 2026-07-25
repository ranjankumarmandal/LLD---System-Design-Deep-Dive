import java.time.LocalDateTime;
import java.util.*;

enum Role {
    MEMBER,
    LIBRARIAN
}

enum TransactionType {
    BORROW,
    RETURN
}

enum BookCopyStatus {
    AVAILABLE,
    BORROWED
}

class Book {
    private final String isbn;
    private final String title;
    private final String author;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

class BookCopy {
    private final String copyId;
    private final Book book;
    private BookCopyStatus status;

    public BookCopy(String copyId, Book book) {
        this.copyId = copyId;
        this.book = book;
        this.status = BookCopyStatus.AVAILABLE;
    }

    public String getCopyId() {
        return copyId;
    }

    public Book getBook() {
        return book;
    }

    public BookCopyStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == BookCopyStatus.AVAILABLE;
    }

    public void borrow() {
        status = BookCopyStatus.BORROWED;
    }

    public void makeAvailable() {
        status = BookCopyStatus.AVAILABLE;
    }
}

class User {
    private final String userId;
    private final String name;
    private final Role role;
    private final List<BookCopy> borrowedBooks = new ArrayList<>();

    public User(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public List<BookCopy> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(BookCopy copy) {
        borrowedBooks.add(copy);
    }

    public void returnBook(BookCopy copy) {
        borrowedBooks.remove(copy);
    }
}

class Transaction {
    private final String transactionId;
    private final User user;
    private final BookCopy bookCopy;
    private final TransactionType type;
    private final LocalDateTime timestamp;

    public Transaction(String transactionId, User user, BookCopy bookCopy, TransactionType type) {
        this.transactionId = transactionId;
        this.user = user;
        this.bookCopy = bookCopy;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public User getUser() {
        return user;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

class Catalog {
    private final Map<String, List<BookCopy>> inventory = new HashMap<>();

    public void addBookCopy(BookCopy copy) {
        inventory.computeIfAbsent(copy.getBook().getIsbn(), k -> new ArrayList<>()).add(copy);
    }

    public List<BookCopy> searchByIsbn(String isbn) {
        return inventory.getOrDefault(isbn, Collections.emptyList());
    }

    public BookCopy getAvailableCopy(String isbn) {
        for (BookCopy copy : searchByIsbn(isbn)) {
            if (copy.isAvailable()) {
                return copy;
            }
        }
        return null;
    }
}

class Library {
    private final Catalog catalog = new Catalog();
    private final Map<String, User> users = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void addBookCopy(User librarian, BookCopy copy) {
        if (librarian.getRole() != Role.LIBRARIAN) {
            throw new IllegalStateException("Only librarian can add books");
        }
        catalog.addBookCopy(copy);
    }

    public void borrowBook(String userId, String isbn) {
        User user = users.get(userId);
        if (user == null || user.getRole() != Role.MEMBER) {
            throw new IllegalStateException("Invalid member");
        }

        BookCopy copy = catalog.getAvailableCopy(isbn);
        if (copy == null) {
            throw new IllegalStateException("Book unavailable");
        }

        copy.borrow();
        user.borrowBook(copy);

        transactions.add(
                new Transaction(
                        UUID.randomUUID().toString(),
                        user,
                        copy,
                        TransactionType.BORROW
                )
        );
    }

    public void returnBook(String userId, String copyId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalStateException("Invalid member");
        }

        BookCopy target = null;

        for (BookCopy copy : user.getBorrowedBooks()) {
            if (copy.getCopyId().equals(copyId)) {
                target = copy;
                break;
            }
        }

        if (target == null) {
            throw new IllegalStateException("Book not borrowed by user");
        }

        target.makeAvailable();
        user.returnBook(target);

        transactions.add(
                new Transaction(
                        UUID.randomUUID().toString(),
                        user,
                        target,
                        TransactionType.RETURN
                )
        );
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        User librarian = new User("L1", "Alice", Role.LIBRARIAN);
        User member = new User("M1", "Bob", Role.MEMBER);

        library.registerUser(librarian);
        library.registerUser(member);

        Book book = new Book("ISBN001", "Clean Code", "Robert Martin");

        library.addBookCopy(librarian, new BookCopy("C1", book));
        library.addBookCopy(librarian, new BookCopy("C2", book));

        library.borrowBook("M1", "ISBN001");
        library.returnBook("M1", "C1");
    }
}