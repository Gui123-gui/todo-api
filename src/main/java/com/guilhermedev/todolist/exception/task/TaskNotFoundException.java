package com.guilhermedev.todolist.exception.task;

import com.guilhermedev.todolist.exception.GlobalException;

public class TaskNotFoundException extends GlobalException {

    public TaskNotFoundException(Long taskId) {
        super("TASK_NOT_FOUND", "No task found with id: " + taskId);
    }

    public TaskNotFoundException(Long taskId, Long userId) {
        super("TASK_NOT_FOUND", "No task found with id: " + taskId + " for user with id: " + userId);
    }
}
