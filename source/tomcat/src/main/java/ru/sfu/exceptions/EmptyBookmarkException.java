package ru.sfu.exceptions;

public class EmptyBookmarkException extends Exception {
    public EmptyBookmarkException() {
        super("Cannot save empty bookmark.");
    }
}
