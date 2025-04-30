package com.example.kptc_smp.entity.main;

import com.example.kptc_smp.enums.ImageCategory;
import com.example.kptc_smp.enums.ImageStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "image_registry")
public class ImageRegistry {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "original_name")
    private String originalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImageStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ImageCategory imageCategory;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private Long size;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "attached_at")
    private LocalDateTime attachedAt;
}
