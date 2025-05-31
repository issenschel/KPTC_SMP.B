package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.email.CodeValidationException;
import com.example.kptc_smp.exception.password.IncorrectCurrentPasswordException;
import com.example.kptc_smp.exception.password.PasswordMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CodeValidationException.class)
    public ResponseEntity<ResponseDto> codeValidationException(CodeValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ResponseDto> passwordMismatchException(PasswordMismatchException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(IncorrectCurrentPasswordException.class)
    public ResponseEntity<ResponseDto> incorrectCurrentPasswordException(IncorrectCurrentPasswordException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
