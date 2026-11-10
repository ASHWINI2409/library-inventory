package com.library;

import com.library.exception.BookNotFoundException;
import com.library.exception.DuplicateBookException;
import com.library.exception.InvalidBookException;
import com.library.model.Book;
import com.library.service.BookService;

/**
 * Lightweight self-checking tests (no external library needed, so they run
 * anywhere with just a JDK). Each check prints PASS/FAIL; the process exits
 * with code 1 if anything fails.
 */
public class BookServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testAddAndFind();
        testDuplicateRejected();
        testValidation();
        testUpdateKeepsOldValuesWhenNull();
        testUpdateIsAtomic();
        testDelete();
        testSearch();
        System.out.println("\nResult: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) System.exit(1);
    }

    private static void testAddAndFind() {
        BookService s = new BookService();
        s.addBook(new Book("978-0-13-468599-1", "Effective Java", "Joshua Bloch", 2018));
        check("add + find by ISBN (hyphens ignored)",
                s.findByIsbn("9780134685991").getTitle().equals("Effective Java"));
        check("count is 1", s.count() == 1);
    }

    private static void testDuplicateRejected() {
        BookService s = new BookService();
        s.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        check("duplicate ISBN rejected", throwsException(DuplicateBookException.class,
                () -> s.addBook(new Book("978-0-13-468599-1", "Other", "Someone", 2000))));
    }

    private static void testValidation() {
        check("blank title rejected", throwsException(InvalidBookException.class,
                () -> new Book("9780134685991", "  ", "A", 2000)));
        check("bad ISBN checksum rejected", throwsException(InvalidBookException.class,
                () -> new Book("9780134685992", "T", "A", 2000)));
        check("future year rejected", throwsException(InvalidBookException.class,
                () -> new Book("9780134685991", "T", "A", 3000)));
        check("year before printing rejected", throwsException(InvalidBookException.class,
                () -> new Book("9780134685991", "T", "A", 1200)));
        check("valid ISBN-10 with X accepted", new Book("080442957X", "T", "A", 1990).getIsbn().equals("080442957X"));
    }

    private static void testUpdateKeepsOldValuesWhenNull() {
        BookService s = new BookService();
        s.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        s.updateBook("9780134685991", null, null, 2019);
        Book b = s.findByIsbn("9780134685991");
        check("update changes only supplied field",
                b.getPublicationYear() == 2019 && b.getTitle().equals("Effective Java"));
    }

    private static void testUpdateIsAtomic() {
        BookService s = new BookService();
        s.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        throwsException(InvalidBookException.class, () -> s.updateBook("9780134685991", "New Title", null, 1000));
        check("failed update leaves book untouched",
                s.findByIsbn("9780134685991").getTitle().equals("Effective Java"));
    }

    private static void testDelete() {
        BookService s = new BookService();
        s.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        s.deleteBook("9780134685991");
        check("deleted book is gone", s.count() == 0);
        check("deleting again -> not found", throwsException(BookNotFoundException.class,
                () -> s.deleteBook("9780134685991")));
    }

    private static void testSearch() {
        BookService s = new BookService();
        s.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        s.addBook(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
        check("search by title, case-insensitive", s.search("JAVA").size() == 1);
        check("search by author", s.search("martin").size() == 1);
        check("search with no match", s.search("python").isEmpty());
    }

    // ----------------------------------------------------------- tiny helpers
    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (ok) passed++; else failed++;
    }

    private static boolean throwsException(Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
            return false;
        } catch (RuntimeException e) {
            return type.isInstance(e);
        }
    }
}
