package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.dto.news.HeadlineNewsDto;
import com.example.kptc_smp.dto.news.HeadlineNewsGroupDto;
import com.example.kptc_smp.dto.news.NewsDto;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.repository.main.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;

    @Value("${upload.path.image.news}")
    private Path newsImagesDirectory;

    @Value("${standard.image.news}")
    private String standardImageNewsName;

    public News createNews(NewsDto newsDto, MultipartFile image) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setDatePublication(LocalDateTime.now());
        if (imageService.isValidImage(image)) {
            news.setImageName(imageService.uploadImage(image, newsImagesDirectory.resolve(String.valueOf(news.getId())).toAbsolutePath()));
        } else {
            news.setImageName(standardImageNewsName);
        }
        newsRepository.save(news);
        return news;
    }

    public News changeNews(NewsDto newsDto, MultipartFile image, int id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        if (imageService.isValidImage(image)) {
            String imageName = imageService.updateImage(image, news.getImageName(), newsImagesDirectory);
            news.setImageName(imageName);
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
                    headlineNewsDto.setPhotoUrl(imageService.getImageUrl(newsImagesDirectory.resolve(news.getImageName()).toAbsolutePath()));
                    return headlineNewsDto;
                }).collect(Collectors.toList());
    }
}
