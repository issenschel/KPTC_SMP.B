package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ImageValidator {
    private final Set<String> allowedTypes = Set.of("image/jpeg", "image/png");

    public void validateImage(MultipartFile image) {
        if (!isValidImage(image)) {
            throw new ImageInvalidFormatException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null && !image.isEmpty() && allowedTypes.contains(image.getContentType());
    }
}
