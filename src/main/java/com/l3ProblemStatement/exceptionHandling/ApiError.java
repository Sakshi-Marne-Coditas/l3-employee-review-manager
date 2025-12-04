package com.l3ProblemStatement.exceptionHandling;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

@Data
public class ApiError {
    private LocalTime localTime;
    private String message;
    private int status;

    public ApiError( String message, HttpStatus status) {
        this.localTime = LocalTime.now();
        this.message = message;
        this.status = status.value();
    }
}