package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.ListNewsDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.dto.news.NewsResponseDto;
import com.example.kptc_smp.entity.postgreSQL.News;
import com.example.kptc_smp.exception.NewsNotFoundException;
import com.example.kptc_smp.repository.postgreSQL.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    @Value("${upload.path}")
    private String uploadPath;

    public News createNews(NewsRequestDto newsRequestDto, MultipartFile image) {
        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        if (image.getContentType() != null && image.getContentType().matches("image/.*")) {
            news.setImageName(imageService.uploadImage(image));
        }
        newsRepository.save(news);
        return news;
    }

    public News changeNews(NewsRequestDto newsRequestDto, MultipartFile image, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        if (image.getContentType() != null && image.getContentType().matches("image/.*")) {
            news.setImageName(imageService.updateImage(image, news.getImageName()));
        }
        newsRepository.save(news);
        return news;
    }

    public ListNewsDto getNews(int page) {
        ListNewsDto listNewsDto = new ListNewsDto();
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        List<NewsResponseDto> newsDtoList = newsPage.getContent().stream().map(
                news -> {
                    NewsResponseDto newsResponseDto = new NewsResponseDto();
                    newsResponseDto.setId(news.getId());
                    newsResponseDto.setTitle(news.getTitle());
                    newsResponseDto.setContent(news.getContent());
                    if (news.getImageName() != null) {
                        try {
                            File image = new File(uploadPath + "/" + news.getImageName());
                            newsResponseDto.setPhoto(Files.readAllBytes(image.toPath()));
                        } catch (IOException ignored) {

                        }
                    }
                    return newsResponseDto;
                }).collect(Collectors.toList());
        listNewsDto.setNews(newsDtoList);
        listNewsDto.setCount(totalPages);
        return listNewsDto;
    }

    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
        return new ResponseDto("Новость удалена");
    }

}
