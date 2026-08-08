package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.dto.task.TaskResponseDTO;
import com.guilhermedev.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.guilhermedev.todolist.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@AuthenticationPrincipal User user,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(taskService.getAllTasks(user.getId(), page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id,
                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTaskById(id, user.getId()));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO dto,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(dto, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id,
                                                      @RequestBody TaskRequestDTO dto,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}