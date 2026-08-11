package com.guilhermedev.todolist.exception.auth;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando um token JWT é inválido.
 */
public class InvalidTokenException extends GlobalException {

    public InvalidTokenException() {
        super("INVALID_TOKEN", "The provided token is invalid");
    }

    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message);
    }
}
