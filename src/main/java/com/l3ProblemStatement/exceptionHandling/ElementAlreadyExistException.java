package com.l3ProblemStatement.exceptionHandling;

public class ElementAlreadyExistException extends RuntimeException{
    public ElementAlreadyExistException(String s) {
        super(s);
    }
}
