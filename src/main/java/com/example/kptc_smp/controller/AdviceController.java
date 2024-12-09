package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.ChangeEmailException;
import com.example.kptc_smp.exception.OrderNotFoundException;
import com.example.kptc_smp.exception.RegistrationValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto> authException() {
        return ResponseEntity.badRequest().body(new ResponseDto("Неверный логин или пароль"));
    }

    @ExceptionHandler(RegistrationValidationException.class)
    public ResponseEntity<?> validationException(RegistrationValidationException e) {
        return ResponseEntity.badRequest().body(e.getValidationErrors());
    }

    @ExceptionHandler(ChangeEmailException.class)
    public ResponseEntity<?> validationException(ChangeEmailException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> validationException(OrderNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
