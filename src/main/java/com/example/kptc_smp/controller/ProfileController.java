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
        return ResponseEntity.ok().body(profileService.changeLogin(loginChangeDto));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changeLogin(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return ResponseEntity.ok().body(profileService.changePassword(passwordChangeDto));
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        return ResponseEntity.ok().body(profileService.changeEmail(emailChangeDto));
    }

    @PostMapping("/changePhoto")
    public ResponseEntity<?> changePhoto(@RequestParam("file") MultipartFile photo) {
        return ResponseEntity.ok().body(profileService.changePhoto(photo));
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getData(){
        return ResponseEntity.ok().body(profileService.getData());
    }

    @GetMapping("/photo")
    public ResponseEntity<?> getPhoto(){
        return ResponseEntity.ok().body(profileService.getPhoto());
    }

}

