package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Profile")
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = "application/json")})
        })

@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping("/password")
    @Operation(summary = "Смена пароля пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль изменен", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Старый и новый пароль не совпадают | Новый пароль и подтверждение пароля не совпадают",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return profileService.changePassword(passwordChangeDto);
    }

    @PutMapping("/email")
    @Operation(summary = "Смена почты пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Почта изменена", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены поля | Неверный код", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Почта занята", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        return profileService.changeEmail(emailChangeDto);
    }

    @PutMapping("/photo")
    @Operation(summary = "Смена фотографии пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Что-то не так с фото", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto changePhoto(@RequestParam("file") MultipartFile photo) {
        return profileService.changeImage(photo);
    }

    @GetMapping("/information")
    @Operation(summary = "Получение данных пользователя")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {@Content(mediaType = "application/json")})
    public UserInformationDto getData() {
        return profileService.getData();
    }

    @GetMapping("/photo")
    @Operation(summary = "Получение фото пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото получено", content = {@Content(mediaType = "multipart/form-data")}),
            @ApiResponse(responseCode = "400", description = "Что-то не так с фото", content = {@Content(mediaType = "application/json")})
    })
    public Resource getPhoto() {
        return profileService.getImage();
    }

}

