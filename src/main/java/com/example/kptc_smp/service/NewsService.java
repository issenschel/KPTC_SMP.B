package com.example.kptc_smp.service;

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
    private final FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    public void createNews(NewsRequestDto newsRequestDto, MultipartFile photo){
        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        if (photo != null) {
            news.setPhoto(fileService.updatePhoto(photo,null));
        }
        newsRepository.save(news);
    }

    public void changeNews(NewsRequestDto newsRequestDto, MultipartFile photo, int id){
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        if(photo != null){
            news.setPhoto(fileService.updatePhoto(photo,news.getPhoto()));
        }
        newsRepository.save(news);
    }

    public ListNewsDto getNews(int page) {
        ListNewsDto listNewsDto = new ListNewsDto();
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        List<NewsResponseDto> newsDtoList = newsPage.getContent().stream().map(
                news -> {
                    NewsResponseDto newsResponseDto = new NewsResponseDto();
                    newsResponseDto.setId(news.getId());
                    newsResponseDto.setTitle(news.getTitle());
                    newsResponseDto.setContent(news.getContent());
                    if(news.getPhoto() != null){
                        try {
                            File arr = new File(uploadPath + "/" + news.getPhoto());
                            newsResponseDto.setPhoto(Files.readAllBytes(arr.toPath()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return newsResponseDto;
                }).collect(Collectors.toList());
        listNewsDto.setNews(newsDtoList);
        listNewsDto.setCount(totalPages);
        return listNewsDto;
    }

    public void deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
    }

}
