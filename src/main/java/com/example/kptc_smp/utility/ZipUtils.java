package com.example.kptc_smp.utility;

import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.zip.ZipException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
public class ZipUtils {

    public File createZipArchive(Path sourceDirectoryPath, Path zipFilePath) {
        File zipFile = zipFilePath.resolve(LocalDate.now() + ".zip").toFile();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            Files.walkFileTree(sourceDirectoryPath, new SimpleFileVisitor<>() {

                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String zipEntryName = sourceDirectoryPath.relativize(file).toString();
                    zipOutputStream.putNextEntry(new ZipEntry(zipEntryName));
                    Files.copy(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    String zipEntryName = sourceDirectoryPath.relativize(dir) + "/";
                    zipOutputStream.putNextEntry(new ZipEntry(zipEntryName));
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new ZipException("Ошибка при создании архива" + e);
        }

        return zipFile;
    }

    public void unzipArchive(Path destinationDir, Path zipFilePath) {
        File directory = destinationDir.toFile();
        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File file = new File(directory, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    if (file.exists()) {
                        continue;
                    }
                    file.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = new FileOutputStream(file)) {
                        copy(in, out);
                    }
                }
            }
        } catch (NoSuchFileException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new ZipException("Ошибка при распаковке архива" + e);
        }
    }


    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int readCount;
        while ((readCount = in.read(buffer)) != -1) {
            out.write(buffer, 0, readCount);
        }
    }

}
