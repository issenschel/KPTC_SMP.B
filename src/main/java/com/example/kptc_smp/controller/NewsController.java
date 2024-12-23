package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "News")
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping
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
    public ResponseEntity<?> createNews(@Valid @ModelAttribute NewsRequestDto newsRequestDto,
                                        @RequestParam(value = "file", required = false) MultipartFile photo) {
        newsService.createNews(newsRequestDto,photo);
        return ResponseEntity.ok().build();
    }

    @PutMapping
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
                                        @Valid @ModelAttribute NewsRequestDto newsRequestDto,
                                        @RequestParam(value = "file", required = false) MultipartFile photo) {
        newsService.changeNews(newsRequestDto,photo,id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
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

    @DeleteMapping
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
