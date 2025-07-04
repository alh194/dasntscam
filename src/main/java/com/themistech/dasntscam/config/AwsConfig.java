package com.themistech.dasntscam.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;

@Configuration
public class AwsConfig {

    @Value("${aws.access_key_id}")
    private String accessKeyId;

    @Value("${aws.secret_access_key}")
    private String accessSecretKey;

    @Value("${aws.s3.region}")
    private String region;

    //En un futuro si se quiere usar diferentes buckets, usar esta configuracion modificando los valores accessKeyId, accessSecretKey y region en application.properties
    @Bean
    public AmazonS3 getS3Client() {

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessSecretKey);
        Region awsRegion = RegionUtils.getRegion(region);

        // Crear y devolver el cliente S3
        return AmazonS3Client.builder()
                .withRegion(awsRegion.getName())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
