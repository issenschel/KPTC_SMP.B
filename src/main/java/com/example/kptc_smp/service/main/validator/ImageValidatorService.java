package com.example.kptc_smp.service.main.validator;

import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ImageValidatorService {

    @Value("#{'${image.allowed.mime.types}'.split(',')}")
    private Set<String> allowedTypes;

    @Value("#{'${image.allowed.extensions}'.split(',')}")
    private Set<String> allowedExtensions;

    public void validateImage(MultipartFile image) {
        if (!isValidImage(image)) {
            throw new ImageInvalidFormatException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null &&
               !image.isEmpty() &&
               allowedTypes.contains(image.getContentType()) &&
               hasValidExtension(image.getOriginalFilename());
    }

    private boolean hasValidExtension(String filename) {
        if (filename == null) {
            return false;
        }
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        return allowedExtensions.contains(extension);
    }
}
