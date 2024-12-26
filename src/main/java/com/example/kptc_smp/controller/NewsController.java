package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.news.ListNewsDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.entity.postgreSQL.News;
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
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "Создание новости")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Новость добавлена", content = {@Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
            })
    public News createNews(@Valid @ModelAttribute NewsRequestDto newsRequestDto,
                           @RequestParam(value = "file", required = false) MultipartFile photo) {
        return newsService.createNews(newsRequestDto,photo);
    }

    @PutMapping
    @Operation(summary = "Изменение новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость изменена", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public News changeNews(@RequestParam(name = "id") int id,
                           @Valid @ModelAttribute NewsRequestDto newsRequestDto,
                           @RequestParam(value = "file", required = false) MultipartFile photo) {
        return newsService.changeNews(newsRequestDto,photo,id);
    }

    @DeleteMapping
    @Operation(summary = "Удаление новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость удалена", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> deleteNews(@RequestParam(name = "id") int id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получение новостей")
    @ApiResponse(responseCode = "200", description = "Список новостей получен", content = {@Content(mediaType = "application/json")})
    public ListNewsDto getNews(@RequestParam(name = "page") int page) {
        return newsService.getNews(page);
    }
}
