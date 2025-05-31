package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.dto.image.ImageResponseDto;
import com.example.kptc_smp.model.main.ImageRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageMapperService {
    private final ImagePathBuilderService imagePathBuilderService;

    public ImageResponseDto toImageResponse(ImageRegistry registry) {
        return ImageResponseDto.builder()
                .id(registry.getId())
                .originalName(registry.getOriginalName())
                .mimeType(registry.getMimeType())
                .size(registry.getSize())
                .uploadedAt(registry.getUploadedAt())
                .imageUrl(imagePathBuilderService.getImageUrl(registry.getId()))
                .build();
    }
}
