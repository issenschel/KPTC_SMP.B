package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageStorageService imageStorageService;

    @PostMapping()
    @Operation(summary = "Отправка фотографии")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото изменено", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "С фото что-то не так", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseEntity<ImageResponse> uploadTempImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageStorageService.uploadTempFile(file));
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Получение фотографии")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")
    })
    public ResponseEntity<Resource> getImage(@PathVariable UUID imageId) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageStorageService.getFileAsResource(imageId));
    }

}