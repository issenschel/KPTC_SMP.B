package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.ImageStatus;
import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.repository.main.ImageRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageStorageService {
    private final ImageRegistryRepository imageRegistryRepository;
    private final ImageStorageManager storageManager;
    private final ImagePathBuilder pathBuilder;
    private final ImageValidator imageValidator;
    private final ImageRegistryManager registryManager;

    @Value("${file.storage.temp-expiration-hours}")
    private int tempExpirationHours;

    @Value("${server.url}")
    private String serverUrl;

    @Transactional
    public ImageResponse uploadTempImage(MultipartFile image){
        imageValidator.validateImage(image);

        UUID fileId = UUID.randomUUID();
        String extension = pathBuilder.getExtension(image.getOriginalFilename());
        String objectKey = pathBuilder.buildTempPath(fileId, extension);
        try {
            storageManager.storeImage(image, objectKey);
            return registryManager.createTempRegistry(image, fileId, objectKey);
        } catch (IOException e) {
            throw new ImageException();
        }
    }

    @Transactional
    public ImageResponse uploadAndAttachImage(MultipartFile image, ImageCategory category, Integer ownerId) {
        UUID fileId = UUID.randomUUID();
        String extension = pathBuilder.getExtension(image.getOriginalFilename());
        String objectKey = pathBuilder.buildPermanentPath(category, ownerId, fileId, extension);
        try {
            storageManager.storeImage(image, objectKey);
            return registryManager.createAttachedRegistry(image, fileId, objectKey, category, ownerId);
        } catch (IOException e){
                throw new ImageException();
        }
    }

    @Transactional
    public ImageResponse updateImage(MultipartFile image, ImageRegistry imageRegistry)  {
        ImageResponse response = uploadAndAttachImage(image, imageRegistry.getImageCategory(), imageRegistry.getOwnerId());
        deleteImage(imageRegistry);
        return response;
    }

    @Transactional
    public void attachImage(UUID fileId, ImageCategory category, Integer ownerId)  {
        ImageRegistry file = imageRegistryRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        if (file.getStatus() != ImageStatus.TEMP) {
            throw new ImageException();
        }

        String extension = pathBuilder.getExtension(file.getOriginalName());
        String newObjectKey = pathBuilder.buildPermanentPath(category, ownerId, fileId, extension);
        storageManager.moveImage(file.getStoragePath(), newObjectKey);
        registryManager.updateRegistry(file, newObjectKey, category);
    }

    public void deleteImage(ImageRegistry imageRegistry) {
        storageManager.deleteImage(imageRegistry.getStoragePath());
        imageRegistryRepository.delete(imageRegistry);
    }

    @Transactional
    public void deleteFolder(List<ImageRegistry> imagesRegistry) {
        storageManager.deleteFilesByRegistryList(imagesRegistry);
        imageRegistryRepository.deleteAll(imagesRegistry);
    }

    public Resource getFileAsResource(UUID fileId)  {
        ImageRegistry file = imageRegistryRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        try {
            InputStream inputStream = storageManager.getFileStream(file.getStoragePath());
            return new InputStreamResource(inputStream);
        }catch (Exception e){
            throw new FileNotFoundException();
        }
    }

    public String getImageUrl(UUID id){
        return pathBuilder.getImageUrl(id);
    }

    public Optional<ImageRegistry> findById(UUID fileId) {
        return imageRegistryRepository.findById(fileId);
    }

    public List<ImageRegistry> findByOwnerId(Integer ownerId) {
        return imageRegistryRepository.findByOwnerId(ownerId);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupTempFiles() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(tempExpirationHours);
        List<ImageRegistry> oldTempFiles = imageRegistryRepository.findByStatusAndUploadedAtBefore(ImageStatus.TEMP, threshold);

        oldTempFiles.forEach(file -> {
            storageManager.deleteImage(file.getStoragePath());
            imageRegistryRepository.delete(file);
        });
    }

}