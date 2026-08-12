package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.exception.user.UserNotFoundException;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getProfile(Long id) {
        log.info("Getting profile for user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updateUser(Long id, User user) {
        log.info("Updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        return userRepository.save(u);
    }

    public void changePassword(Long id, String newPassword) {
        log.info("Changing password for user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(u);
    }

    public User findById(Long userId) {
        log.info("Finding user with id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}