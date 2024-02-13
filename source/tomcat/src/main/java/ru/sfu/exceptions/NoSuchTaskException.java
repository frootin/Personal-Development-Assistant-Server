package ru.sfu.exceptions;

import java.lang.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchTaskException extends Exception {
    public NoSuchTaskException() {
        super("No task with this id found.");
    }
}
