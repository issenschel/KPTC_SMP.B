package com.example.kptc_smp.service;

import com.example.kptc_smp.exception.profile.PhotoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    public String updatePhoto(MultipartFile photo, String oldFileName) {
        try {
            if (oldFileName != null) {
                Files.delete(Path.of(uploadPath + "/" + oldFileName));
            }
            String uuidFile = UUID.randomUUID().toString();
            String result = uuidFile + "." + photo.getOriginalFilename();
            photo.transferTo(new File(uploadPath + "/" + result));
            return result;
        } catch (IOException e) {
            throw new PhotoException();
        }
    }
}
