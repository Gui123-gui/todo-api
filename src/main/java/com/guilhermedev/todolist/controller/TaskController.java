package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.model.Task;
import com.guilhermedev.todolist.repository.TaskRepository;
import com.guilhermedev.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<?> getTaskById(@RequestParam Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
