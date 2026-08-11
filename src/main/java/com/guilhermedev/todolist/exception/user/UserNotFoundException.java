package com.guilhermedev.todolist.exception.user;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando um usuário não é encontrado.
 */
public class UserNotFoundException extends GlobalException {

    public UserNotFoundException(Long userId) {
        super("USER_NOT_FOUND", "No user found with id: " + userId);
    }

    public UserNotFoundException(String email) {
        super("USER_NOT_FOUND", "No user found with email: " + email);
    }
}
