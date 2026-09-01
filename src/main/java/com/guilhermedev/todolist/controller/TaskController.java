package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskResponseDTO;
import com.guilhermedev.todolist.enums.Priority;
import com.guilhermedev.todolist.enums.Status;
import com.guilhermedev.todolist.model.User;
import com.guilhermedev.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody TaskRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {
        TaskResponseDTO createdTask = taskService.createTask(requestDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TaskResponseDTO> tasks = taskService.getAllTasks(user.getId(), page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Status status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(user.getId(), status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByPriority(
            @AuthenticationPrincipal User user,
            @PathVariable Priority priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(user.getId(), priority));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTaskById(id, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(id, requestDTO, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> markAsCompleted(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.markAsCompleted(id, user.getId()));
    }
}