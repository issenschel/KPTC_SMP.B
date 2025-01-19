package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.image.ImageDto;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.image.ImageTransferException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    public Resource getImageAsResource(Path imagePath) {
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException();
        }
    }

    public ImageDto getImageAsBytes(Path imagePath) {
        try {
            return new ImageDto(Files.readAllBytes(imagePath));
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
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
        String fileName  = fileUUID + "." + extension;
        Path filePath = imageStoragePath.resolve(fileName);
        try {
            Files.createDirectories(imageStoragePath);
            image.transferTo(filePath.toFile());
            return fileName;
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public void transferImage(Path oldImagePath, Path newImagePath) {
        try {
            Path parentDirectory = newImagePath.getParent();
            if (!Files.exists(parentDirectory)) {
                Files.createDirectories(parentDirectory);
            }
            Files.move(oldImagePath, newImagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageTransferException();
        }
    }

    public boolean isValidImage(MultipartFile image) {
        return image != null && image.getContentType() != null && image.getContentType().matches("image/.*");
    }
}
