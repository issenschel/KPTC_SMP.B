package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile")
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("/changePassword")
    @Operation(summary = "Смена пароля пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль изменен",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Старый и новый пароль не совпадают | Новый пароль и подтверждение пароля не совпадают",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return ResponseEntity.ok().body(profileService.changePassword(passwordChangeDto));
    }

    @PutMapping("/changeEmail")
    @Operation(summary = "Смена почты пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Почта изменена",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверно заполнены поля | Неверный код",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Почта занята",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        return ResponseEntity.ok().body(profileService.changeEmail(emailChangeDto));
    }

    @PutMapping("/changePhoto")
    @Operation(summary = "Смена фотографии пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Фото отправлено",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Что-то не так с фото",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> changePhoto(@RequestParam("file") MultipartFile photo) {
        return ResponseEntity.ok().body(profileService.changePhoto(photo));
    }

    @GetMapping("/information")
    @Operation(summary = "Получение данных пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные получены",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> getData() {
        return ResponseEntity.ok().body(profileService.getData());
    }

    @GetMapping("/photo")
    @Operation(summary = "Получение фото пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Фото получено",
                    content = {
                            @Content(mediaType = "multipart/form-data")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> getPhoto() {
        return ResponseEntity.ok().body(profileService.getPhoto());
    }

}

