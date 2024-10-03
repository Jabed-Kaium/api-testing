package com.example.test.exceptionHandler;

import com.example.test.exception.BatteryNotFoundException;
import com.example.test.exception.InvalidBatteryListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BatteryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBatteryNotFoundException(BatteryNotFoundException ex){
        Map<String, String> response = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        response.put("timestamp", now.toString());
        response.put("status", "404");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex){
        Map<String, String> response = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        response.put("timestamp", now.toString());
        response.put("status", "400");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidBatteryListException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBatteryListException(InvalidBatteryListException ex){
        Map<String, Object> response = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        response.put("timestamp", now.toString());
        response.put("status", "400");
        response.put("message","Invalid input detected.");
        response.put("details","Please correct the invalid batteries and try again.");
        response.put("invalidBatteries", ex.getInvalidBatteryDtoList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
