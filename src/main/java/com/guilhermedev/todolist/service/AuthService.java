package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.user.UserRequestDTO;
import com.guilhermedev.todolist.dto.user.UserResponseDTO;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponseDTO register(UserRequestDTO  requestDTO) {
        var entity = parseObject(requestDTO, User.class);
        entity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(entity);

        return parseObject(entity, UserResponseDTO.class);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(userNamePassword);

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        return response;
    }
}
