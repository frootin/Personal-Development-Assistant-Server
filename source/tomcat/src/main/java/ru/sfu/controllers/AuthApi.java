package ru.sfu.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.User;
import ru.sfu.db.services.AuthenticationService;
import ru.sfu.db.services.UserService;
import ru.sfu.exceptions.PasswordNoMatchException;
import ru.sfu.exceptions.UsernameAlreadyExistsException;
import ru.sfu.objects.*;

import javax.validation.Valid;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthenticationService authenticationService;

    //@Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    //@Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/register")
    public String helloWorld() {
        return "Hello world I'm on the register!";
    }
}
