package com.example.kptc_smp.interfaces;

import com.example.kptc_smp.exception.file.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageStrategy {
    Resource getImageAsResource(String filename) throws IOException, FileNotFoundException;
    String getImageUrl(String imageName);
    String uploadImage(MultipartFile image) throws IOException; ;
    void deleteImage(String filename) throws IOException;
    void transferImage(Path source, Path destination) throws  IOException;
    Path getStoragePath();
    String getDefaultImageName();
}
