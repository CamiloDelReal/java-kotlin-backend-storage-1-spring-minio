package com.xapps.services.fileuploaderservice.services.impl;

import com.xapps.services.fileuploaderservice.services.UploaderService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class MinioUploaderService implements UploaderService {

    public static final Logger logger = LoggerFactory.getLogger(MinioUploaderService.class);

    private final MinioClient minioClient;

    @Autowired
    public MinioUploaderService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadFile(MultipartFile file) {
        logger.info("Uploading file {} to Minio...", file.getOriginalFilename());
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String bucketName = dtf.format(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());

            boolean currentDateBucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if(!currentDateBucketExists) {
                logger.debug("Bucket {} does not exist, creating it...", bucketName);
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }

            ObjectWriteResponse response =  minioClient.putObject(
                io.minio.PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(
                        file.getInputStream(),
                        file.getSize(),
                        -1
                    )
                    .contentType(file.getContentType())
                    .build()
            );
            logger.info("File uploaded successfully to Minio, response: {}", response.object());

        } catch (ServerException | InternalException | ErrorResponseException | InsufficientDataException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException e) {
            logger.error("Error while uploading file to Minio", e);
        }
    }
}
