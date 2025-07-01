package com.example.VitaDigging.exception;

public class IncorrectLoginException extends RuntimeException {
    public IncorrectLoginException(String message) {
        super(message);
    }
}
