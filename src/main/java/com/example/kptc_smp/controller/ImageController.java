package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ImageDto;
import com.example.kptc_smp.service.main.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "Image")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/resource")
    @Operation(summary = "Получение фото пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Фото не найдено", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<Resource> getImageAsResource(@RequestParam(name = "imageName") String imageName) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageService.getImage(imageName));
    }

    @GetMapping("/byte")
    @Operation(summary = "Получение фото пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Фото не найдено", content = {@Content(mediaType = "application/json")})
    })
    public ImageDto getImageAsBytes(@RequestParam(name = "imageName") String imageName) {
        return imageService.getImageAsBytes(imageName);
    }
}
