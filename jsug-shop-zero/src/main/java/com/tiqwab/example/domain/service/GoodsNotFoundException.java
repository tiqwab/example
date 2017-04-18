package com.tiqwab.example.domain.service;

public class GoodsNotFoundException extends RuntimeException {

    public GoodsNotFoundException(String message) {
        super(message);
    }

}
