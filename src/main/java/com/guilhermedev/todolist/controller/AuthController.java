package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.login.RegisterDTO;
import com.guilhermedev.todolist.dto.login.RegisterResponseDTO;
import com.guilhermedev.todolist.repository.UserRepository;
import com.guilhermedev.todolist.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService  authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        var entity = authService.login(loginRequestDTO);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {

    }
}
