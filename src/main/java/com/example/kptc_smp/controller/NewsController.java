package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.news.NewsDto;
import com.example.kptc_smp.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/news")
    @Operation(summary = "Создание новости")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новость добавлена",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Вы не авторизованы",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    )
            })
    public ResponseEntity<?> createNews(@Valid @ModelAttribute NewsDto newsDto,
                                        @RequestParam(value = "file", required = false) MultipartFile photo) {
        newsService.createNews(newsDto,photo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/news")
    @Operation(summary = "Изменение новости")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новость изменена",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Вы не авторизованы",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Новость не найдена",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
            })
    public ResponseEntity<?> changeNews(@RequestParam(name = "id") int id,
                                        @Valid @ModelAttribute NewsDto newsDto,
                                        @RequestParam(value = "file", required = false) MultipartFile photo) {
        newsService.changeNews(newsDto,photo,id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/news")
    @Operation(summary = "Получение новостей")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список новостей получен",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные",
                            content = {
                                    @Content(mediaType = "application/json")
                            }
                    )
            })
    public ResponseEntity<?> getNews(@RequestParam(name = "page") int page) {
        return ResponseEntity.ok(newsService.getNews(page));
    }

    @DeleteMapping("/news")
    @Operation(summary = "Удаление новости")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Новость удалена",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Вы не авторизованы",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Новость не найдена",
                    content = {
                            @Content(mediaType = "application/json")
                    }
            )
    })
    public ResponseEntity<?> deleteNews(@RequestParam(name = "id") int id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

}
