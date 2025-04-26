package com.example.kptc_smp.service.main.image.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ProfileImageStrategy extends AbstractImageStrategy {

    public ProfileImageStrategy(
            @Value("${upload.path.image.profile}") Path storagePath,
            @Value("${standard.image.profile}") String defaultImageName) {
        super(storagePath, defaultImageName, "/images/profile/");
    }
}