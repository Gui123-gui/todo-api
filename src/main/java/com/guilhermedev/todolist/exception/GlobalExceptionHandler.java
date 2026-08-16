package com.guilhermedev.todolist.exception;

import com.guilhermedev.todolist.exception.auth.InvalidCredentialsException;
import com.guilhermedev.todolist.exception.auth.InvalidTokenException;
import com.guilhermedev.todolist.exception.auth.TokenExpiredException;
import com.guilhermedev.todolist.exception.category.CategoryNotFoundException;
import com.guilhermedev.todolist.exception.category.CategoryUnauthorizedException;
import com.guilhermedev.todolist.exception.task.TaskNotFoundException;
import com.guilhermedev.todolist.exception.task.TaskUnauthorizedException;
import com.guilhermedev.todolist.exception.user.InvalidPasswordException;
import com.guilhermedev.todolist.exception.user.UserAlreadyExistsException;
import com.guilhermedev.todolist.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {TaskNotFoundException.class, CategoryNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound
            (GlobalException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class, InvalidPasswordException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized
            (GlobalException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleAlreadyExists
            (GlobalException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {InvalidTokenException.class, TokenExpiredException.class})
    public ResponseEntity<ErrorResponse> handleTokenIssues
            (GlobalException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = {CategoryUnauthorizedException.class, TaskUnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleForbidden
            (GlobalException ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}: ", request.getRequestURI(), ex);

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An internal server error occurred.",
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
