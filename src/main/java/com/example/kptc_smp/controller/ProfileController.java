package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.JwtTokenPairDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.profile.EmailChangeDto;
import com.example.kptc_smp.dto.profile.PasswordChangeDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsDto;
import com.example.kptc_smp.dto.profile.UserProfileDto;
import com.example.kptc_smp.service.main.user.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/account-details")
    @Operation(summary = "Получение данных об аккаунте пользователя в профиле")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDetailsDto.class))})
    public UserAccountDetailsDto getUserAccountDetails() {
        return profileService.getUserAccountDetails();
    }

    @GetMapping("/user-profile")
    @Operation(summary = "Получение логина и ссылки на аватарку для профиля")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))})
    public UserProfileDto getUserProfileInfo() {
        return profileService.getUserProfileInfo();
    }

    @PutMapping("/password")
    @Operation(summary = "Смена пароля пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль изменен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")}),
    })
    public JwtTokenPairDto changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return profileService.changePassword(passwordChangeDto);
    }

    @PutMapping("/email")
    @Operation(summary = "Смена почты пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Почта изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Почта занята", content = {@Content(mediaType = "application/json")}),
    })
    public JwtTokenPairDto changeEmail(@Valid @RequestBody EmailChangeDto emailChangeDto) {
        return profileService.changeEmail(emailChangeDto);
    }

    @PutMapping(path = "/image", consumes = "multipart/*")
    @Operation(summary = "Смена фотографии пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото изменено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "С фото что-то не так", content = {@Content(mediaType = "application/json")}),
    })
    public UserProfileDto changeImage(@RequestParam("image") MultipartFile image) {
        return profileService.changeImage(image);
    }
}

