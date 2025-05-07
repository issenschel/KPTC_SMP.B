package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.JwtTokenPairDto;
import com.example.kptc_smp.dto.RefreshTokenRequestDto;
import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.*;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.dto.profile.UserAccountDetailsDto;
import com.example.kptc_smp.service.main.auth.AuthService;
import com.example.kptc_smp.service.main.auth.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    @Operation(summary = "Авторизация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токен получен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = "application/json")})
    })
    public AuthResponseDto authenticate(@Valid @RequestBody JwtRequestDto authRequest) {
        return authService.authenticate(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDetailsDto.class))}),
            @ApiResponse(responseCode = "409", description = "Данные уже заняты", content = {@Content(mediaType = "application/json")})
    })
    public UserAccountDetailsDto registrationUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.registrationUser(registrationUserDto);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Обновление токена")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токены обновлены", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Недействительный refresh-токен", content = {@Content(mediaType = "application/json")})
    })
    public JwtTokenPairDto refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

    @PostMapping("/password-forgot")
    @Operation(summary = "Отправка ссылки для смены пароля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto createPasswordResetLink(@Valid @RequestBody EmailDto emailDto) {
        return passwordResetService.createPasswordResetLink(emailDto);
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Смена пароля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль изменён", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Неверный UUID | Время истекло", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto resetPassword(@RequestParam("uuid") UUID linkUUID, @Valid @RequestBody PasswordResetDto passwordResetDto) {
        return passwordResetService.resetPassword(linkUUID, passwordResetDto);
    }



}