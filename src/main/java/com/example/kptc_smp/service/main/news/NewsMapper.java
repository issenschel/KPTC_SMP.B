package com.example.kptc_smp.service.main.news;

import com.example.kptc_smp.dto.news.HeadlineNewsDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.enums.NewsImageRole;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsMapper {
    private final ImageStorageService imageStorageService;

    public NewsResponseDto toNewsResponseDto(News news) {
        return NewsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .datePublication(news.getDatePublication())
                .previewUrl(news.getImages().stream()
                        .filter(t -> NewsImageRole.PREVIEW.equals(t.getNewsImageRole()))
                        .findFirst()
                        .map(img -> imageStorageService.getImageUrl(img.getImageRegistry().getId()))
                        .orElse(null))
                .build();
    }

    public List<HeadlineNewsDto> toHeadlineNewsDtoList(List<News> newsList) {
        return newsList.stream()
                .map(this::toHeadlineNewsDto)
                .collect(Collectors.toList());
    }

    private HeadlineNewsDto toHeadlineNewsDto(News news) {
        return HeadlineNewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .datePublication(news.getDatePublication())
                .previewUrl(news.getImages().stream()
                        .filter(t -> NewsImageRole.PREVIEW.equals(t.getNewsImageRole()))
                        .findFirst()
                        .map(img -> imageStorageService.getImageUrl(img.getImageRegistry().getId()))
                        .orElse(null))
                .build();
    }
}