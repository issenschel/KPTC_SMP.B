package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.auth.PasswordResetDateExpiredException;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.guild.OrderNotFoundException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
import com.example.kptc_smp.exception.email.CodeValidationException;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.exception.profile.PasswordValidationException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.registration.RegistrationValidationException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

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
    public ResponseEntity<Map<String, String>> registrationValidationException(RegistrationValidationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getValidationErrors());
    }

    @ExceptionHandler(EmailFoundException.class)
    public ResponseEntity<ResponseDto> emailException(EmailFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ResponseDto> orderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ResponseDto> photoException(ImageException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(CodeValidationException.class)
    public ResponseEntity<ResponseDto> codeValidationException(CodeValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ResponseDto> passwordValidationException(PasswordValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDto> multipartException(MultipartException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ResponseDto> newsNotFoundException(NewsNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ResponseDto> imageNotFoundException(ImageNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordResetDateExpiredException.class)
    public ResponseEntity<ResponseDto> passwordResetDateExpired(PasswordResetDateExpiredException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ResponseDto("Метод не поддерживается: " + ex.getMethod()));
    }

}