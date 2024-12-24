package com.example.kptc_smp.service;

import com.example.kptc_smp.exception.image.OldImageNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public void deleteOldImage(String oldImageName) throws IOException {
        try {
            Files.delete(Path.of(uploadPath + "/" + oldImageName));
        } catch (IOException e) {
            throw new OldImageNotFoundException();
        }

    }

    public String uploadImage(MultipartFile image){
        try {
            String uuidFile = UUID.randomUUID().toString();
            String result = uuidFile + "." + image.getOriginalFilename();
            image.transferTo(new File(uploadPath + "/" + result));
            return result;
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public String updateImage(MultipartFile image, String oldImageName) {
        try {
            deleteOldImage(oldImageName);
            return uploadImage(image);
        } catch (IOException e) {
            throw new ImageException();
        }
    }

}
