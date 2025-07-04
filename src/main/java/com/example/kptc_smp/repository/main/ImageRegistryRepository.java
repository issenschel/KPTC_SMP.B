package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.enums.ImageStatus;
import com.example.kptc_smp.model.main.ImageRegistry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRegistryRepository extends CrudRepository<ImageRegistry, Long> {
    Optional<ImageRegistry> findById(UUID Id);

    List<ImageRegistry> findByStatusAndUploadedAtBefore(ImageStatus status, LocalDateTime date);

    List<ImageRegistry> findByOwnerId(Integer ownerId);
}
