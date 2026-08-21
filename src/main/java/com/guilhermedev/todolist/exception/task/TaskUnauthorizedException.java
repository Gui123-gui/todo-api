package com.guilhermedev.todolist.exception.task;

import com.guilhermedev.todolist.exception.GlobalException;

public class TaskUnauthorizedException extends GlobalException {

    public TaskUnauthorizedException(Long taskId, Long userId) {
        super("TASK_UNAUTHORIZED", "User with id: " + userId + " is not authorized to access task with id: " + taskId);
    }
}
