package ru.sfu.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
public class WebController {
    @GetMapping("/")
    public String helloWorld() {
        return "Hello world I'm on the web!";
    }
}
