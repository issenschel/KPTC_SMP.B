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

    public void validateImage(MultipartFile image) {
        if (!isValidImage(image)) {
            throw new ImageInvalidFormatException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null && !image.isEmpty() && allowedTypes.contains(image.getContentType());
    }
}
