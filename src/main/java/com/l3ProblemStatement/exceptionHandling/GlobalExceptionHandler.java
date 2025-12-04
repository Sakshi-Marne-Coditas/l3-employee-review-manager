package com.l3ProblemStatement.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistException(EmailAlreadyExistException ex){
        ApiError apiError= new ApiError( ex.getMessage(), HttpStatus.CONFLICT );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InActiveUserException.class)
    public ResponseEntity<ApiError> handleInActiveUserException(InActiveUserException ex){
        ApiError apiError= new ApiError( ex.getMessage(), HttpStatus.CONFLICT );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
