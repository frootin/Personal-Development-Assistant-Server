package ru.sfu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNoMatchException extends Exception {
    public PasswordNoMatchException() {
        super("Password and repeat password don't match.");
    }
}
