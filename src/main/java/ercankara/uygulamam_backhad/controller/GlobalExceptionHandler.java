package ercankara.uygulamam_backhad.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());  // Hata mesajını döndür
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientLandSizeException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientLandSizeException(InsufficientLandSizeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());  // Özel hata mesajı döndür
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Bir hata oluştu. Lütfen tekrar deneyin.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Özel İstisna Sınıfı
    public static class InsufficientLandSizeException extends RuntimeException {
        public InsufficientLandSizeException(String message) {
            super(message);
        }
    }
}
