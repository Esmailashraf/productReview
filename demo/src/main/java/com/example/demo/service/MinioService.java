package com.example.demo.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file) {

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        boolean exist = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!exist) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        }

        // Apply public policy whether the bucket already exists or was just created
        setPublicPolicy();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(
                                file.getInputStream(),
                                file.getSize(),
                                -1
                        )
                        .contentType(file.getContentType())
                        .build()
        );

        return minioUrl + "/" + bucketName + "/" + fileName;
    }

    @SneakyThrows
    private void setPublicPolicy() {

        String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": {
                        "AWS": ["*"]
                      },
                      "Action": [
                        "s3:GetBucketLocation"
                      ],
                      "Resource": [
                        "arn:aws:s3:::%s"
                      ]
                    },
                    {
                      "Effect": "Allow",
                      "Principal": {
                        "AWS": ["*"]
                      },
                      "Action": [
                        "s3:GetObject"
                      ],
                      "Resource": [
                        "arn:aws:s3:::%s/*"
                      ]
                    }
                  ]
                }
                """.formatted(bucketName, bucketName);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policy)
                        .build()
        );
    }
}
