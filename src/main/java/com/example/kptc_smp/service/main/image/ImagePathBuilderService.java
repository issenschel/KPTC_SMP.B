package com.example.kptc_smp.service.main.image;

import com.example.kptc_smp.enums.ImageCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImagePathBuilderService {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${image.endpoint}")
    private String imageEndpoint;

    @Value("${image.path.permanent}")
    private String permanentPathPrefix;

    @Value("${image.path.temp}")
    private String tempPathPrefix;

    public String buildPermanentPath(ImageCategory category, Integer ownerId, UUID fileId, String extension) {
        return String.format("%s/%s/%d/%s%s",
                permanentPathPrefix,category.name().toLowerCase(), ownerId, fileId, extension);
    }

    public String buildTempPath(UUID fileId, String extension) {
        return String.format("%s/%s%s",tempPathPrefix, fileId, extension);
    }

    public String getImageUrl(UUID fileId) {
        if(fileId == null){
            return "null";
        }
        return serverUrl + imageEndpoint + fileId;
    }

    public String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }

}
