package ru.sfu.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.sfu.config.JwtTokenUtil;
import ru.sfu.db.models.User;
import ru.sfu.db.services.UserService;
import ru.sfu.exceptions.PasswordNoMatchException;
import ru.sfu.exceptions.UsernameAlreadyExistsException;
import ru.sfu.objects.AuthRequestDto;
import ru.sfu.objects.AuthResponseDto;
import ru.sfu.objects.RegisterUserDto;

import javax.validation.Valid;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthApi {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            AuthResponseDto response = new AuthResponseDto(accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //@PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticateUser(@Valid @RequestBody AuthRequestDto loginRequest) throws Exception {
        /**try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }*/
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        return ResponseEntity.ok().body(
                new AuthResponseDto(jwtUtil.generateAccessToken(userService.findByEmail(loginRequest.getEmail())))
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto registerUserDto) throws UsernameAlreadyExistsException, PasswordNoMatchException {
        final var registeredUser = userService.registerUser(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/register")
    public String helloWorld() {
        return "Hello world I'm on the register!";
    }
}
