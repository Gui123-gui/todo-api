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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Cria o perfil do usuário
     */
    public UserResponseDTO createUser(UserRequestDTO  userRequestDTO) {
        var entity = parseObject(userRequestDTO, User.class);
        entity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return parseObject(userRepository.save(entity), UserResponseDTO.class);
    }

    /**
     * Verifica o email e senha
     * Gera o token de acesso
     */

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        var entity = parseObject(loginRequestDTO, User.class);
        User u = userRepository.findByEmail(entity.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));
        if (!passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                u.getPassword())) {

            throw new BadCredentialsException("Credenciais inválidas");
        }
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(jwtUtil.generateToken(u.getId(), u.getEmail()));
        return loginResponseDTO;
    }
}
