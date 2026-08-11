package com.guilhermedev.todolist.exception.user;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando a senha fornecida é inválida.
 */
public class InvalidPasswordException extends GlobalException {

    public InvalidPasswordException() {
        super("INVALID_PASSWORD", "The provided password is invalid");
    }

    public InvalidPasswordException(String message) {
        super("INVALID_PASSWORD", message);
    }
}
