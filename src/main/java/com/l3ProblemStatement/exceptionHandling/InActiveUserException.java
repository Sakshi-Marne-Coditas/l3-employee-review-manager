package com.l3ProblemStatement.exceptionHandling;

public class InActiveUserException extends RuntimeException{
    public InActiveUserException(String s) {
        super(s);
    }
}
