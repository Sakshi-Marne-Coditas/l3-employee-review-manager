package com.l3ProblemStatement.exceptionHandling;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
