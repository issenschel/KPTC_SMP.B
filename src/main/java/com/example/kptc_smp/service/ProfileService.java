package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.EmailChangeDto;
import com.example.kptc_smp.dto.JwtResponseDto;
import com.example.kptc_smp.dto.LoginChangeDto;
import com.example.kptc_smp.dto.PasswordChangeDto;
import com.example.kptc_smp.entitys.User;
import com.example.kptc_smp.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final UserInformationService userInformationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final EmailService emailService;
    private final AssumptionService assumptionService;

    public ResponseEntity<?> changeLogin(LoginChangeDto loginChangeDto) {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    boolean isUsernameAvailable = userService.findByUsername(loginChangeDto.getNewUsername()).isEmpty();
                    boolean isPasswordValid = passwordEncoder.matches(loginChangeDto.getPassword(), user.getPassword());
                    if (isUsernameAvailable && isPasswordValid) {
                        user.setUsername(loginChangeDto.getNewUsername());
                        user.getUserInformation().setVersionId(UUID.randomUUID().toString());
                        userService.saveUser(user);
                        String token = jwtTokenUtils.generateToken(
                                userService.loadUserByUsername(user.getUsername())
                        );
                        return ResponseEntity.ok(new JwtResponseDto(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(isUsernameAvailable ? "Неверный пароль" : "Логин уже занят");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

    public ResponseEntity<?> changePassword(PasswordChangeDto passwordChangeDto) {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> {
                    boolean isPasswordEqual = passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword());
                    boolean isPasswordValid = passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword());
                    if (isPasswordEqual && isPasswordValid) {
                        user.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
                        userService.saveUser(user);
                        String token = jwtTokenUtils.generateToken(
                                userService.loadUserByUsername(user.getUsername())
                        );
                        return ResponseEntity.ok(new JwtResponseDto(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(isPasswordValid ? "Пароли не совпадают" : "Неверный пароль");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

    public ResponseEntity<?> changeEmail(EmailChangeDto emailChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (emailChangeDto.getCode().isEmpty() && user.isPresent()){
            emailService.sendCode(user.get().getUserInformation().getEmail());
        }
        return user.map(u -> {
            boolean emailAvailable = userInformationService.findByEmail(emailChangeDto.getEmail()).isEmpty();
            boolean validateCode = emailService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode());
            if(emailAvailable && validateCode) {
                assumptionService.findByEmail(u.getUserInformation().getEmail()).ifPresent(assumptionService::delete);
                u.getUserInformation().setEmail(emailChangeDto.getEmail());
                u.getUserInformation().setVersionId(UUID.randomUUID().toString());
                userInformationService.save(u.getUserInformation());
                String token = jwtTokenUtils.generateToken(
                        userService.loadUserByUsername(u.getUsername())
                );
                return ResponseEntity.ok(new JwtResponseDto(token));
            }else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(emailAvailable ? "Новая почта уже занята" : "Неверный код");
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

}
