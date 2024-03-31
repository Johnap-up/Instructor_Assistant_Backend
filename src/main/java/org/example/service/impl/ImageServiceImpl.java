package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.entity.dto.Account;
import org.example.mapper.AccountMapper;
import org.example.service.ImageService;
import org.example.utils.ConstUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    MinioClient minioClient;
    @Resource
    AccountMapper accountMapper;
    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try{
            minioClient.putObject(args);
            if (accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id", id).set("avatar", imageName)) > 0){
                return imageName;
            }else {
                return null;
            }
        }catch (Exception e){
            log.info("图片上传出现问题" + e.getMessage());
            return null;
        }
    }
    @Override
    public void fetchImageFromMinio(OutputStream stream, String image) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(ConstUtil.BUCKET_INSTRUCTOR)
                .object(image)
                .build();
        GetObjectResponse response = minioClient.getObject(args);
        IOUtils.copy(response, stream);
    }
}














