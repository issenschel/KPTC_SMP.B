package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.*;
import com.example.kptc_smp.exception.EmailException;
import com.example.kptc_smp.exception.profile.CodeValidationException;
import com.example.kptc_smp.exception.profile.PasswordValidationException;
import com.example.kptc_smp.exception.profile.PhotoException;
import com.example.kptc_smp.exception.profile.UserFoundException;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Неверный логин или пароль"));
    }

    @ExceptionHandler(RegistrationValidationException.class)
    public ResponseEntity<?> registrationValidationException(RegistrationValidationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getValidationErrors());
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<?> emailException(EmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> orderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PhotoException.class)
    public ResponseEntity<?> photoException(PhotoException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(CodeValidationException.class)
    public ResponseEntity<?> codeValidationException(CodeValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<?> passwordValidationException(PasswordValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<?> userFoundException(UserFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

}
