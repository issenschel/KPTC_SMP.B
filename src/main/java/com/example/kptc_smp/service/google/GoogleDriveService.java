package com.example.kptc_smp.service.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    @Value("${google.drive.folder.image.profile.id}")
    private String imageProfileFolderId;

    @Value("${google.drive.folder.news.id}")
    private String newsFolderId;

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "google.json");
        return filePath.toString();
    }

    public void uploadImageToDrive(File file,String folderId) throws IOException {
        try{
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpeg", file);
            drive.files().create(fileMetaData, mediaContent).setFields("id").execute();
        }catch (Exception e){

        }
    }

    public String createFolder(String folderName, String folderId){
        try {
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            fileMetaData.setParents(Collections.singletonList(folderId));
            com.google.api.services.drive.model.File folder = drive.files().create(fileMetaData).setFields("id").execute();
            return folder.getId();
        }catch (Exception ignored){
            return null;
        }
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential).setApplicationName("KPTC")
                .build();

    }
}