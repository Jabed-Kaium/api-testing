package com.example.test.exception;

public class BatteryNotFoundException extends RuntimeException {
    public BatteryNotFoundException(String message) {
        super(message);
    }
}
