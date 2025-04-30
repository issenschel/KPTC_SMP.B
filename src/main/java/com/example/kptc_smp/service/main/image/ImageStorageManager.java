package com.example.kptc_smp.service.main.image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageStorageManager {
    @Value("${file.storage.root}")
    private String storageRoot;

    public Path storeFile(MultipartFile file, String relativePath) throws IOException {
        Path fullPath = getFullPath(relativePath);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);
        return fullPath;
    }

    public Path moveFile(String sourcePath, String newRelativePath) throws IOException {
        Path newPath = getFullPath(newRelativePath);
        Files.createDirectories(newPath.getParent());
        Files.move(Path.of(sourcePath), newPath);
        return newPath;
    }

    public void deleteFile(String storagePath) throws IOException {
        Path path = Path.of(storagePath).toAbsolutePath();
        Files.deleteIfExists(path);
    }

    public void deleteOwnerImage(String storagePath) throws IOException {
        Path path = Path.of(storagePath).toAbsolutePath();
        Path parentFolder = path.getParent();
        if (parentFolder != null && Files.exists(parentFolder)) {
            Files.deleteIfExists(parentFolder);
        }
    }

    private Path getFullPath(String relativePath) {
        return Path.of(storageRoot, relativePath).toAbsolutePath();
    }
}