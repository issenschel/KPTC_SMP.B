package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineGroupNewsDto;
import com.example.kptc_smp.dto.news.NewsDto;
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
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public News createNews(@Valid @ModelAttribute NewsDto newsDto,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        return newsService.createNews(newsDto, image);
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
                           @Valid @ModelAttribute NewsDto newsDto,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        return newsService.changeNews(newsDto, image, id);
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
            @Content(mediaType = "application/json", schema = @Schema(implementation = HeadlineGroupNewsDto.class))})
    public HeadlineGroupNewsDto getHeadlineNews(@RequestParam(name = "page") int page) {
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

    @GetMapping("/{newsId}/resource")
    @Operation(summary = "Получение изображение новости")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото отправлено", content = {@Content(mediaType = "image/jpeg")}),
            @ApiResponse(responseCode = "404", description = "Фото не найдено", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<Resource> getImageAsResource(@PathVariable int newsId, @RequestParam(name = "imageName") String imageName) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newsService.getImageAsResource(newsId,imageName));
    }
}
