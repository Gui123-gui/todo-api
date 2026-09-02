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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class, CategoryNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(GlobalException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getCode(), ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidCredentialsException.class, InvalidPasswordException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(GlobalException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getCode(), ex.getMessage(), request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(GlobalException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getCode(), ex.getMessage(), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidTokenException.class, TokenExpiredException.class})
    public ResponseEntity<ErrorResponse> handleTokenIssues(GlobalException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getCode(), ex.getMessage(), request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CategoryUnauthorizedException.class, TaskUnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(GlobalException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getCode(), ex.getMessage(), request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = Objects.nonNull(fieldError)
                ? String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage())
                : "Invalid input";
        return buildErrorResponse("INVALID_INPUT", message, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}: ", request.getRequestURI(), ex);
        return buildErrorResponse("INTERNAL_SERVER_ERROR", "An internal server error occurred.", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpServletRequest request, HttpStatus status) {
        var response = new ErrorResponse(code, message, LocalDateTime.now(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }
}
