package com.tiqwab.example.domain.service;

public class InvalidCartOrderException extends RuntimeException {

    public InvalidCartOrderException(String message) {
        super(message);
    }

}
