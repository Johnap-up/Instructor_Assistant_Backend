package org.example.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    MinioClient minioClient;
    @Override
    public String uploadLearning(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace(",", "");
        imageName = "/learning/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket("instructor")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try{
            minioClient.putObject(args);
            return imageName;
        }catch (Exception e){
            log.info("图片上传出现问题" + e.getMessage());
            return null;
        }
    }
}
