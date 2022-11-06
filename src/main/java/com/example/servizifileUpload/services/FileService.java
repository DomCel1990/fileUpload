package com.example.servizifileUpload.services;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    @SneakyThrows
    public String upload(MultipartFile file) {
        String exception = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString();
        String nameFileComplete = fileName + "." + exception;

        File finalFolder = new File(fileRepositoryFolder);
        if (!finalFolder.exists()) throw new IOException("Final folder doesn't exist");
        if (!finalFolder.isDirectory()) throw new IOException("Final folder is not a Directory");

        File finalDestination = new File(fileRepositoryFolder + "\\" + nameFileComplete);
        if (finalDestination.exists()) throw new IOException("File conflict");

        file.transferTo(finalDestination);
        return nameFileComplete;
    }

    public void matchException(String fileName, HttpServletResponse response) {
        String extension= FilenameUtils.getExtension(fileName);
        switch (extension) {
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\"");
    }

    @SneakyThrows
    public byte[] download(String fileName) {
        File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
        if (!fileFromRepository.exists()) throw new IOException("file no exist");
        return IOUtils.toByteArray(new FileInputStream(fileName));
    }
}
