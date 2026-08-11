package com.guilhermedev.todolist.exception.auth;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando as credenciais fornecidas são inválidas.
 */
public class InvalidCredentialsException extends GlobalException {

    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", "Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super("INVALID_CREDENTIALS", message);
    }
}
