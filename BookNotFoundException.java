package com.library.exception;

/**
 * Thrown when an operation (update, delete, view) refers to an ISBN that is
 * not present in the inventory.
 */
public class BookNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BookNotFoundException(String isbn) {
        super("No book with ISBN " + isbn + " was found.");
    }
}
