package com.guilhermedev.todolist.controller;

import com.guilhermedev.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // TODO: reescrever endpoints após implementar JWT + Spring Security
    // - getAllTasks() -> taskService.getAllTasks(userId)
    // - getTaskById(@PathVariable Long id) -> taskService.getTaskById(id, userId)
    // - createTask(@RequestBody TaskRequestDTO) -> taskService.createTask(dto, userId)
    // - updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO) -> taskService.updateTask(id, dto, userId)
    // - deleteTask(@PathVariable Long id) -> taskService.deleteTask(id, userId)
}