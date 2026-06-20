package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.config.SecurityConfig;
import com.guilhermedev.todolist.dto.login.LoginRequestDTO;
import com.guilhermedev.todolist.dto.login.LoginResponseDTO;
import com.guilhermedev.todolist.dto.user.UserRequestDTO;
import com.guilhermedev.todolist.dto.user.UserResponseDTO;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import static com.guilhermedev.todolist.mapper.ObjectMapper.parseObject;

import com.guilhermedev.todolist.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService{

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Cria o perfil do usuário
     */
    public UserResponseDTO createUser(UserRequestDTO  userRequestDTO) {
        var entity = parseObject(userRequestDTO, User.class);
        entity.setPassword(securityConfig.passwordEncoder().encode(userRequestDTO.getPassword()));
        return parseObject(userRepository.save(entity), UserResponseDTO.class);
    }

    /**
     * Verifica o email e senha
     * Gera o token de acesso
     */

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        var auth = authenticationManager.authenticate(userNamePassword);
        User authenticatedUser = (User) auth.getPrincipal();

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtUtil.generateToken(authenticatedUser.getId(), authenticatedUser.getEmail()));
        return response;
    }
}
