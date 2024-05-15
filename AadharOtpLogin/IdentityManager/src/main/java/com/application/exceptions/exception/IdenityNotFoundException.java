package com.application.exceptions.exception;

public class IdenityNotFoundException extends RuntimeException{
    public IdenityNotFoundException(String message) {
        super(message);
    }
}