package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.TokenDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.service.main.EmailService;
import com.example.kptc_smp.service.main.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final EmailService emailService;

    @PutMapping("/password")
    @Operation(summary = "Смена пароля пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль изменен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")}),
    })
    public TokenDto changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return profileService.changePassword(passwordChangeDto);
    }

    @PutMapping("/email")
    @Operation(summary = "Смена почты пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Почта изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Почта занята", content = {@Content(mediaType = "application/json")}),
    })
    public TokenDto changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        return profileService.changeEmail(emailChangeDto);
    }

    @PutMapping(path = "/image", consumes = "multipart/*")
    @Operation(summary = "Смена фотографии пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото изменено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "С фото что-то не так", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseDto changeImage(@RequestParam("image") MultipartFile image) {
        return profileService.changeImage(image);
    }

    @GetMapping("/information")
    @Operation(summary = "Получение данных пользователя")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformationDto.class))})
    public UserInformationDto getData() {
        return profileService.getData();
    }

    @GetMapping("/image-name")
    @Operation(summary = "Получение названия изображения пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Фото не найдено", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto getImageName() {
        return profileService.getImageName();
    }

    @PostMapping("/email/confirmation-code")
    @Operation(summary = "Отправка кода на текущую почту")
    @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))})
    public ResponseDto sendChangeEmailCode() {
        return emailService.sendChangeEmailCode();
    }

}

