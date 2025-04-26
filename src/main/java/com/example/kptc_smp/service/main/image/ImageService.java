package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.enums.ImageType;
import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import com.example.kptc_smp.exception.image.ImageSendException;
import com.example.kptc_smp.exception.image.ImageTransferException;
import com.example.kptc_smp.exception.image.ImageUploadException;
import com.example.kptc_smp.interfaces.ImageStrategy;
import com.example.kptc_smp.service.main.image.strategies.NewsImageStrategy;
import com.example.kptc_smp.service.main.image.strategies.ProfileImageStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final Set<String> allowedTypes = Set.of("image/jpeg", "image/png");
    private final Map<ImageType, ImageStrategy> strategies = new EnumMap<>(ImageType.class);
    private final ProfileImageStrategy profileImageStrategy;
    private final NewsImageStrategy newsImageStrategy;

    @Value("${server.url}")
    private String serverUrl;

    @PostConstruct
    public void init() {
        strategies.put(ImageType.PROFILE, profileImageStrategy);
        strategies.put(ImageType.NEWS, newsImageStrategy);
    }

    public Resource getImageAsResource(ImageType type, String filename) {
        try {
            return strategies.get(type).getImageAsResource(filename);
        } catch (IOException e) {
            throw new ImageSendException();
        }
    }

    public String getImageUrl(ImageType type, String imageName) {
        return serverUrl + strategies.get(type).getImageUrl(imageName);
    }

    public String updateImage(ImageType imageType, MultipartFile image, String oldImageName)  {
        deleteImage(imageType,oldImageName);
        return uploadImage(imageType, image);
    }

    public String uploadImage(ImageType type, MultipartFile image) {
        if (!isValidImage(image)) {
            throw new ImageInvalidFormatException();
        }
        try {
            return strategies.get(type).uploadImage(image);
        } catch (IOException e) {
            throw new ImageUploadException();
        }
    }

    public void deleteImage(ImageType type, String filename) {
        try {
            strategies.get(type).deleteImage(filename);
        }catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public void transferImage(ImageType type, Path source, Path destination) {
        try {
            strategies.get(type).transferImage(source, destination);
        } catch (IOException e) {
            throw new ImageTransferException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null && !image.isEmpty() && allowedTypes.contains(image.getContentType());
    }

    public Path getStoragePath(ImageType imageType) {
        return strategies.get(imageType).getStoragePath();
    }

    public String getDefaultImageName(ImageType imageType) {
        return strategies.get(imageType).getDefaultImageName();
    }
}
