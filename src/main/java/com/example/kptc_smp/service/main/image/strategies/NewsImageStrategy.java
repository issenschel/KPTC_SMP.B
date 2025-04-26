package com.example.kptc_smp.service.main.image.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class NewsImageStrategy extends AbstractImageStrategy {

    public NewsImageStrategy(
            @Value("${upload.path.image.news}") Path storagePath,
            @Value("${standard.image.news}") String defaultImageName) {
        super(storagePath, defaultImageName, "/images/news/");
    }


}