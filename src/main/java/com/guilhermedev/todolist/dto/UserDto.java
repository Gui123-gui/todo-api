package com.guilhermedev.todolist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    private List<Task> tasks;

}
