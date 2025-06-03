package com.themistech.dasntscam.aws;

import java.io.InputStream;
import java.util.List;

import com.themistech.dasntscam.responses.VideoListResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

    void uploadFile(MultipartFile file);

    VideoListResponse getVideoList();

    InputStream downloadFile(String key);
}
