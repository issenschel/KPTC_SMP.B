package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.auth.JwtTokenPairResponseDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.image.ImageResponseDto;
import com.example.kptc_smp.dto.profile.*;
import com.example.kptc_smp.service.main.user.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDetailsResponseDto.class))})
    public UserAccountDetailsResponseDto getUserAccountDetails() {
        return profileService.getUserAccountDetails();
    }

    @GetMapping("/user-profile")
    @Operation(summary = "Получение данных об аккаунте пользователя для главной")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDto.class))})
    public UserProfileResponseDto getUserProfileInfo() {
        return profileService.getUserProfileInfo();
    }

    @GetMapping("/user-sessions")
    @Operation(summary = "Получение всех сессий пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SessionDataResponseDto.class))})
    public List<SessionDataResponseDto> getUserSessions() {
        return profileService.getAllSession();
    }

    @DeleteMapping("/user-session/{userSessionId}")
    @Operation(summary = "Удаление конкретной сессии пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDto.class))})
    public ResponseDto deleteSession(@PathVariable UUID userSessionId) {
        return profileService.deleteSession(userSessionId);
    }

    @DeleteMapping("/user-sessions")
    @Operation(summary = "Удаление всех сессий пользователя кроме текущей")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponseDto.class))})
    public ResponseDto deleteAllSessionsExceptCurrentByUser() {
        return profileService.deleteAllSessionsExceptCurrentByUser();
    }

    @PutMapping("/password")
    @Operation(summary = "Смена пароля пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль изменен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")}),
    })
    public JwtTokenPairResponseDto changePassword(@Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {
        return profileService.changePassword(passwordChangeRequestDto);
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
    public JwtTokenPairResponseDto changeEmail(@Valid @RequestBody EmailChangeRequestDto emailChangeRequestDto) {
        return profileService.changeEmail(emailChangeRequestDto);
    }

    @PutMapping(path = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Смена фотографии пользователя")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото изменено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "С фото что-то не так", content = {@Content(mediaType = "application/json")}),
    })
    public ImageResponseDto changeImage(@RequestParam("image") MultipartFile image) {
        return profileService.changeImage(image);
    }
}

