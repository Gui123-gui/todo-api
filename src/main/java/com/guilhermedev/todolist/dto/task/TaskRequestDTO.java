package com.guilhermedev.todolist.dto.task;

import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "title is required")
    private String title;

    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private Long categoryId;
}