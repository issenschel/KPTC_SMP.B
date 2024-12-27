package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.ListHeadlineNewsDto;
import com.example.kptc_smp.dto.news.HeadlineNewsDto;
import com.example.kptc_smp.dto.news.NewsRequestDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.exception.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${upload.path}")
    private String uploadPath;

    public News createNews(NewsRequestDto newsRequestDto, MultipartFile image) {
        News news = new News();
        news.setTitle(newsRequestDto.getTitle());
        news.setContent(newsRequestDto.getContent());
        news.setDatePublication(LocalDate.now());
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

    public ListHeadlineNewsDto getHeadlineNews(int page) {
        ListHeadlineNewsDto listHeadlineNewsDto = new ListHeadlineNewsDto();
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<News> newsPage = newsRepository.findAll(pageRequest);
        int totalPages = newsPage.getTotalPages();
        List<HeadlineNewsDto> headlineNewsDtoList = newsPage.getContent().stream().map(
                news -> {
                    HeadlineNewsDto headlineNewsDto = new HeadlineNewsDto();
                    headlineNewsDto.setId(news.getId());
                    headlineNewsDto.setTitle(news.getTitle());
                    headlineNewsDto.setDatePublication(news.getDatePublication());
                    headlineNewsDto.setPhotoName(news.getImageName());
                    return headlineNewsDto;
                }).collect(Collectors.toList());
        listHeadlineNewsDto.setNews(headlineNewsDtoList);
        listHeadlineNewsDto.setCount(totalPages);
        return listHeadlineNewsDto;
    }

    public News getNews(int newsId){
        return newsRepository.findById(newsId).orElseThrow(NewsNotFoundException::new);
    }



    public ResponseDto deleteNews(int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        newsRepository.delete(news);
        return new ResponseDto("Новость удалена");
    }

}
