package com.guilhermedev.todolist.exception.user;

import com.guilhermedev.todolist.exception.GlobalException;

public class UserAlreadyExistsException extends GlobalException {

    public UserAlreadyExistsException(String email) {
        super("USER_ALREADY_EXISTS", "User with email: " + email + " already exists");
    }
}
