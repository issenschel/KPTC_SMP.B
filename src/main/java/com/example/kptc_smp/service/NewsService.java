package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.news.ListNewsDto;
import com.example.kptc_smp.dto.news.NewsDto;
import com.example.kptc_smp.entity.postgreSQL.News;
import com.example.kptc_smp.exception.NewsNotFoundException;
import com.example.kptc_smp.exception.profile.PhotoException;
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
import java.nio.file.Path;
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
        if (photo != null) {
            news.setPhoto(check(photo));
        }
        newsRepository.save(news);
    }

    public void changeNews(NewsDto newsDto,MultipartFile photo,int id){
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        if(photo != null){
            if (news.getPhoto() != null) {
                try {
                    Files.delete(Path.of(uploadPath + "/" + news.getPhoto()));
                } catch (IOException e) {
                    throw new PhotoException();
                }
            }
            news.setPhoto(check(photo));
        }
        newsRepository.save(news);
    }

    public ListNewsDto getNews(int page) {
        ListNewsDto listNewsDto = new ListNewsDto();
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        listNewsDto.setNews(newsPage.getContent());
        listNewsDto.setCount(totalPages);
        return listNewsDto;
    }

    public void deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
    }

    private String check(MultipartFile photo){
        String uuidFile = UUID.randomUUID().toString();
        String result = uuidFile + "." + photo.getOriginalFilename();
        try {
            photo.transferTo(new File(uploadPath + "/" + result));
        } catch (IOException e) {
            throw new PhotoException();
        }
        return result;
    }

}
