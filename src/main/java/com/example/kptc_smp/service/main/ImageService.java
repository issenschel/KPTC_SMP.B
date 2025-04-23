package com.example.kptc_smp.service.main;

import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageSendException;
import com.example.kptc_smp.exception.image.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${upload.path.image.profile}")
    private Path profileImagesDirectory;

    @Value("${upload.path.image.news}")
    private Path newsImagesDirectory;

    @Value("${standard.image.news}")
    private String standardImageNewsName;

    @Value("${standard.image.profile}")
    private String standardImageProfileName;

    public Resource getProfileImageAsResource(String imageName) {
        return getImageAsResource(profileImagesDirectory.resolve(imageName));
    }

    public Resource getNewsImageAsResource(String imageName) {
        return getImageAsResource(newsImagesDirectory.resolve(imageName));
    }

    private Resource getImageAsResource(Path imagePath) {
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException();
        }
        try {
            return new UrlResource(imagePath.toUri());
        } catch (IOException e) {
            throw new ImageSendException();
        }
    }

    public String getNewsImageUrl(Path imagePath) {
        return getImageUrlWithFallback(imagePath, standardImageNewsName);
    }

    public String getProfileImageUrl(Path imagePath) {
        return getImageUrlWithFallback(imagePath, standardImageProfileName);
    }

    private String getImageUrlWithFallback(Path imagePath, String defaultImageName) {
        if (!Files.exists(imagePath)) {
            imagePath = imagePath.resolveSibling(defaultImageName);
        }

        Path parent = imagePath.getParent().getParent();
        Path relativePath = parent.relativize(imagePath);

        return serverUrl + "/images/" + relativePath.toString().replace("\\", "/");
    }

    public void deleteOldImage(Path imagePath) {
        try {
            Files.delete(imagePath);
        } catch (IOException ignored) {

        }
    }

    public String uploadImage(MultipartFile image, Path imageStoragePath) {
        String fileUUID = UUID.randomUUID().toString();
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String fileName = fileUUID + "." + extension;
        Path filePath = imageStoragePath.resolve(fileName);
        try {
            Files.createDirectories(imageStoragePath);
            image.transferTo(filePath.toFile());
            return fileName;
        } catch (IOException e) {
            throw new ImageUploadException();
        }
    }

    public String updateImage(MultipartFile image, String oldImageName,Path imageStoragePath) {
        deleteOldImage(imageStoragePath.resolve(oldImageName));
        return uploadImage(image, imageStoragePath.toAbsolutePath());
    }

    public void transferImage(Path oldImagePath, Path newImagePath) {
        try {
            Path parentDirectory = newImagePath.getParent();
            if (!Files.exists(parentDirectory)) {
                Files.createDirectories(parentDirectory);
            }
            Files.move(oldImagePath, newImagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null && image.getContentType() != null && image.getContentType().matches("image/.*");
    }
}
