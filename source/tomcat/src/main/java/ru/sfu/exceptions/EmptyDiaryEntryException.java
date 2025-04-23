package ru.sfu.exceptions;

public class EmptyDiaryEntryException extends Exception {
    public EmptyDiaryEntryException() {
        super("Cannot save empty diary entry.");
    }
}
