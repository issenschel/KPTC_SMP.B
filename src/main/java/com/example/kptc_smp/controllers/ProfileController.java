package com.example.kptc_smp.controllers;

import com.example.kptc_smp.dto.LoginChangeDto;
import com.example.kptc_smp.dto.PasswordChangeDto;
import com.example.kptc_smp.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/changeLogin")
    public ResponseEntity<?> changeLogin(@Valid @RequestBody LoginChangeDto loginChangeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return profileService.changeLogin(loginChangeDto);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changeLogin(@Valid @RequestBody PasswordChangeDto passwordChangeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return profileService.changePassword(passwordChangeDto);
    }

//    @PostMapping("/changeEmail")
//    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
//        }
//        return profileService.changeEmail(emailChangeDto);
//    }

    @PostMapping("/changePhoto")
    public ResponseEntity<?> changePhoto(@RequestParam("file") MultipartFile photo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return profileService.changePhoto(photo);
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getData(){
        return profileService.getData();
    }

    @GetMapping("/photo")
    public ResponseEntity<?> getPhoto(){
        return profileService.getPhoto();
    }

}

