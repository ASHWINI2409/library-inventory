package com.library.exception;

/**
 * Thrown when book data fails validation (blank title, malformed ISBN,
 * impossible publication year, and so on).
 *
 * It extends RuntimeException (unchecked) because invalid input is a
 * programming/user-input problem that the menu layer handles in one place,
 * so we avoid cluttering every method signature with "throws".
 */
public class InvalidBookException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidBookException(String message) {
        super(message);
    }
}
