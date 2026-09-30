package com.example.demo.config;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.secret-key}")
    private String minioSecret;

    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minioUrl).credentials(minioAccessKey, minioSecret).build();
    }
}
