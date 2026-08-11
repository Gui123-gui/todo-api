package com.guilhermedev.todolist.exception;

/**
 * Exception global da aplicação.
 * Classe base para todas as exceções de negócio.
 */
public class GlobalException extends RuntimeException {

    private final String code;

    public GlobalException(String message) {
        super(message);
        this.code = "INTERNAL_ERROR";
    }

    public GlobalException(String code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
