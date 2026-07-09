package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.dto.task.TaskRequestDTO;
import com.guilhermedev.todolist.model.Task;
import com.guilhermedev.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // TODO: reescrever endpoints após implementar JWT + Spring Security
    // - getAllTasks() -> taskService.getAllTasks(userId)
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }
    // - getTaskById(@PathVariable Long id) -> taskService.getTaskById(id, userId)
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id, userId));
    }
    // - createTask(@RequestBody TaskRequestDTO) -> taskService.createTask(dto, userId)
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto, userId));
    }
    // - updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO) -> taskService.updateTask(id, dto, userId)
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, userId));
    }
    // - deleteTask(@PathVariable Long id) -> taskService.deleteTask(id, userId)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}