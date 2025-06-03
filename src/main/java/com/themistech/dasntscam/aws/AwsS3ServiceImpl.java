package com.themistech.dasntscam.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.themistech.dasntscam.dto.VideoDTO;
import com.themistech.dasntscam.responses.VideoListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class AwsS3ServiceImpl implements AwsS3Service{
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3ServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    //Subir archivo
    @Override
    public void uploadFile(MultipartFile file) {
        File mainFile = new File(file.getOriginalFilename());
        try (FileOutputStream stream = new FileOutputStream(mainFile)) {
            stream.write(file.getBytes());
            String newFileName = System.currentTimeMillis() + "_" + mainFile.getName();
            LOGGER.info("Subiendo archivo con el nombre... " + newFileName);
            PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, mainFile);
            amazonS3.putObject(request);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    //Recuperar lista de todos los videos
    @Override
    public VideoListResponse getVideoList() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        List<VideoDTO> videos = objects.stream()
                .map(obj -> createVideoFromKey(obj.getKey()))
                .collect(Collectors.toList());

        return new VideoListResponse(videos);
    }

    //Descargar archivo
    @Override
    public InputStream downloadFile(String key) {
        LOGGER.info("Descargando archivo...");
        S3Object object = amazonS3.getObject(bucketName, key);
        return object.getObjectContent();
    }

    //Imagen provisional
    private static final String IMAGE_URL = "https://imgs.search.brave.com/4_HyfUmqSVQQXLdfzxfeAl9RHDsgQ9pmTnEmXzBla3I/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YS5nZXR0eWltYWdl/cy5jb20vaWQvNjYw/Mzg2MzI4L3B0L2Zv/dG8vc2lkZXZpZXct/bWlycm9yLW9uLWEt/bW92aW5nLWNhci5q/cGc_cz02MTJ4NjEy/Jnc9MCZrPTIwJmM9/WmpQTFFGZ2J1MVZn/RklKcXNLTEx1YlJG/OXhhOGFzdElSOUZp/bVdqVlBOcz0";
    //Desglosar nombre de video y obtener fecha y hora
    private VideoDTO createVideoFromKey(String key) {
        String[] parts = key.split("_");

        if (parts.length < 5) {
            // Si el formato no es válido, devolver objeto con valores por defecto
            return new VideoDTO(key, IMAGE_URL, "N/A", "N/A", "N/A", false);
        }

        String rawDate = parts[2];
        String rawTime = parts[3];

        // Formato de salida
        String date = rawDate.substring(0, 4) + "-" + rawDate.substring(4, 6) + "-" + rawDate.substring(6, 8);
        String time = rawTime.substring(0, 2) + ":" + rawTime.substring(2, 4) + ":" + rawTime.substring(4, 6);
        String dateTime = date + "T" + time;

        return new VideoDTO(key, IMAGE_URL, date, time, dateTime, false);
    }
}
