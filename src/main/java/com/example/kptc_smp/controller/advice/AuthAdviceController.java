package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.auth.PasswordResetDateExpiredException;
import com.example.kptc_smp.exception.auth.PasswordResetUUIDNotFoundException;
import com.example.kptc_smp.exception.auth.RegistrationValidationException;
import com.example.kptc_smp.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class AuthAdviceController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto> authException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Неверный логин или пароль"));
    }

    @ExceptionHandler(RegistrationValidationException.class)
    public ResponseEntity<Map<String, String>> registrationValidationException(RegistrationValidationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getValidationErrors());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordResetDateExpiredException.class)
    public ResponseEntity<ResponseDto> passwordResetDateExpired(PasswordResetDateExpiredException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordResetUUIDNotFoundException.class)
    public ResponseEntity<ResponseDto> passwordResetUUIDNotFoundException(PasswordResetUUIDNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
