# Library Book Inventory Manager

A menu-driven, command-line Java application that manages a library's book
inventory with full CRUD (Create, Read, Update, Delete) operations. All data is
kept in memory using a `LinkedHashMap`, so it is cleared when the program exits.

## Features

| Menu option | What it does |
|-------------|--------------|
| 1. Add a new book | Creates a book (ISBN, title, author, publication year) |
| 2. List all books | Shows every book in an aligned table |
| 3. Search | Case-insensitive search by part of a title or author |
| 4. Update a book | Edits title, author and/or year; press Enter to keep a value |
| 5. Delete a book | Removes a book after a yes/no confirmation |
| 6. Load sample data | Adds four real books so you can try the program quickly |
| 0. Exit | Quits the program |

## Requirements

* JDK 17 or newer (developed and tested with JDK 21). Check with `java -version` and `javac -version`.
* No external libraries or build tools are needed.

## Project structure

```
library-inventory/
  README.md
  src/com/library/
    Main.java                      entry point, wires the objects together
    model/Book.java                the Book entity (with validation)
    service/BookService.java       CRUD logic + in-memory storage (LinkedHashMap)
    ui/ConsoleMenu.java            menu and screens
    util/InputReader.java          safe console input helpers
    util/IsbnValidator.java        ISBN-10 / ISBN-13 validation
    exception/                     InvalidBookException, DuplicateBookException, BookNotFoundException
  test/com/library/BookServiceTest.java   self-checking tests (no JUnit needed)
```

## How to compile

Run these commands from inside the `library-inventory` folder.

**Linux / macOS**
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

**Windows (Command Prompt)**
```bat
mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
```

## How to run

```bash
java -cp out com.library.Main
```

## How to run the tests (optional)

```bash
javac -cp out -d out test/com/library/BookServiceTest.java
java -cp out com.library.BookServiceTest
```
Expected result: `Result: 15 passed, 0 failed.`

## Using the program

1. Start the program and type `6` to load sample data, then `2` to see the list.
2. Use `1` to add your own book. ISBNs may be typed with or without hyphens
   (for example `978-0-13-468599-1`). Sample valid ISBNs: `9780321125217`, `080442957X`.
3. Use `4` to update a book by its ISBN. Press Enter at a prompt to keep the old value.
4. Use `5` to delete a book; answer `y` to confirm.

## Validation rules

* **ISBN:** must be a valid ISBN-10 or ISBN-13 (check digit verified) and unique in the inventory.
* **Title / Author:** cannot be blank and are limited to 150 characters.
* **Publication year:** a whole number between 1450 and the current year.
* Menu choices must be numbers from 0 to 6; invalid entries are re-asked, never a crash.

## Error handling

Custom exceptions (`InvalidBookException`, `DuplicateBookException`,
`BookNotFoundException`) are thrown by the model/service layers and caught in one
place (`ConsoleMenu.run`), which prints a friendly `[Error]` message and returns
to the menu. A failed update never leaves a book half-changed.

## Design notes

* **Layers:** UI (`ConsoleMenu`) -> service (`BookService`) -> model (`Book`). The service has no console code, so it is easy to test.
* **Why `LinkedHashMap`?** Fast lookup by ISBN, automatic uniqueness of keys, and books stay in insertion order.
* **Why is the ISBN not editable?** It is the book's identity. To correct it, delete and re-add the book.
