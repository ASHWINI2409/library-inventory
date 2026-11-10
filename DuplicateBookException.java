package com.library.exception;

/**
 * Thrown when someone tries to add a book whose ISBN is already in the
 * inventory. The ISBN is our unique identifier, so duplicates are not allowed.
 */
public class DuplicateBookException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateBookException(String isbn) {
        super("A book with ISBN " + isbn + " already exists in the inventory.");
    }
}
