package com.guilhermedev.todolist.service;

import com.guilhermedev.todolist.model.Task;
import com.guilhermedev.todolist.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {


    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        log.info("Creating a new task with title: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public Task findById(Long id){
        log.info("Finding task with id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No task found with id: " + id));
    }

    public List<Task> findAll(){
        log.info("Finding all tasks");
        return taskRepository.findAll();
    }

    public Task updateTask(Task task) {
        log.info("Updating task with id: {}", task.getId());
        Task existingTask = findById(task.getId());
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCompleted(task.isCompleted());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        Task existingTask = findById(id);
        taskRepository.delete(existingTask);
    }
}
