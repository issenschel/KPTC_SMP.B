package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.image.ImageDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
import com.example.kptc_smp.service.google.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final GoogleDriveService googleDriveService;

    @Value("${upload.path}")
    private String uploadPath;

    public Resource getImageAsResource(String imageName) {
        Path path = getImagePath(imageName);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException();
        }
    }

    public ImageDto getImageAsBytes(String imageName) {
        Path path = getImagePath(imageName);
        try {
            return new ImageDto(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new ImageNotFoundException();
        }
    }

    private Path getImagePath(String imageName) {
        Path path = Paths.get(uploadPath, imageName);
        if (!Files.exists(path)) {
            throw new ImageNotFoundException();
        }
        return path;
    }

    public void deleteOldImage(String oldImageName){
        try {
            Files.delete(Path.of(uploadPath + File.separator + oldImageName));
        } catch (IOException ignored) {}
    }

    public String uploadImage(MultipartFile image,String folderId){
        String fileUUID = UUID.randomUUID().toString();
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String result = fileUUID + "." + extension;
        File file = new File(uploadPath + File.separator + result);
        try {
            image.transferTo(file);
            googleDriveService.uploadImageToDrive(file, folderId);
            return result;
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public boolean isValidImage(MultipartFile image){
        return image != null && image.getContentType() != null && image.getContentType().matches("image/.*");
    }
}
