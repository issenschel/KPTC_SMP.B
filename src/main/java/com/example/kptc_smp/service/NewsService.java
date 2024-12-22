package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.news.NewsDto;
import com.example.kptc_smp.entity.postgreSQL.News;
import com.example.kptc_smp.repository.postgreSQL.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public void createNews(NewsDto newsDto, MultipartFile photo){
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        String uuidFile = UUID.randomUUID().toString();
        String result = uuidFile + "." + photo.getOriginalFilename();
        try {
            photo.transferTo(new File(uploadPath + "/" + result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        news.setPhoto(result);
        newsRepository.save(news);
    }

    public void changeNews(NewsDto newsDto, int id){
        News news = newsRepository.findById(id).orElseThrow(RuntimeException::new);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        newsRepository.save(news);
    }

    public void changeNews(NewsDto newsDto, int id, MultipartFile photo){
        News news = newsRepository.findById(id).orElseThrow(RuntimeException::new);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        newsRepository.save(news);

    }
}
