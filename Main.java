package com.library;

import com.library.service.BookService;
import com.library.ui.ConsoleMenu;
import com.library.util.InputReader;

import java.util.Scanner;

/**
 * Program entry point. It only creates the objects and connects them
 * (service -> menu); all real work happens in the other classes.
 */
public class Main {
    public static void main(String[] args) {
        // try-with-resources closes the Scanner automatically when we finish.
        try (Scanner scanner = new Scanner(System.in)) {
            BookService service = new BookService();
            InputReader reader = new InputReader(scanner);
            new ConsoleMenu(service, reader).run();
        }
    }
}
