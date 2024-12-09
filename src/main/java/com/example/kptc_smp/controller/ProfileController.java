package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.*;
import com.example.kptc_smp.dto.auth.JwtResponseDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.LoginChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/changeLogin")
    public ResponseEntity<?> changeLogin(@Valid @RequestBody LoginChangeDto loginChangeDto) {
        StringResponseDto responseDto = profileService.changeLogin(loginChangeDto);
        return ResponseEntity.status(responseDto.getStatus()).body(new JwtResponseDto(responseDto.getMessage()));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changeLogin(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        StringResponseDto responseDto = profileService.changePassword(passwordChangeDto);
        return ResponseEntity.status(responseDto.getStatus()).body(new JwtResponseDto(responseDto.getMessage()));
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        StringResponseDto responseDto = profileService.changeEmail(emailChangeDto);
        return ResponseEntity.status(responseDto.getStatus()).body(new JwtResponseDto(responseDto.getMessage()));
    }

    @PostMapping("/changePhoto")
    public ResponseEntity<?> changePhoto(@RequestParam("file") MultipartFile photo) {
        StringResponseDto responseDto = profileService.changePhoto(photo);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto.getMessage());
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getData(){
        UserInformationDto userInformationDto = profileService.getData();
        if (userInformationDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
        return ResponseEntity.ok().body(userInformationDto);
    }

    @GetMapping("/photo")
    public ResponseEntity<?> getPhoto(){
        String path = profileService.getPhoto();
        if (path == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
        return ResponseEntity.ok().body(path);
    }

}

