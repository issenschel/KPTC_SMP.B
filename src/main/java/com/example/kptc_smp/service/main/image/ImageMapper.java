package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.dto.image.ImageResponse;
import com.example.kptc_smp.entity.main.ImageRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageMapper {
    private final ImagePathBuilder imagePathBuilder;

    public ImageResponse toImageResponse(ImageRegistry registry) {
        return ImageResponse.builder()
                .id(registry.getId())
                .originalName(registry.getOriginalName())
                .mimeType(registry.getMimeType())
                .size(registry.getSize())
                .uploadedAt(registry.getUploadedAt())
                .downloadUrl(imagePathBuilder.getImageUrl(registry.getId()))
                .build();
    }
}
