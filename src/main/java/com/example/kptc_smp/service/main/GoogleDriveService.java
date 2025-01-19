package com.example.kptc_smp.service.main;

import com.example.kptc_smp.exception.google.GoogleDriveException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoogleCredentials();

    private static String getPathToGoogleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "google.json");
        return filePath.toString();
    }

    public void uploadZipToDrive(File file, String folderId) {
        try {
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("application/zip", file);
            drive.files().create(fileMetaData, mediaContent).setFields("id").execute();
        } catch (Exception e) {
            throw new GoogleDriveException("Не удалось загрузить файл на гугл диск: " + e.getMessage());
        }
    }

    public void downloadFileByName(String fileName, Path destinationPath) {
        try {
            Drive drive = createDriveService();

            FileList result = drive.files().list()
                    .setQ("name='" + fileName + "'")
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                throw new FileNotFoundException("Файл не найден: " + fileName);
            }

            String fileId = result.getFiles().getFirst().getId();

            OutputStream outputStream = new FileOutputStream(destinationPath.resolve(result.getFiles().getFirst().getName()).toFile());
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            outputStream.close();
        } catch (Exception e) {
            throw new GoogleDriveException("Не удалось скачать файл: " + e.getMessage());
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
