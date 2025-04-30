package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.service.main.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/news")
@Tag(name = "News")
public class NewsController {
    private final NewsService newsService;

    @PostMapping(consumes = "multipart/*")
    @Operation(summary = "Создание новости")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость добавлена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
    })
    public NewsResponseDto createNews(@Valid @ModelAttribute NewsRequestDto newsRequestDto,
                           @RequestParam(value = "image") MultipartFile image) {
        return newsService.createNews(newsRequestDto, image);
    }

    @PutMapping(value = "/{newsId}")
    @Operation(summary = "Изменение новости")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public NewsResponseDto updateNews(@PathVariable @Min(1) int newsId,
                           @Valid @RequestBody NewsRequestDto newsRequestDto) {
        return newsService.updateNews(newsRequestDto, newsId);
    }

    @PutMapping(consumes = "multipart/*", value = "/{newsId}/preview")
    @Operation(summary = "Изменение новости")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public NewsResponseDto updateNewsPreview(@PathVariable @Min(1) int newsId, @RequestParam(value = "image") MultipartFile image) {
        return newsService.updateNewsPreview(image, newsId);
    }

    @DeleteMapping(value = "/{newsId}")
    @Operation(summary = "Удаление новости")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость удалена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto deleteNews(@PathVariable @Min(1) int newsId) {
        return newsService.deleteNews(newsId);
    }

    @GetMapping()
    @Operation(summary = "Получение списка новостей")
    @ApiResponse(responseCode = "200", description = "Список новостей получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = HeadlineNewsGroupDto.class))})
    public HeadlineNewsGroupDto getHeadlineNews(@RequestParam(name = "page", defaultValue = "1") @Min(1) int page) {
        return newsService.getHeadlineNews(page);
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "Получение конкретной новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новости получены", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
    })
    public NewsResponseDto getNews(@PathVariable @Min(1) int newsId) {
        return newsService.getNews(newsId);
    }


}
