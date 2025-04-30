package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.ImageRegistry;
import com.example.kptc_smp.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRegistryRepository extends JpaRepository<ImageRegistry, Long> {
    Optional<ImageRegistry> findById(UUID Id);
    List<ImageRegistry> findByStatusAndUploadedAtBefore(ImageStatus status, LocalDateTime date);
}
