package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.service.main.DataBackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backup")
@Tag(name = "Backup")
public class DataBackupController {
    private final DataBackupService dataBackupService;

    @PostMapping("/data")
    @Operation(summary = "Создание бэкапа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Бэкап создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Не удалось залить на диск | Ошибка архива", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto createBackupData() {
        dataBackupService.createBackupData();
        return new ResponseDto("Всё прошло успешно");
    }

    @PostMapping("/restore-data")
    @Operation(summary = "Восстановление бэкапа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Восстановление успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Бэкап не найден", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Ошибка архива", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto restoreBackupData(@RequestParam(name = "zipFileName") String zipFileName) {
        return dataBackupService.restoreBackupData(zipFileName);
    }

    @GetMapping("/download")
    @Operation(summary = "Скачивание бэкапа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Скачивание успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Бэкап не найден", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "Не удалось скачать архив", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseDto downloadBackupData(@RequestParam(name = "zipFileName") String imageName) {
        return dataBackupService.downloadBackupData(imageName);
    }

}
