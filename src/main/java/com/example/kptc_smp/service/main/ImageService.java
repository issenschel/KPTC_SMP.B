package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.image.ImageDto;
import com.example.kptc_smp.entity.main.User;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageNotFoundException;
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
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public Resource getImageAsResource(String imageName){
        Path path = Paths.get(uploadPath + File.separator + imageName);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException();
        }
    }

    public ImageDto getImageAsBytes(String imageName){
        try {
            File image = new File(uploadPath + File.separator + imageName);
            return new ImageDto(Files.readAllBytes(image.toPath()));
        } catch (IOException e){
            throw new ImageNotFoundException();
        }
    }

    public String updateOrUploadImage(MultipartFile image, User user){
        if (user.getUserInformation().getImageName() == null) {
            return uploadImage(image);
        } else {
            return updateImage(image, user.getUserInformation().getImageName());
        }
    }

    public String updateImage(MultipartFile image, String oldImageName) {
        deleteOldImage(oldImageName);
        return uploadImage(image);
    }

    public void deleteOldImage(String oldImageName){
        try {
            Files.delete(Path.of(uploadPath + File.separator + oldImageName));
        } catch (IOException ignored) {}
    }

    public String uploadImage(MultipartFile image){
        String fileUUID = UUID.randomUUID().toString();
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String result = fileUUID + "." + extension;
        File file = new File(uploadPath + File.separator + result);
        try {
            image.transferTo(file);
            return result;
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public boolean isValidImage(MultipartFile image){
        return image != null && image.getContentType() != null && image.getContentType().matches("image/.*");
    }
}
