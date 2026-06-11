package com.guilhermedev.todolist.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String color;
    private Long userId;
}