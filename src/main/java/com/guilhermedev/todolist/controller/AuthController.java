package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.dto.login.*;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import com.guilhermedev.todolist.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService  authService;

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        var entity = authService.login(loginRequestDTO);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        User newUser = new User();
        newUser.setPassword(request.getPassword());
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponseDTO(newUser.getName(), newUser.getEmail()));
    }
}
