package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.ImageStatus;
import com.example.kptc_smp.repository.main.FileRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageRegistryManager {
    private final FileRegistryRepository fileRegistryRepository;

    @Value("${server.url}")
    private String serverUrl;

    public ImageResponse createTempRegistry(MultipartFile file, UUID fileId, Path storagePath) {
        ImageRegistry registry = new ImageRegistry();
        registry.setId(fileId);
        registry.setOriginalName(file.getOriginalFilename());
        registry.setMimeType(file.getContentType());
        registry.setSize(file.getSize());
        registry.setStoragePath(storagePath.toString());
        registry.setStatus(ImageStatus.TEMP);
        registry.setUploadedAt(LocalDateTime.now());

        fileRegistryRepository.save(registry);
        return toFileResponse(registry);
    }

    public ImageResponse createAttachedRegistry(MultipartFile file, UUID fileId, Path storagePath,
                                                ImageCategory category, Integer ownerId) {
        ImageRegistry registry = new ImageRegistry();
        registry.setId(fileId);
        registry.setOriginalName(file.getOriginalFilename());
        registry.setMimeType(file.getContentType());
        registry.setSize(file.getSize());
        registry.setStoragePath(storagePath.toString());
        registry.setStatus(ImageStatus.ATTACHED);
        registry.setImageCategory(category);
        registry.setOwnerId(ownerId);
        registry.setUploadedAt(LocalDateTime.now());
        registry.setAttachedAt(LocalDateTime.now());

        fileRegistryRepository.save(registry);
        return toFileResponse(registry);
    }

    public void updateRegistry(ImageRegistry file, String newStoragePath, ImageCategory category) {
        file.setStoragePath(newStoragePath);
        file.setImageCategory(category);
        file.setStatus(ImageStatus.ATTACHED);
        file.setAttachedAt(LocalDateTime.now());
        fileRegistryRepository.save(file);
    }

    private ImageResponse toFileResponse(ImageRegistry registry) {
        return ImageResponse.builder()
                .id(registry.getId())
                .originalName(registry.getOriginalName())
                .mimeType(registry.getMimeType())
                .size(registry.getSize())
                .uploadedAt(registry.getUploadedAt())
                .downloadUrl(serverUrl + "/api/files/" + registry.getId())
                .build();
    }
}
