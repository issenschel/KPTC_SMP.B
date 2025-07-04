package com.example.kptc_smp.controller;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.image.ImageResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupResponseDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.service.main.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "400", description = "Неверно заполнены данные | поля", content = {@Content(mediaType = "application/json")})
@RequestMapping("/news")
@Validated
@Tag(name = "News")
public class NewsController {
    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создание новости", description = "ВСЕ ПОЛЯ FORM DATA")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость добавлена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")})
    })
    public NewsResponseDto createNews(
            @Valid @ModelAttribute @ParameterObject NewsRequestDto newsRequestDto,
            @RequestPart(value = "image") MultipartFile image) {
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
    public NewsResponseDto updateNews(@PathVariable @Min(value = 1, message = "Номер новости должен быть не меньше 1")
                                      @Max(value = 1000000, message = "Номер новости должен быть не больше 1000000") int newsId, @Valid @RequestBody NewsRequestDto newsRequestDto) {
        return newsService.updateNews(newsRequestDto, newsId);
    }

    @PutMapping(consumes = "multipart/*", value = "/{newsId}/preview")
    @Operation(summary = "Изменение превью новости")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новость изменена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Новость не найдена", content = {@Content(mediaType = "application/json")})
    })
    public ImageResponseDto updateNewsPreview(@PathVariable @Min(value = 1, message = "Номер новости должен быть не меньше 1")
                                              @Max(value = 1000000, message = "Номер новости должен быть не больше 1000000") int newsId, @RequestParam(value = "image") MultipartFile image) {
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
    public ResponseDto deleteNews(@PathVariable @Min(value = 1, message = "Номер новости должен быть не меньше 1")
                                  @Max(value = 1000000, message = "Номер новости должен быть не больше 1000000") int newsId) {
        return newsService.deleteNews(newsId);
    }

    @GetMapping()
    @Operation(summary = "Получение списка новостей")
    @ApiResponse(responseCode = "200", description = "Список новостей получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = HeadlineNewsGroupResponseDto.class))})
    public HeadlineNewsGroupResponseDto getHeadlineNews(@RequestParam(name = "page", defaultValue = "1")
                                                        @Min(value = 1, message = "Номер страницы новостей должен быть не меньше 1")
                                                        @Max(value = 1000000, message = "Номер страницы новостей должен быть не больше 1000000") int page) {
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
    public NewsResponseDto getNews(@PathVariable @Min(value = 1, message = "Номер новости должен быть не меньше 1")
                                   @Max(value = 1000000, message = "Номер новости должен быть не больше 1000000") int newsId) {
        return newsService.getNews(newsId);
    }


}
