package com.guilhermedev.todolist.exception.auth;

import com.guilhermedev.todolist.exception.GlobalException;

/**
 * Exception lançada quando um token JWT está expirado.
 */
public class TokenExpiredException extends GlobalException {

    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "The provided token has expired");
    }

    public TokenExpiredException(String message) {
        super("TOKEN_EXPIRED", message);
    }
}
