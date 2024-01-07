package com.volvo.carbookingbackend.file.controller;

import com.volvo.carbookingbackend.file.service.IFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private IFileService fileService;

    public FileController(IFileService fileService){
        this.fileService = fileService;
    }

    @PostMapping("/download")
    private void downloadFile(@RequestBody String url){
        fileService.download(url);
    }
}
