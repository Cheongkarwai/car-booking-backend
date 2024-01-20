package com.volvo.carbookingbackend.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileService {

    String uploadFile(String path, String bucket);

    List<String> uploadFile(String bucket, String key, MultipartFile... files) throws IOException;

    void deleteFile(String path);

    void deleteFiles(List<String> paths);
    void download(String url);
}
