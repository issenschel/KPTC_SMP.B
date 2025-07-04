package com.example.kptc_smp.service.main.news;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.image.ImageResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupResponseDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.model.main.ImageRegistry;
import com.example.kptc_smp.model.main.News;
import com.example.kptc_smp.model.main.NewsImage;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.NewsImageRole;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import com.example.kptc_smp.service.main.image.ImageMapperService;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import com.example.kptc_smp.service.main.validator.ImageValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapperService newsMapperService;
    private final NewsImageService newsImageService;
    private final ImageStorageService imageStorageService;
    private final ImageValidatorService imageValidatorService;
    private final ImageMapperService imageMapperService;

    @Value("${message.news.deleted}")
    private String newsDeletedMessage;

    @Value("${page.news.headline.size}")
    private int headlinePageSize;

    @Value("${page.news.headline.sort.field}")
    private String headlineSortField;

    @Transactional(rollbackFor = IOException.class)
    public NewsResponseDto createNews(NewsRequestDto newsRequestDto, MultipartFile image) {
        imageValidatorService.validateImage(image);

        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        news.setDatePublication(LocalDateTime.now());
        newsRepository.save(news);

        ImageRegistry imageRegistry = imageStorageService.uploadAndAttachImage(image, ImageCategory.NEWS, news.getId());
        NewsImage newsImage = newsImageService.createNewsImage(news, imageRegistry, NewsImageRole.PREVIEW);
        news.getImages().add(newsImage);

        return newsMapperService.toNewsResponseDto(news);
    }

    @Transactional
    public NewsResponseDto updateNews(NewsRequestDto newsRequestDto, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        return newsMapperService.toNewsResponseDto(news);
    }

    @Transactional
    public ImageResponseDto updateNewsPreview(MultipartFile image, int id) {
        imageValidatorService.validateImage(image);

        News news = newsRepository.findWithImagesAndRegistryById(id).orElseThrow(NewsNotFoundException::new);
        NewsImage newsImage = news.getImages().stream()
                .filter(img -> NewsImageRole.PREVIEW.equals(img.getNewsImageRole()))
                .findFirst()
                .orElse(null);

        ImageRegistry newImageRegistry;
        if (newsImage != null && newsImage.getImageRegistry() != null) {
            newImageRegistry = imageStorageService.updateImage(image, newsImage.getImageRegistry());
            newsImage.setImageRegistry(newImageRegistry);
        } else {
            newImageRegistry = imageStorageService.uploadAndAttachImage(image, ImageCategory.NEWS, news.getId());
            newsImageService.createNewsImage(news, newImageRegistry, NewsImageRole.PREVIEW);
        }

        return imageMapperService.toImageResponse(newImageRegistry);
    }

    public NewsResponseDto getNews(int newsId) {
        News news = newsRepository.findWithImagesById(newsId).orElseThrow(NewsNotFoundException::new);
        return newsMapperService.toNewsResponseDto(news);
    }

    @Transactional
    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
        imageStorageService.deleteFolder(news.getImages().stream().map(NewsImage::getImageRegistry).toList());
        return new ResponseDto(newsDeletedMessage);
    }

    @Transactional
    public HeadlineNewsGroupResponseDto getHeadlineNews(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, headlinePageSize, Sort.by(Sort.Direction.DESC, headlineSortField));
        Page<Integer> idsPage = newsRepository.findNewsIds(pageRequest);
        List<News> newsList = newsRepository.findFullNewsByIds(idsPage.getContent());
        return HeadlineNewsGroupResponseDto.builder()
                .news(newsMapperService.toHeadlineNewsDtoList(newsList))
                .countPage(idsPage.getTotalPages())
                .build();
    }
}
