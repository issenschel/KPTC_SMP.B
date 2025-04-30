package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.entity.main.ImageRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageStorageManager {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public void storeImage(MultipartFile file, String objectKey) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public void moveImage(String sourceKey, String destinationKey) {
        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(sourceKey)
                .destinationBucket(bucketName)
                .destinationKey(destinationKey)
                .build();

        s3Client.copyObject(copyRequest);
        deleteImage(sourceKey);
    }

    public void deleteImage(String objectKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (S3Exception e) {
            log.debug("Ошибка при удалении файла: {}", e.getMessage());
        }
    }

    public void deleteFilesByRegistryList(List<ImageRegistry> imageRegistries) {
        try {
            List<ObjectIdentifier> objectsToDelete = imageRegistries.stream()
                    .map(ImageRegistry::getStoragePath)
                    .filter(Objects::nonNull)
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());
            if (!objectsToDelete.isEmpty()) {
                int batchSize = 1000;
                for (int i = 0; i < objectsToDelete.size(); i += batchSize) {
                    List<ObjectIdentifier> batch = objectsToDelete.subList(i,
                            Math.min(i + batchSize, objectsToDelete.size()));

                    DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                            .bucket(bucketName)
                            .delete(Delete.builder()
                                    .objects(batch)
                                    .build())
                            .build();

                    s3Client.deleteObjects(deleteRequest);
                }
            }
        } catch (S3Exception e) {
            log.debug("Ошибка при удалении изображений: {}", e.getMessage());
        }
    }

    public InputStream getFileStream(String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        return s3Client.getObject(getObjectRequest);

    }

}