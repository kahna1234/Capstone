package com.dev.ecommerce.userauthservice.controllers;

import com.dev.ecommerce.userauthservice.exceptions.UserNotRegisteredException;
import com.dev.ecommerce.userauthservice.exceptions.IncorrectPasswordException;
import com.dev.ecommerce.userauthservice.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<Map<String, String>> handleUserNotRegistered(UserNotRegisteredException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectPassword(IncorrectPasswordException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid password");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExist(UserAlreadyExistException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User already exists");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage() != null ? ex.getMessage() : "Unknown Error");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
