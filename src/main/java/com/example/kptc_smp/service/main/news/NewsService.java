package com.example.kptc_smp.service.main.news;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.entity.main.NewsImage;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.NewsImageRole;
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

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsImageService newsImageService;
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

        ImageRegistry imageRegistry = imageStorageService.uploadAndAttachImage(image, ImageCategory.NEWS, news.getId());
        NewsImage newsImage = newsImageService.createNewsImage(news, imageRegistry, NewsImageRole.PREVIEW);
        news.getImages().add(newsImage);

        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public NewsResponseDto updateNews(NewsRequestDto newsRequestDto, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public NewsResponseDto updateNewsPreview(MultipartFile image, int id) {
        imageValidator.validateImage(image);

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

        return newsMapper.toNewsResponseDto(news);
    }

    public NewsResponseDto getNews(int newsId) {
        News news = newsRepository.findWithImagesById(newsId).orElseThrow(NewsNotFoundException::new);
        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional
    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
        imageStorageService.deleteFolder(news.getImages().stream().map(NewsImage::getImageRegistry).toList());
        return new ResponseDto("Новость удалена");
    }

    @Transactional
    public HeadlineNewsGroupDto getHeadlineNews(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 9, Sort.by(Sort.Direction.DESC, "id"));
        Page<Integer> idsPage = newsRepository.findNewsIds(pageRequest);
        List<News> newsList = newsRepository.findFullNewsByIds(idsPage.getContent());
        return HeadlineNewsGroupDto.builder()
                .news(newsMapper.toHeadlineNewsDtoList(newsList))
                .countPage(idsPage.getTotalPages())
                .build();
    }
}
