package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.ListHeadlineNewsDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.service.main.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость добавлена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
    })
    public News createNews(@Valid @ModelAttribute NewsRequestDto newsRequestDto,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        return newsService.createNews(newsRequestDto, image);
    }

    @PutMapping(consumes = "multipart/*")
    @Operation(summary = "Изменение новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public News changeNews(@RequestParam(name = "id") int id,
                           @Valid @ModelAttribute NewsRequestDto newsRequestDto,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        return newsService.changeNews(newsRequestDto, image, id);
    }

    @DeleteMapping
    @Operation(summary = "Удаление новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость удалена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public ResponseDto deleteNews(@RequestParam(name = "id") int id) {
        return newsService.deleteNews(id);
    }

    @GetMapping()
    @Operation(summary = "Получение списка новостей")
    @ApiResponse(responseCode = "200", description = "Список новостей получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ListHeadlineNewsDto.class))})
    public ListHeadlineNewsDto getHeadlineNews(@RequestParam(name = "page") int page) {
        return newsService.getHeadlineNews(page);
    }

    @GetMapping("/{newsId}")
    @Operation(summary = "Получение списка новостей")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новости получены", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))}),
    })
    public News getNews(@PathVariable int newsId) {
        return newsService.getNews(newsId);
    }
}
