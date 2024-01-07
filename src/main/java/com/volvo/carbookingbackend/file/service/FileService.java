package com.volvo.carbookingbackend.file.service;

import com.volvo.carbookingbackend.configuration.AwsS3Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService implements  IFileService {

    public final S3Client s3Client;

    //has better performance.
    private final S3TransferManager s3TransferManager;

    private final AwsS3Configuration awsS3Configuration;

    public FileService(S3Client s3Client,S3TransferManager s3TransferManager,AwsS3Configuration awsS3Configuration){
        this.s3Client = s3Client;
        this.s3TransferManager = s3TransferManager;
        this.awsS3Configuration = awsS3Configuration;
    }


    @Override
    public String uploadFile(String path, String bucket) {

//        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
//                .putObjectRequest(b->
//                        b.bucket(awsS3Configuration.getBucketName())
//                        .key("lol.txt")
//                        .acl(ObjectCannedACL.PUBLIC_READ))
//                .addTransferListener(LoggingTransferListener.create())
//                .source(Paths.get(path))
//                .build();
//
//        FileUpload fileUpload = s3TransferManager.uploadFile(uploadFileRequest);
//        CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
//        return uploadResult.response().eTag();

        Path sourcePath = Paths.get(path);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(sourcePath.getFileName().toString())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(objectRequest, sourcePath);
        return putObjectResponse.eTag();
    }

    public List<String> uploadFile(String bucket,String key, MultipartFile... files) throws IOException {

        List<String> filePath = new ArrayList<>();

        int i = 1;
        for(MultipartFile multipartFile : files){
            File file = toFile(multipartFile);

            String newKey = key +"/" + key+ "-"+ i;
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(newKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(objectRequest, RequestBody.fromFile(file));

            filePath.add(s3Client.utilities().getUrl(r->r.bucket(bucket).key(newKey)).toExternalForm());
            i++;
        }

        return filePath;
    }

    private File  toFile(MultipartFile multipartFile) throws IOException {

        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        return file;
    }

    public void download(String url){

    }
}
