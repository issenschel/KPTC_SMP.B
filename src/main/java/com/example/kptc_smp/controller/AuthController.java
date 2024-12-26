package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.auth.AuthTokenDto;
import com.example.kptc_smp.dto.auth.JwtRequestDto;
import com.example.kptc_smp.dto.profile.UserInformationDto;
import com.example.kptc_smp.dto.registration.EmailDto;
import com.example.kptc_smp.dto.registration.RegistrationUserDto;
import com.example.kptc_smp.service.AuthService;
import com.example.kptc_smp.service.EmailService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth")
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/auth")
    @Operation(summary = "Авторизация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токен получен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenDto.class))}),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {@Content(mediaType = "application/json")})
    })
    public AuthTokenDto createAuthToken(@Valid @RequestBody JwtRequestDto authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformationDto.class))}),
            @ApiResponse(responseCode = "409", description = "Данные уже заняты", content = {@Content(mediaType = "application/json")})
    })
    public UserInformationDto createNewUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/sendCode")
    @Operation(summary = "Отправка кода по почте")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Почта уже занята", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto sendCode(@Valid @RequestBody EmailDto emailDto) {
        return emailService.sendRegistrationCode(emailDto);
    }
}