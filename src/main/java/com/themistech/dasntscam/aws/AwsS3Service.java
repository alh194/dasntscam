package com.themistech.dasntscam.aws;

import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

    void uploadFile(MultipartFile file);

    List<String> getObjectsFromS3();

    InputStream downloadFile(String key);
}
