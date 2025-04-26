package com.example.kptc_smp.controller;

import com.example.kptc_smp.enums.ImageType;
import com.example.kptc_smp.service.main.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/profile/{filename:.+}")
    @Operation(summary = "Получение фото профиля по ссылке")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")
    })
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) {
        Resource resource = imageService.getImageAsResource(ImageType.PROFILE, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/news/{filename:.+}")
    @Operation(summary = "Получение фото новости по ссылке")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")
    })
    public ResponseEntity<Resource> getNewsImage(@PathVariable String filename) {
        Resource resource = imageService.getImageAsResource(ImageType.NEWS, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
