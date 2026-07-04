package com.sawari.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file, String subFolder) throws IOException {
        Path folderPath = Paths.get(uploadDir + subFolder);
        Files.createDirectories(folderPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = folderPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uploadDir + subFolder + "/" + fileName;
    }
}