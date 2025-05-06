package com.example.kptc_smp.service.main.news;

import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.entity.main.News;
import com.example.kptc_smp.entity.main.NewsImage;
import com.example.kptc_smp.enums.NewsImageRole;
import com.example.kptc_smp.repository.main.NewsImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NewsImageService {
    private final NewsImageRepository newsImageRepository;

    public NewsImage createNewsImage(News news, ImageRegistry imageRegistry, NewsImageRole newsImageRole) {
        NewsImage newsImage = new NewsImage();
        newsImage.setNews(news);
        newsImage.setImageRegistry(imageRegistry);
        newsImage.setNewsImageRole(newsImageRole);
        newsImageRepository.save(newsImage);
        return newsImageRepository.save(newsImage);
    }

    public void deleteNewsImage(NewsImage newsImage) {
        newsImageRepository.delete(newsImage);
    }

    public void deleteAllNewsImage(Collection<NewsImage> newsImage) {
        newsImageRepository.deleteAll(newsImage);
    }

}
