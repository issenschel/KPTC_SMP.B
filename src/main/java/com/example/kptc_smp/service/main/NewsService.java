package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsDto;
import com.example.kptc_smp.dto.news.HeadlineGroupNewsDto;
import com.example.kptc_smp.dto.news.NewsDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    public News createNews(NewsDto newsDto, MultipartFile image) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setDatePublication(LocalDate.now());
        if (imageService.isValidImage(image)) {
            news.setImageName(imageService.uploadImage(image));
        }
        newsRepository.save(news);
        return news;
    }

    public News changeNews(NewsDto newsDto, MultipartFile image, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        if (imageService.isValidImage(image)) {
            news.setImageName(imageService.updateImage(image, news.getImageName()));
        }
        newsRepository.save(news);
        return news;
    }

    public News getNews(int newsId) {
        return newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
    }

    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
        return new ResponseDto("Новость удалена");
    }

    public HeadlineGroupNewsDto getHeadlineNews(int page) {
        HeadlineGroupNewsDto headlineGroupNewsDto = new HeadlineGroupNewsDto();
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        List<HeadlineNewsDto> headlineNewsDto = convertNewsToHeadlineNewsDto(newsPage);
        headlineGroupNewsDto.setNews(headlineNewsDto);
        headlineGroupNewsDto.setCount(totalPages);
        return headlineGroupNewsDto;
    }

    private List<HeadlineNewsDto> convertNewsToHeadlineNewsDto(Page<News> newsPage) {
        return newsPage.getContent().stream().map(
                news -> {
                    HeadlineNewsDto headlineNewsDto = new HeadlineNewsDto();
                    headlineNewsDto.setId(news.getId());
                    headlineNewsDto.setTitle(news.getTitle());
                    headlineNewsDto.setDatePublication(news.getDatePublication());
                    headlineNewsDto.setPhotoName(news.getImageName());
                    return headlineNewsDto;
                }).collect(Collectors.toList());
    }

}
