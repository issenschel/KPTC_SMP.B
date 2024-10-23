package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.*;
import com.example.kptc_smp.entitys.User;
import com.example.kptc_smp.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Value("${upload.path}")
    private String uploadPath;

    public ResponseEntity<?> changeLogin(LoginChangeDto loginChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                u -> {
                    boolean isUsernameAvailable = userService.findByUsername(loginChangeDto.getNewUsername()).isEmpty();
                    boolean isPasswordValid = passwordEncoder.matches(loginChangeDto.getPassword(), u.getPassword());
                    if (isUsernameAvailable && isPasswordValid) {
                        u.setUsername(loginChangeDto.getNewUsername());
                        u.getUserInformation().setVersionId(UUID.randomUUID().toString());
                        userService.saveUser(u);
                        UserDetails userDetails = userService.loadUserByUsername(u.getUsername());
                        String versionId = u.getUserInformation().getVersionId();
                        String token = jwtTokenUtils.generateToken(userDetails, versionId);
                        return ResponseEntity.ok(new JwtResponseDto(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(isUsernameAvailable ? "Неверный пароль" : "Логин уже занят");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

    public ResponseEntity<?> changePassword(PasswordChangeDto passwordChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                u -> {
                    boolean isPasswordEqual = passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword());
                    boolean isPasswordValid = passwordEncoder.matches(passwordChangeDto.getOldPassword(), u.getPassword());
                    if (isPasswordEqual && isPasswordValid) {
                        u.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
                        userService.saveUser(u);
                        UserDetails userDetails = userService.loadUserByUsername(u.getUsername());
                        String versionId = u.getUserInformation().getVersionId();
                        String token = jwtTokenUtils.generateToken(userDetails, versionId);
                        return ResponseEntity.ok(new JwtResponseDto(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(isPasswordValid ? "Пароли не совпадают" : "Неверный пароль");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

    public ResponseEntity<?> changeEmail(EmailChangeDto emailChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (emailChangeDto.getCode().isEmpty() && user.isPresent()){
            return emailService.sendCode(user.get().getUserInformation().getEmail());
        }
        return user.map(u -> {
            boolean emailAvailable = userInformationService.findByEmail(emailChangeDto.getEmail()).isEmpty();
            boolean validateCode = emailService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode());
            if(emailAvailable && validateCode) {
                assumptionService.findByEmail(u.getUserInformation().getEmail()).ifPresent(assumptionService::delete);
                u.getUserInformation().setEmail(emailChangeDto.getEmail());
                u.getUserInformation().setVersionId(UUID.randomUUID().toString());
                userInformationService.save(u.getUserInformation());
                UserDetails userDetails = userService.loadUserByUsername(u.getUsername());
                String versionId = u.getUserInformation().getVersionId();
                String token = jwtTokenUtils.generateToken(userDetails, versionId);
                return ResponseEntity.ok(new JwtResponseDto(token));
            }else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(emailAvailable ? "Неверный код" : "Новая почта уже занята");
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }

    public ResponseEntity<?> changePhoto(MultipartFile photo) {
        if (photo != null && !photo.getContentType().matches("image/.*")) {
            Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            String uuidFile = UUID.randomUUID().toString();
            String result = uuidFile + "." + photo.getOriginalFilename();
            return user.map(u -> {
                try {
                    if(u.getUserInformation().getPhoto() != null){
                        Files.delete(Path.of(uploadPath+"/"+u.getUserInformation().getPhoto()));
                    }
                    photo.transferTo(new File(uploadPath+"/"+ result));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                u.getUserInformation().setPhoto(result);
                userInformationService.save(u.getUserInformation());
                return ResponseEntity.ok().build();
            }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Неверный формат фотографии");
    }

    public ResponseEntity<?> getData(){
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> ResponseEntity.ok(new UserInformationDto(user.getId(), user.getUsername(), user.getUserInformation().getEmail(),
                        user.getUserInformation().getMinecraftName()))
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    public ResponseEntity<?> getPhoto(){
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> ResponseEntity.ok().body(user.getUserInformation().getPhoto())
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден"));
    }
}
