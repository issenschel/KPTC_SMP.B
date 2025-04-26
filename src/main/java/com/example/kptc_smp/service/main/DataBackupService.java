package com.example.kptc_smp.service.main;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.utility.ZipUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class DataBackupService {
    private final GoogleDriveService googleDriveService;
    private final ZipUtils zipUtils;

    @Value("${upload.path.directory}")
    private Path sourceDirectoryPath;

    @Value("${backup.image.path.directory}")
    private Path backupDirectoryPath;

    @Value("${google.backup.folder.id}")
    private String googleFolderId;

    @Scheduled(cron = "0 0 2 * * SAT")
    public void createBackupData() {
        File zipArchive = zipUtils.createZipArchive(sourceDirectoryPath.toAbsolutePath(), backupDirectoryPath.toAbsolutePath());
        googleDriveService.uploadZipToDrive(zipArchive, googleFolderId);
    }

    public ResponseDto restoreBackupData(String zipFileName) {
        Path zipArchivePath = backupDirectoryPath.toAbsolutePath().resolve(zipFileName + ".zip");
        zipUtils.unzipArchive(sourceDirectoryPath.toAbsolutePath(), zipArchivePath);
        return new ResponseDto("Распаковка прошла успешно");
    }

    public ResponseDto downloadBackupData(String zipFileName) {
        googleDriveService.downloadFileByName(zipFileName, backupDirectoryPath.toAbsolutePath());
        return new ResponseDto("Архив скачен");
    }

}
