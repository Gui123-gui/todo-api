package com.guilhermedev.todolist.unittest.mapper;

import com.guilhermedev.todolist.dto.user.UserResponseDTO;
import com.guilhermedev.todolist.mapper.ObjectMapper;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.unittest.mapper.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperTest {

    MockUser input;

    @BeforeEach
    void setUp() {
        input = new MockUser();
    }

    @Test
    void parseEntityToDTOTest() {
        User entity = input.mockEntity();
        UserResponseDTO dto = ObjectMapper.parseObject(entity, UserResponseDTO.class);

        assertEquals(Long.valueOf(0L), dto.getId());
        assertEquals("Name0", dto.getName());
        assertEquals("email0@email.com", dto.getEmail());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void parseDTOToEntityTest() {
        UserResponseDTO dto = input.mockDTO();
        User entity = ObjectMapper.parseObject(dto, User.class);

        assertEquals(Long.valueOf(0L), entity.getId());
        assertEquals("Name0", entity.getName());
        assertEquals("email0@email.com", entity.getEmail());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void parseEntityListToDTOListTest() {
        List<User> entities = input.mockEntityList();
        List<UserResponseDTO> dtos = ObjectMapper.parseListObjects(entities, UserResponseDTO.class);

        assertEquals(14, dtos.size());
        assertEquals("Name5", dtos.get(5).getName());
        assertEquals("email13@email.com", dtos.get(13).getEmail());
    }

    @Test
    void parseDTOListToEntityListTest() {
        List<UserResponseDTO> dtos = input.mockDTOList();
        List<User> entities = ObjectMapper.parseListObjects(dtos, User.class);

        assertEquals(14, entities.size());
        assertEquals("Name5", entities.get(5).getName());
        assertEquals("email13@email.com", entities.get(13).getEmail());
    }
}