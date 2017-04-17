package com.tiqwab.example.domain.service;

public class EmptyCartOrderException extends RuntimeException {

    public EmptyCartOrderException(String message) {
        super(message);
    }

}
