package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.actionticket.ActionTicketExpireException;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
import com.example.kptc_smp.exception.guild.OrderNotFoundException;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.exception.role.RoleNotFoundException;
import com.example.kptc_smp.exception.user.ActiveSessionDeletionException;
import com.example.kptc_smp.exception.user.UserSessionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ResponseDto> orderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ResponseDto> newsNotFoundException(NewsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ActionTicketNotFoundException.class)
    public ResponseEntity<ResponseDto> actionTicketNotFoundException(ActionTicketNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ActionTicketExpireException.class)
    public ResponseEntity<ResponseDto> actionTicketExpireException(ActionTicketExpireException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ResponseDto> roleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(UserSessionNotFound.class)
    public ResponseEntity<ResponseDto> userSessionNotFound(UserSessionNotFound e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ActiveSessionDeletionException.class)
    public ResponseEntity<ResponseDto> activeSessionDeletionException(ActiveSessionDeletionException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }


}