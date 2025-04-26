package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.enums.ImageType;
import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import com.example.kptc_smp.service.main.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    public NewsResponseDto createNews(NewsRequestDto newsRequestDto, MultipartFile image) {
        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        news.setDatePublication(LocalDateTime.now());
        news.setImageName(imageService.uploadImage(ImageType.NEWS,image));
        newsRepository.save(news);
        return getNewsResponseDto(news);
    }

    public NewsResponseDto changeNews(NewsRequestDto newsRequestDto, MultipartFile image, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        if (imageService.isValidImage(image)) {
            news.setImageName(imageService.updateImage(ImageType.NEWS,image, news.getImageName()));
        }
        newsRepository.save(news);
        return getNewsResponseDto(news);
    }

    public NewsResponseDto getNews(int newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
        return getNewsResponseDto(news);
    }

    private NewsResponseDto getNewsResponseDto(News news){
        NewsResponseDto newsResponseDto = new NewsResponseDto();
        newsResponseDto.setId(news.getId());
        newsResponseDto.setTitle(news.getTitle());
        newsResponseDto.setContent(news.getContent());
        newsResponseDto.setDatePublication(news.getDatePublication());
        newsResponseDto.setPhotoUrl(imageService.getImageUrl(ImageType.NEWS, news.getImageName()));
        return newsResponseDto;
    }

    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        imageService.deleteImage(ImageType.NEWS,news.getImageName());
        newsRepository.delete(news);
        return new ResponseDto("Новость удалена");
    }

    public HeadlineNewsGroupDto getHeadlineNews(int page) {
        HeadlineNewsGroupDto headlineNewsGroupDto = new HeadlineNewsGroupDto();
        PageRequest pageRequest = PageRequest.of(page - 1, 9, Sort.by(Sort.Direction.DESC, "id"));
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        List<HeadlineNewsDto> headlineNewsDto = convertNewsToHeadlineNewsDto(newsPage);
        headlineNewsGroupDto.setNews(headlineNewsDto);
        headlineNewsGroupDto.setCountPage(totalPages);
        return headlineNewsGroupDto;
    }

    private List<HeadlineNewsDto> convertNewsToHeadlineNewsDto(Page<News> newsPage) {
        return newsPage.getContent().stream().map(
                news -> {
                    HeadlineNewsDto headlineNewsDto = new HeadlineNewsDto();
                    headlineNewsDto.setId(news.getId());
                    headlineNewsDto.setTitle(news.getTitle());
                    headlineNewsDto.setDatePublication(news.getDatePublication());
                    headlineNewsDto.setPhotoUrl(imageService.getImageUrl(ImageType.NEWS, news.getImageName()));
                    return headlineNewsDto;
                }).collect(Collectors.toList());
    }
}
