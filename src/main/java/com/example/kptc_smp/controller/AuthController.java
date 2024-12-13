package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.registration.EmailDto;
import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.service.AuthService;
import com.example.kptc_smp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody JwtRequestDto authRequest) {
        return ResponseEntity.ok(authService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.ok(authService.createNewUser(registrationUserDto));
    }

    @PostMapping("/sendCode")
    public ResponseEntity<?> sendCode(@Valid @RequestBody EmailDto emailDto) {
        return ResponseEntity.ok().body(emailService.sendCode(emailDto));
    }
}
