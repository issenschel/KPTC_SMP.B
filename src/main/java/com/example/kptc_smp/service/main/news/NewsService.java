package com.example.kptc_smp.service.main.news;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import com.example.kptc_smp.service.main.image.ImageStorageService;
import com.example.kptc_smp.service.main.image.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final ImageStorageService imageStorageService;
    private final ImageValidator imageValidator;

    @Transactional(rollbackFor = IOException.class)
    public NewsResponseDto createNews(NewsRequestDto newsRequestDto, MultipartFile image) {
        imageValidator.validateImage(image);

        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        news.setDatePublication(LocalDateTime.now());
        newsRepository.save(news);

        ImageResponse imageResponse = imageStorageService.uploadAndAttachImage(image, ImageCategory.NEWS, news.getId());
        news.setImageName(imageResponse.getId());

        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public NewsResponseDto updateNews(NewsRequestDto newsRequestDto, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        newsRepository.save(news);
        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public NewsResponseDto updateNewsPreview(MultipartFile image, int id) {
        imageValidator.validateImage(image);
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        Optional<ImageRegistry> fileRegistry = imageStorageService.findById(news.getImageName());

        ImageResponse imageResponse;
        if (fileRegistry.isPresent()) {
            imageResponse = imageStorageService.updateImage(image, fileRegistry.get());
        } else {
            imageResponse = imageStorageService.uploadAndAttachImage(image, ImageCategory.NEWS, news.getId());
        }

        news.setImageName(imageResponse.getId());
        newsRepository.save(news);
        return newsMapper.toNewsResponseDto(news);

    }

    public NewsResponseDto getNews(int newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        List<ImageRegistry> imageRegistry = imageStorageService.findByOwnerId(news.getId());
        imageStorageService.deleteFolder(imageRegistry);
        newsRepository.delete(news);
        return new ResponseDto("Новость удалена");
    }

    public HeadlineNewsGroupDto getHeadlineNews(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 9, Sort.by(Sort.Direction.DESC, "id"));
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        return HeadlineNewsGroupDto.builder()
                .news(newsMapper.toHeadlineNewsDtoList(newsPage.getContent()))
                .countPage(newsPage.getTotalPages())
                .build();
    }
}
