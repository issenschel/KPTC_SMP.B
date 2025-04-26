package com.example.kptc_smp.service.main.image.strategies;

import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.interfaces.ImageStrategy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public abstract class AbstractImageStrategy implements ImageStrategy {
    protected final Path storagePath;
    protected final String defaultImageName;
    protected final String imageUrlPrefix;

    public AbstractImageStrategy(Path storagePath, String defaultImageName, String imageUrlPrefix) {
        this.storagePath = storagePath;
        this.defaultImageName = defaultImageName;
        this.imageUrlPrefix = imageUrlPrefix;
    }

    @Override
    public Resource getImageAsResource(String filename) throws FileNotFoundException, IOException {
        Path imagePath = storagePath.resolve(filename).toAbsolutePath();
        if (!imagePath.toFile().exists()) {
            throw new FileNotFoundException();
        }
        return new UrlResource(imagePath.toUri());
    }

    @Override
    public String getImageUrl(String imageName) {
        Path imagePath = storagePath.resolve(imageName).toAbsolutePath();
        if (!imagePath.toFile().exists()) {
            imagePath = storagePath.resolve(defaultImageName).toAbsolutePath();
        }
        return imageUrlPrefix + imagePath.getFileName().toString();
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String fileName = generateUniqueFileName(image);
        Path filePath = storagePath.resolve(fileName).toAbsolutePath();
        Files.createDirectories(storagePath.toAbsolutePath());
        image.transferTo(filePath.toFile());
        return fileName;

    }

    @Override
    public void deleteImage(String filename) throws IOException {
        Path imagePath = storagePath.resolve(filename).toAbsolutePath();
        if (!imagePath.getFileName().toString().equals(defaultImageName)) {
            Files.deleteIfExists(imagePath);
        }
    }

    @Override
    public void transferImage(Path source, Path destination) throws IOException {
        Path parentDirectory = destination.getParent().toAbsolutePath();
        if (!Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }
        Files.move(source.toAbsolutePath(), destination.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
    }

    protected String generateUniqueFileName(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return UUID.randomUUID() + extension;
    }

    @Override
    public Path getStoragePath() {
        return storagePath;
    }

    @Override
    public String getDefaultImageName() {
        return defaultImageName;
    }
}