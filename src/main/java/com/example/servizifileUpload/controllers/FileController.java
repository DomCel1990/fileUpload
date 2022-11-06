package com.example.servizifileUpload.controllers;

import com.example.servizifileUpload.services.FileService;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) {
        return fileService.upload(file);
    }

    @SneakyThrows
    @GetMapping("/download")
    public @ResponseBody byte[] download(@RequestParam String fileName, HttpServletResponse response) {
        fileService.matchException(fileName, response);
        return fileService.download(fileName);
    }
}


