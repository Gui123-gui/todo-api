package com.guilhermedev.todolist.unittest.mapper.mocks;

import com.guilhermedev.todolist.dto.user.UserResponseDTO;
import com.guilhermedev.todolist.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockUser {

    public User mockEntity() {
        return mockEntity(0);
    }

    public UserResponseDTO mockDTO() {
        return mockDTO(0);
    }

    public List<User> mockEntityList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            users.add(mockEntity(i));
        }
        return users;
    }

    public List<UserResponseDTO> mockDTOList() {
        List<UserResponseDTO> users = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            users.add(mockDTO(i));
        }
        return users;
    }

    public User mockEntity(Integer number) {
        User user = new User();
        user.setId(number.longValue());
        user.setName("Name" + number);
        user.setEmail("email" + number + "@email.com");
        user.setPassword("password" + number);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public UserResponseDTO mockDTO(Integer number) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(number.longValue());
        dto.setName("Name" + number);
        dto.setEmail("email" + number + "@email.com");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}