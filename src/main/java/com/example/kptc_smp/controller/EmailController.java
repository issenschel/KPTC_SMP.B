package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.email.EmailDto;
import com.example.kptc_smp.service.main.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/confirmation-code")
    @Operation(summary = "Отправка кода на почту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден | Время истекло", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto sendCode(@Valid @RequestBody EmailDto emailDto) {
        return emailService.sendRegistrationCode(emailDto);
    }

    @PostMapping("/confirmation-code/current")
    @Operation(summary = "Отправка кода на текущую почту")
    @ApiResponse(responseCode = "200", description = "Письмо отправлено", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))})
    public ResponseDto sendChangeEmailCode() {
        return emailService.sendChangeEmailCode();
    }
}
