package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getProfile(Long id){
        log.info("Getting profile for user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
    }

    public User updateUser(long id, User user){
        log.info("Updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        return userRepository.save(u);
    }

    public void changePassword(Long id, String newPassword){
        log.info("Changing password for user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    public void deleteUser(Long id){
        log.info("Deleting user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + id));
        userRepository.delete(u);
    }

    public User findById(Long userId){
        log.info("Finding user with id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
    }
}
