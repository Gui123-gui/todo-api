package com.guilhermedev.todolist.exception.user;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando tenta-se criar um usuário com um email já existente.
 */
public class UserAlreadyExistsException extends GlobalException {

    public UserAlreadyExistsException(String email) {
        super("USER_ALREADY_EXISTS", "User with email: " + email + " already exists");
    }
}
