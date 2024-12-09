package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.*;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.LoginChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.entity.User;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final AssumptionService assumptionService;

    @Value("${upload.path}")
    private String uploadPath;

    public StringResponseDto changeLogin(LoginChangeDto loginChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    boolean isUsernameAvailable = userService.findByUsername(loginChangeDto.getNewUsername()).isEmpty();
                    boolean isPasswordValid = passwordEncoder.matches(loginChangeDto.getPassword(), us.getPassword());
                    if (isUsernameAvailable && isPasswordValid) {
                        us.setUsername(loginChangeDto.getNewUsername());
                        us.getTokenVersion().setVersion(UUID.randomUUID().toString());
                        userService.saveUser(us);
                        UserDetails userDetails = userService.loadUserByUsername(us.getUsername());
                        String versionId = us.getTokenVersion().getVersion();
                        String token = jwtTokenUtils.generateToken(userDetails, versionId);
                        return new StringResponseDto(token,HttpStatus.OK);
                    } else {
                        return new StringResponseDto(isUsernameAvailable ? "Неверный пароль" : "Логин уже занят", HttpStatus.CONFLICT);
                    }
                }).orElseGet(() -> new StringResponseDto("Пользователь не найден",HttpStatus.NOT_FOUND));
    }

    public StringResponseDto changePassword(PasswordChangeDto passwordChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> {
                    boolean isPasswordEqual = passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword());
                    boolean isPasswordValid = passwordEncoder.matches(passwordChangeDto.getOldPassword(), us.getPassword());
                    if (isPasswordEqual && isPasswordValid) {
                        us.setPassword(passwordEncoder.encode(passwordChangeDto.getPassword()));
                        us.getTokenVersion().setVersion(UUID.randomUUID().toString());
                        userService.saveUser(us);
                        UserDetails userDetails = userService.loadUserByUsername(us.getUsername());
                        String versionId = us.getTokenVersion().getVersion();
                        String token = jwtTokenUtils.generateToken(userDetails, versionId);
                        return new StringResponseDto(token,HttpStatus.OK);
                    } else {
                        return new StringResponseDto(isPasswordValid ? "Пароли не совпадают" : "Неверный пароль", HttpStatus.CONFLICT);
                    }
                }).orElseGet(() -> new StringResponseDto("Пользователь не найден",HttpStatus.NOT_FOUND));
    }

    public StringResponseDto changeEmail(EmailChangeDto emailChangeDto) {
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(us -> {
            boolean emailAvailable = userInformationService.findByEmail(emailChangeDto.getEmail()).isEmpty();
            boolean validateCode = assumptionService.validateCode(user.get().getUserInformation().getEmail(), emailChangeDto.getCode());
            if(emailAvailable && validateCode) {
                assumptionService.findByEmail(us.getUserInformation().getEmail()).ifPresent(assumptionService::delete);
                us.getUserInformation().setEmail(emailChangeDto.getEmail());
                us.getTokenVersion().setVersion(UUID.randomUUID().toString());
                userInformationService.save(us.getUserInformation());
                UserDetails userDetails = userService.loadUserByUsername(us.getUsername());
                String versionId = us.getTokenVersion().getVersion();
                String token = jwtTokenUtils.generateToken(userDetails, versionId);
                return new StringResponseDto(token,HttpStatus.OK);
            }else {
                return new StringResponseDto(emailAvailable ? "Неверный код" : "Новая почта уже занята", HttpStatus.CONFLICT);
            }
        }).orElseGet(() -> new StringResponseDto("Пользователь не найден",HttpStatus.NOT_FOUND));
    }

    public StringResponseDto changePhoto(MultipartFile photo) {
        if (photo != null && photo.getContentType() != null && !photo.getContentType().matches("image/.*")) {
            Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            String uuidFile = UUID.randomUUID().toString();
            String result = uuidFile + "." + photo.getOriginalFilename();
            return user.map(us -> {
                try {
                    if(us.getUserInformation().getPhoto() != null){
                        Files.delete(Path.of(uploadPath+"/"+us.getUserInformation().getPhoto()));
                    }
                    photo.transferTo(new File(uploadPath+"/"+ result));
                } catch (IOException e) {
                    return new StringResponseDto("Что-то пошло не так",HttpStatus.CONFLICT);
                }
                us.getUserInformation().setPhoto(result);
                userInformationService.save(us.getUserInformation());
                return new StringResponseDto("Успешное изменение фотографии",HttpStatus.OK);
            }).orElseGet(() -> new StringResponseDto("Пользователь не найден",HttpStatus.NOT_FOUND));
        }
        return new StringResponseDto("Неверный формат фотографии",HttpStatus.BAD_REQUEST);
    }

    public UserInformationDto getData(){
        Optional<User> user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.map(
                us -> new UserInformationDto(us.getId(), us.getUsername(), us.getUserInformation().getEmail(),
                        us.getUserInformation().getMinecraftName())
        ).orElse(null);
    }

    public String getPhoto(){
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(
                user -> user.getUserInformation().getPhoto()
        ).orElse( null);
    }
}
