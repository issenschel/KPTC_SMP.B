package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.JwtResponseDto;
import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.auth.PasswordResetDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.service.main.AuthService;
import com.example.kptc_smp.service.main.EmailService;
import com.example.kptc_smp.service.main.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    @Operation(summary = "Авторизация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токен получен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = "application/json")})
    })
    public JwtResponseDto createAuthToken(@Valid @RequestBody JwtRequestDto authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformationDto.class))}),
            @ApiResponse(responseCode = "409", description = "Данные уже заняты", content = {@Content(mediaType = "application/json")})
    })
    public UserInformationDto registrationUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.registrationUser(registrationUserDto);
    }

    @PostMapping("/password-reset/request")
    public ResponseDto createPasswordResetLink(@RequestBody EmailDto emailDto){
        return passwordResetService.createPasswordResetLink(emailDto);
    }

    @PostMapping("/password-reset/confirm")
    public ResponseDto savePassword(@RequestParam("uuid") UUID linkUUID, @Valid @RequestBody PasswordResetDto passwordResetDto) {
        passwordResetService.changeUserPassword(linkUUID, passwordResetDto);
        return new ResponseDto("Пароль изменен");
    }

    @PostMapping("/registration/confirmation-code")
    @Operation(summary = "Отправка кода на почту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Почта уже занята", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto sendCode(@Valid @RequestBody EmailDto emailDto) {
        return emailService.sendRegistrationCode(emailDto);
    }
}