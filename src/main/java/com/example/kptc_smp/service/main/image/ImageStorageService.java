package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.ImageStatus;
import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.repository.main.FileRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageStorageService {
    private final FileRegistryRepository fileRegistryRepository;
    private final ImageStorageManager storageManager;
    private final ImagePathBuilder pathBuilder;
    private final ImageValidator imageValidator;
    private final ImageRegistryManager registryManager;

    @Value("${file.storage.temp-expiration-hours}")
    private int tempExpirationHours;

    @Value("${server.url}")
    private String serverUrl;

    @Transactional
    public ImageResponse uploadTempFile(MultipartFile image){
        imageValidator.validateImage(image);

        UUID fileId = UUID.randomUUID();
        String extension = pathBuilder.getExtension(image.getOriginalFilename());
        String relativePath = pathBuilder.buildTempPath(fileId, extension);
        try {
            Path path = storageManager.storeFile(image, relativePath);
            return registryManager.createTempRegistry(image, fileId, path);
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    public ImageResponse uploadAndAttachFile(MultipartFile file, ImageCategory category, Integer ownerId) throws IOException {
        UUID fileId = UUID.randomUUID();
        String extension = pathBuilder.getExtension(file.getOriginalFilename());
        String relativePath = pathBuilder.buildPermanentPath(category, ownerId, fileId, extension);

        Path path = storageManager.storeFile(file, relativePath);
        return registryManager.createAttachedRegistry(file, fileId, path, category, ownerId);
    }

    public ImageResponse updateFile(MultipartFile file, ImageRegistry imageRegistry) throws IOException {
        ImageResponse response = uploadAndAttachFile(file, imageRegistry.getImageCategory(), imageRegistry.getOwnerId());
        deleteImage(imageRegistry);
        return response;
    }

    public void attachFile(UUID fileId, ImageCategory category, Integer ownerId) throws IOException {
        ImageRegistry file = fileRegistryRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        if (file.getStatus() != ImageStatus.TEMP) {
            throw new ImageException("Only temp files can be attached");
        }

        String extension = pathBuilder.getExtension(file.getOriginalName());
        String newRelativePath = pathBuilder.buildPermanentPath(category, ownerId, fileId, extension);
        storageManager.moveFile(file.getStoragePath(), newRelativePath);

        registryManager.updateRegistry(file, newRelativePath, category);
    }

    public void deleteImage(ImageRegistry imageRegistry) throws IOException {
        storageManager.deleteFile(imageRegistry.getStoragePath());
        fileRegistryRepository.delete(imageRegistry);
    }

    public void deleteOwnerImage(ImageRegistry imageRegistry) throws IOException {
        storageManager.deleteOwnerImage(imageRegistry.getStoragePath());
        fileRegistryRepository.delete(imageRegistry);
    }

    public Optional<ImageRegistry> findById(UUID fileId) {
        return fileRegistryRepository.findById(fileId);
    }

    public Resource getFileAsResource(UUID fileId) throws FileNotFoundException {
        ImageRegistry file = fileRegistryRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        try {
            Path filePath = Path.of(file.getStoragePath()).toAbsolutePath();
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException();
            }
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException();
        }
    }

    public String getImageUrl(UUID fileId) {
        if(fileId == null){
            return "null";
        }
        return serverUrl + "/image/" + fileId;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupTempFiles() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(tempExpirationHours);
        List<ImageRegistry> oldTempFiles = fileRegistryRepository.findByStatusAndUploadedAtBefore(ImageStatus.TEMP, threshold);

        oldTempFiles.forEach(file -> {
            try {
                storageManager.deleteFile(file.getStoragePath());
                fileRegistryRepository.delete(file);
            } catch (IOException e) {
                log.debug("Ошибка при очистке изображений: " + e.getMessage());
            }
        });
    }

}